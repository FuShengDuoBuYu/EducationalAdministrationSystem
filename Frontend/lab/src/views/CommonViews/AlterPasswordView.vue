<template>
<!--    修改初始密码-->
  <el-dialog v-model="dialogPasswordFormVisible" :close-on-click-modal='false' :show-close='false' title="修改密码">
    <el-alert title="检测到您的密码还为初始密码，请自行修改后重新登录。" type="warning" :closable="false" center show-icon/>
    <br>
    <el-form ref="alterPassword_form" :model="alterPassword_form" :label-position="labelPosition"
              label-width="120px" :rules="verificationRules"
              >
<!--        初始密码-->
      <el-form-item label="初始密码" prop="alteredPwdOriginal">
        <el-input type="password"
                  v-model="alterPassword_form.alteredPwdOriginal"
                  placeholder="请输入初始密码"
                  show-password></el-input>
      </el-form-item>
<!--        修改后的密码-->
      <el-form-item label="修改密码" prop="alteredPwd">
        <el-input type="password"
                  v-model="alterPassword_form.alteredPwd"
                  placeholder="请输入修改后的密码"
                  show-password></el-input>
      </el-form-item>
<!--        再次输入修改后的密码-->
      <el-form-item label="再次确认" prop="alteredPwdConfirm">
        <el-input type="password"
                  v-model="alterPassword_form.alteredPwdConfirm"
                  placeholder="请再次输入修改后的密码"
                  show-password></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
    <span class="dialog-footer">
      <el-button @click="alterLogout">退出登录</el-button>
      <el-button type="primary" @click="confirmPasswordSubmit('alterPassword_form')">提交</el-button>
    </span>
    </template>
  </el-dialog>

</template>

<script>
import {logout} from '@/util/StatusCheck'
import {ElMessage} from "element-plus";
export default {
  name: "AlterPasswordDialog",
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
      dialogPasswordFormVisible: true,
      alterPassword_form: {
        alteredPwdOriginal: '',
        alteredPwd: '',
        alteredPwdConfirm: ''
      },
      verificationRules: {
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
    alterLogout(){
      logout('已登出！');
    },
    cancelPasswordSubmit(){
      // 取消显示
      this.dialogPasswordFormVisible = false;
      // 清空表单
      this.$refs["alterPassword_form"].resetFields();
    },

    confirmPasswordSubmit(formName){
      let _this = this;
      this.$refs[formName].validate((valid) => {
        if(!valid) {
          ElMessage.error({
            message: '修改失败！请检查输入格式后再提交！',
            grouping: true
          })

        } else {
          if(_this.alterPassword_form.alteredPwdOriginal!=='123456'){
            ElMessage.error({
              message: '原密码错误！，修改失败！',
              grouping: true
            })
          } else if(_this.alterPassword_form.alteredPwd!==_this.alterPassword_form.alteredPwdConfirm) {
            ElMessage.error({
              message: '两次修改后的密码不同，请再次检查！',
              grouping: true
            })
          } else {
            console.log(_this.$store.state)
            this.axios({
              method: 'post',
              url: 'http://121.37.98.3:9090/alter/pwd',
              data: {
                jobNum: _this.$store.state.jobNum,
                pwd: _this.alterPassword_form.alteredPwd
              },
              headers:{
                'token':localStorage.getItem("token"),
                'Context-Type':'application/json'
              },
            }).then(res => {
              console.log(res.data)
              if(res.data.success){
                logout('修改成功！请重新登录！')
                _this.$router.push('index')
              } else {
                ElMessage.error(res.data.message)
              }
            }).catch(error => {
              console.log(error)
            });
          }
        }
      })
    }
  }
};

</script>

<style scoped>

</style>
