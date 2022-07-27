<template>
  <el-form style="padding-left: 5%" ref="addUserForm" :model="form" :label-position="labelPosition" label-width="120px" :rules="verificationRules">
    <!--        用户角色-->
    <el-form-item label="用户角色" prop="identity" >
      <el-radio-group v-model="form.identity" :disabled="ifDisableIdentity">
        <el-radio label="教师"></el-radio>
        <el-radio label="学生"></el-radio>
      </el-radio-group>
    </el-form-item>
<!--    院校专业级联选择器-->
    <el-form-item label="学院/专业" prop="institute_major" >
        <el-cascader
            placeholder="请选择学院和专业"
            :options="instituteAndMajor"
            v-model="defaultInstituteAndMajor"
            @change="getInstituteAndMajor"
            filterable
            ref="cascader"
            style="width: 100%"
        />
    </el-form-item>
<!--    状态 -->
    <el-form-item label="用户状态" prop="status" v-if="ifShowStatusSelect">
        <el-select v-model="form.statusId" size="middle" placeholder="选择用户状态">
          <el-option
              v-for="item in statuses"
              :key="item.statusId"
              :label="item.name"
              :value="item.statusId"
              @change="getStatus(item.statusId)"
          />
        </el-select>
    </el-form-item>
    <!--        学工号-->
    <el-form-item label="学工号" prop="jobNum">
      <el-input v-model="form.jobNum" placeholder="请输入学工号" :disabled="ifDisableJobNum"></el-input>
    </el-form-item>
    <!--        姓名-->
    <el-form-item label="姓名" prop="username">
      <el-input v-model="form.username" placeholder="请输入姓名"></el-input>
    </el-form-item>
    <!--        身份证号-->
    <el-form-item label="身份证号" prop="idcardNum">
      <el-input v-model="form.idcardNum" placeholder="请输入身份证号"></el-input>
    </el-form-item>
    <!--        手机号-->
    <el-form-item label="手机号" prop="phoneNum">
      <el-input v-model.number="form.phoneNum" placeholder="请输入手机号(选填)"></el-input>
    </el-form-item>
    <!--        邮箱-->
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="form.email" placeholder="请输入邮箱(选填)"></el-input>
    </el-form-item>
    <!--          取消与提交按钮-->
    <el-button @click="cancelSubmit">取消</el-button>
    <el-button type="primary" @click="confirmSubmit">提交</el-button>
  </el-form>
</template>

<script>
import {ElMessage} from "element-plus";

