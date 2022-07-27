<template>
  <div id="building">
    <div class="login-container">
      <el-form :model="userinfo_form" :rules="verificationRules"
               ref="userinfo_form"
               class="login-page"
               @submit.native.prevent
               size="large">
        <h3 style="font-size: 35px"  class="title">系统登录</h3>
        <el-form-item prop="jobNum">
          <el-input controls=false
                    v-model="userinfo_form.jobNum"
                    auto-complete="off"
                    placeholder="请输入学工号"></el-input>
        </el-form-item>
        <el-form-item prop="pwd">
          <el-input type="password"
                    v-model="userinfo_form.pwd"
                    auto-complete="off"
                    placeholder="请输入密码"
                    show-password></el-input>
        </el-form-item>
        <el-form-item style="width:100%;">
          <el-button native-type="submit" type="primary" style="width:100%;" @click="login('userinfo_form')">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'


export default {
  name: 'IndexView',
  data(){
    const validatePwd = (rule, value, callback) => {
      const otherReg = new RegExp('[^0-9a-zA-Z-_]')
      const pwdRegs = [new RegExp('[0-9]'), new RegExp('[a-zA-Z]'), new RegExp('[-_]')]
      if (value.length<6 || value.length>32) {
        callback(new Error('长度为6-32个字符！'))
      } else if(otherReg.test(value)) {
        callback(new Error('不能包含字母，数字，[-_]之外的字符！'))
      } else {
        var typeCnt = 0
        for(var r of pwdRegs){
          if(r.test(value)) typeCnt++
        }
        if(typeCnt<2) callback(new Error('字母，数字或者特殊字符(-_)至少包含两种！'))
      }
      callback()
    }
    return {
      userinfo_form: {
        jobNum: '',
        pwd: '',
      },
      alterPassword_form: {
        alteredPwdOriginal: '',
        alteredPwd: '',
        alteredPwdConfirm: ''
      },
      verificationRules: {
        jobNum: [
            {required: true, message: '请输入学工号', trigger:'blur'},
            {pattern:/(^[0-9]{6}$)|(^[0-9]{8}$)/, message: '请输入6或8位的学工号', trigger: ['change', 'blur']},
        ],
        pwd: {required: true, message: '请输入密码', trigger:['change', 'blur']},
        alteredPwdOriginal: {required: true, message: '请输入初始密码', trigger:['change', 'blur']},
        alteredPwd: [
            {required: true, message: '请输入修改后的密码', trigger:['change', 'blur']},
            {validator: validatePwd, trigger:['change', 'blur']},
        ],
        alteredPwdConfirm: {required: true, message: '请再次输入修改后的密码', trigger:['change', 'blur']}
      }
    }
  },
  methods: {
    saveToken(token) {
      return new Promise(function(resolve, reject) {
        localStorage.setItem('token', token)
        console.log(token)
        resolve()
      })
    },

    login(formName) {
      let _this = this;
      this.$refs[formName].validate((valid) => {
        if(!valid) { //格式不正确
          _this.userinfo_form.jobNum=''
          _this.userinfo_form.pwd='';//登录失败后重置输入栏
          // ElMessage.closeAll()
          ElMessage.error({
            message:'登陆失败！请检查输入格式后再登录！',
            duration: 1000,
            grouping: true,

          });
        } else { //格式正确 发送请求
          this.axios({
            method: 'post',
            url: 'http://121.37.98.3:9090/login',

            data: {
              jobNum: _this.userinfo_form.jobNum,
              pwd: _this.userinfo_form.pwd
            }
          }).then(res => {
            if(res.data.success){
              if(res.data.data.passwordChange){
                ElMessage({
                  message: '登录成功',
                  type: 'success',
                })
              }
              _this.saveToken(res.data.data.token).then(()=>{
                _this.$store.commit('SET_passwordChange', res.data.data.passwordChange);
                _this.$router.push('home')
              })
            } else {
              this.$message.closeAll()
              ElMessage({
                message: res.data.message,
                type: 'error'
              })
            }
          }).catch(error => {
            console.log(error)
          });
        }
      })
    },


  }
};
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100%;
}
#building{
  background:url("../assets/img/fddx.png");
  width:100%;
  height:100%;
  position:fixed;
  margin-left: -8px;
  margin-top: -8px;
  /*background-repeat: no-repeat;*/


}

.login-page {
  -webkit-border-radius: 5px;
  border-radius: 5px;
  margin: 180px auto;
  width: 350px;
  padding: 5px 35px 15px;
  /*background: aliceblue;*/
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 10px #cac6c6;



}
label.el-checkbox.rememberme {
  margin: 0px 0px 15px;
  text-align: left;
}
</style>
