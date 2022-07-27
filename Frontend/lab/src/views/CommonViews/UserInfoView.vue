<template>
  <el-menu style="min-height: 10vh" class="el-menu-demo" mode="horizontal">
    <arrow-left style="width: 2%; text-align: left;margin-left: 2%;" @click="goBack"></arrow-left>
    <h2 style="width: 35%;margin-left: 4%;text-align: left; font-family: 幼圆">我的个人信息</h2>
  </el-menu>
  <!--  表单-->
  <div class="form_border" style="">
    <el-form ref="user_info_form_ref" :model="user_info_form" label-width="120px" class = "form_content" :rules="verificationRules">
      <el-divider content-position="left" border-style="dotted">基本信息</el-divider>
      <el-row span="16">
        <!--    身份证号-->
        <el-form-item prop="idcardNum" label="身份证号">
          <el-input  disabled="true" v-model="user_info_form.idcardNum" />
        </el-form-item>
        <!--    身份-->
        <el-form-item prop="identity" label="身份">
          <el-input  disabled="true" v-model="user_info_form.identity" />
        </el-form-item>
      </el-row>
      <el-divider content-position="left" border-style="dotted">院系信息</el-divider>
      <el-row span="16">
        <!--    院系-->
        <el-form-item prop="institute" label="学院">
          <el-input  disabled="true" v-model="user_info_form.institute" />
        </el-form-item>
        <!--    专业-->
        <el-form-item prop="major" label="专业">
          <el-input disabled="true" v-model="user_info_form.major" />
        </el-form-item>
      </el-row>
      <el-row span="16">
        <!--    姓名-->
        <el-form-item prop="username" label="姓名">
          <el-input  disabled="true" v-model="user_info_form.username" />
        </el-form-item>
        <!--    学号-->
        <el-form-item prop="jobNum" label="学工号">
          <el-input disabled="true" v-model="user_info_form.jobNum" />
        </el-form-item>
      </el-row>
      <el-divider content-position="left" border-style="dotted">其他信息</el-divider>
      <!--    邮箱-->
      <el-row span="16">
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="user_info_form.email" />
        </el-form-item>
        <!--    手机号-->
        <el-form-item prop="phoneNum" label="手机号">
          <el-input v-model="user_info_form.phoneNum" />
        </el-form-item>
        <el-button type="primary" style="margin-left: 14%" @click="changeUserInfo">修改信息</el-button>
      </el-row>
      <el-divider content-position="left" border-style="dotted">修改密码</el-divider>
      <el-row span="16">
        <!--    密码-->
        <el-form-item prop="pwd" label="新密码">
          <el-input v-model="user_info_form.pwd" type="password" show-password/>
        </el-form-item>
        <!--    确认密码-->
        <el-form-item prop="confirm_pwd" label="确认新密码">
          <el-input v-model="user_info_form.confirm_pwd" type="password" show-password placeholder="请再次输入密码"/>
        </el-form-item>
        <el-button type="primary" style="margin-left: 10%" @click="changePwd">修改密码</el-button>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import {ArrowLeft} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import { ElLoading } from 'element-plus'
import store from "@/assets/store";
import {logout} from "@/util/StatusCheck";
export default {
  name: "UserInfoView",
  components:{
    ArrowLeft
  },
  methods:{
    //提交修改个人信息
    changeUserInfo: function (){
      this.$refs.user_info_form_ref.validateField(['email','phoneNum'],(ifValid)=>{
        if(ifValid){
          const loadingInstance=ElLoading.service()
          this.axios({
            method:"post",
            url: 'http://121.37.98.3:9090/alter/info',
            headers:{
              'token':localStorage.getItem("token"),
              'Context-Type':'application/json'
            },
            data: {
              jobNum: this.user_info_form.jobNum,
              email: this.user_info_form.email,
              phoneNum: this.user_info_form.phoneNum
            }
          }).then(res=>{
            if(res.data.success){
              loadingInstance.close();
                ElMessage.success({message : "修改成功", duration : 1500})
              //更新store
              store.commit('SET_PhoneNum', this.user_info_form.phoneNum)
              store.commit('SET_Email', this.user_info_form.email)
              console.log(this.$store.state)
            }
            else{
              ElMessage.error({message: res.data.message, duration:1500})
            }
          })
        }
        else{
          ElMessage.error({
            message: "表单有误,请检查后提交",
            duration:1500
          })
        }
      })
    },
    //修改密码
    changePwd: function (){
      let _this = this;
      this.$refs.user_info_form_ref.validateField(['pwd','confirm_pwd'],(ifValid)=>{
        if(ifValid){
         //格式正确但是两次密码输入不一致
         if(_this.user_info_form.pwd!==_this.user_info_form.confirm_pwd){
           ElMessage.error({
             message: "两次密码输入不一致!",
             duration:2000
           })
         }
         else{
           const loadingInstance=ElLoading.service()
           this.axios({
             method:"post",
             url: 'http://121.37.98.3:9090/alter/pwd',
             headers:{
               'token':localStorage.getItem("token"),
               'Context-Type':'application/json'
             },
             data: {
               jobNum: this.user_info_form.jobNum,
               pwd: this.user_info_form.pwd
             }
           }).then(res=>{
             loadingInstance.close();
             if(res.data.success){
               logout("修改成功,请重新登录")
             }
             else{
               ElMessage.error({message: res.data.message, duration:1500})
             }
           })
         }
        }
        //格式不正确
        else{
          ElMessage.error({
            message: "表单有误,请检查后提交",
            duration:1500
          })
        }
      })
    },
    //返回上一级
    goBack:function (){
      this.$router.go(-1);
    }
  },
  data(){
    //校验密码的校验器
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
    return{
      user_info_form:{
        email:this.$store.state.email,
        phoneNum:this.$store.state.phoneNum,
        pwd:"",
        confirm_pwd:"",
        institute:this.$store.state.institute,
        major:this.$store.state.major,
        username:this.$store.state.username,
        jobNum:this.$store.state.jobNum,
        idcardNum:this.$store.state.idcardNum,
        identity:this.$store.state.identity
      },
      verificationRules:{
        email: [
          {type:'email',message: '请输入正确格式email',trigger: ['change','blur']},
        ],
        phoneNum: [
          {pattern: /^1[0-9]{10}$/,message: '请以数字1开头书写11位手机号'},
        ],
        pwd: [
          {required: true, message: '请输入修改后的密码', trigger:['change', 'blur']},
          {validator: validatePwd, trigger:['change', 'blur']},
        ],
        confirm_pwd:[
          {required: true, message: '请输入修改后的密码', trigger:['change', 'blur']},
          {validator: validatePwd, trigger:['change', 'blur']},
        ]
      }
    }
  }

}
</script>

<style scoped>
  .head_title{
    text-align: center;
    background-color: #F8F8FF;
    min-height: 10vh;
    margin-left: 2%;
  }
  .form_border{
    background: white;
    margin: 3%;
    border-radius: 2%;
    box-shadow: 3px 3px 3px 3px #cac6c6;
  }
  .form_content{
    text-align: center;
    padding: 5%;
  }
</style>