export default {
  inject:['reload'],
  name: "addUserForm",
  props:{
    formData:Object,
    ifAddUser:Boolean,
    instituteAndMajor:Array,
    statuses:Array
  },
  computed:{
    ifDisableIdentity (){
      return this.ifAddUser !== true
    },
    ifDisableJobNum (){
      return this.ifAddUser !== true
    },
    form(){
      // console.log(this.formData)
      return this.formData
    },
    ifShowStatusSelect(){
      return this.ifAddUser !== true;
    },
    defaultInstituteAndMajor(){
      return [this.form.instituteId,this.form.majorId]
    },
    defaultStatus(){
      return this.form.statusId
    }
  },
  methods:{
    confirmSubmit(){
      // console.log(this.form)
      //校验表单是否符合规则
      this.$refs.addUserForm.validate(valid =>{
        //表单数据是符合规则的
        if(valid){
          //提交新用户
          if(this.ifAddUser===true){
            // console.log(this.form)
            // console.log(this.editMajorId)
            this.axios({
              method:"post",
              url: 'http://121.37.98.3:9090/users',
              headers:{
                'token':localStorage.getItem("token"),
                'Context-Type':'application/json'
              },
              data:{
                username: this.form.username,
                jobNum: this.form.jobNum,
                idcardNum: this.form.idcardNum,
                phoneNum: this.form.phoneNum.toString(),
                email: this.form.email,
                identity: this.form.identity,
                major: this.editMajorId,
                institute: this.editInstituteId
              },
            }).then(res =>{
              if(!res.data.success){
                ElMessage.error({
                  message: res.data.message,
                  grouping: true
                })
              }else{
                // 取消显示
                this.dialogAddFormVisible = false;
                ElMessage({
                  message: res.data.message,
                  type: 'success'
                })
                // 清空表单
                this.$refs["addUserForm"].resetFields();
                //刷新
                this.reload();
              }
            })
          }
          //修改用户信息
          else{
            console.log(this.form)
            console.log(this.editMajorId)
            console.log(this.editInstituteId)
            this.axios({
              method:"put",
              url: 'http://121.37.98.3:9090/users',
              headers:{
                'token':localStorage.getItem("token"),
                'Context-Type':'application/json'
              },
              data:{
                username: this.form.username,
                jobNum: this.form.jobNum,
                idcardNum: this.form.idcardNum,
                phoneNum: this.form.phoneNum.toString(),
                email: this.form.email,
                identity: this.form.identity,
                majorId:this.editMajorId===''?this.form.majorId:this.editMajorId,
                instituteId:this.editInstituteId===''?this.form.instituteId:this.editInstituteId,
                statusId:this.editStatusId===''?this.form.statusId:this.editStatusId
              },
            }).then(res =>{

              // console.log(res.data)
              if(!res.data.success){
                ElMessage.error({
                  message: res.data.message,
                  grouping: true
                })

              }else{
                // 取消显示
                this.dialogAddFormVisible = false;
                ElMessage({
                  message: res.data.message,
                  type: 'success'
                })
                // 清空表单
                this.$refs["addUserForm"].resetFields();
                //刷新
                this.reload();
              }
            })
          }
        }
        else{
          ElMessage.error({
            message: "表单数据有误,请检查!",
            grouping: true
          })
        }//end 表单数据不合格
      })
    },
    cancelSubmit(){
      // console.log(this.form.statusId)
      // 清空表单
      this.$refs["addUserForm"].resetFields();
      this.$emit('cancelSubmit')
      this.$forceUpdate();
    },
    getInstituteAndMajor(){
      const node =this.$refs['cascader'].getCheckedNodes()[0]
      this.editMajorId=node.data.value
      this.editInstituteId=node.parent.data.value
    },
    getStatus(statusId){
      this.editStatusId = statusId
      // console.log(this.editStatusId)
      // console.log(this.statuses)
    }
  },
  data(){
    const validatejobNum = (rule, value, callback) =>{

      if(!value){
        return callback(new Error("学工号不能为空"))
      }
      else if(isNaN(value)){
        return callback(new Error("请输入数字"))
      }//未选用户角色属性
      if (!this.form.identity){
        return callback(new Error("请先选择用户角色"))
      }//教师
      else if(this.form.identity==="教师"){
        const testValue1=new RegExp("^19[0-9]{6}$")
        const testValue2=new RegExp("^20[0-9]{6}$")
        const testValue3=new RegExp("^21[0-9]{6}$")
        const testValue4=new RegExp("^22[0-9]{6}$")
        if(!(testValue1.test(value)||testValue2.test(value)||testValue3.test(value)||testValue4.test(value))){
          return callback(new Error('请输入8位以19-22开头的学工号'))
        }
      }//学生
      else if(this.form.identity==="学生"){
        const testValue1=new RegExp("^19[0-9]{4}$")
        const testValue2=new RegExp("^20[0-9]{4}$")
        const testValue3=new RegExp("^21[0-9]{4}$")
        const testValue4=new RegExp("^22[0-9]{4}$")
        if(!(testValue1.test(value)||testValue2.test(value)||testValue3.test(value)||testValue4.test(value))){
          return callback(new Error('请输入6位以19-22开头的学工号'))
        }
      }
      return callback()
    }
    return {
      editInstituteId:'',
      editMajorId:'',
      editStatusId:'',
      verificationRules: {
        identity:[
          {required: true,message: '请选择身份',trigger: 'change'}
        ],
        jobNum: [
          {required: true,message: '请输入学工号',trigger: 'change'},
          {validator: validatejobNum, trigger:['change', 'blur']},
        ],
        username: [
          {required: true, message: '请输入姓名',trigger: 'change'},
          {pattern: /^[A-Za-z\u4e00-\u9fa5]+$/, message: '只允许中英文字符' }
        ],
        idcardNum: [
          {required: true, message: '请输入身份证号',trigger: 'change'},
          {len:18, message: '请输入18位身份证号',trigger: 'change'},
        ],
        phoneNum: [
          {pattern: /^1[0-9]{10}$/,message: '请以数字1开头书写11位手机号'},
        ],
        email: [
          {type:'email',message: '请输入正确格式email',trigger: 'change'},
        ],
        institute_major:[
          {required:true,message:"请选择院系与专业",trigger:'change',type:Array}
        ],
        status:[
          {required:true,message:"请选择用户状态",trigger:'change',type:Array}
        ]
      },
    }
  },
}
</script>

<style scoped>

</style>
