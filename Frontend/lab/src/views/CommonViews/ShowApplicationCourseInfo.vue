<template>
  <el-table :data="applicationCourseInfoList" style="width:100%" border  height="70vh"
            :header-cell-style="{background:'#79bbff', color:'#fff'}"
            :row-class-name="tableRowClassName"
  >
    <el-table-column prop="applicationType" label="申请类别" />
    <el-table-column prop="name" label="课程名称" />
    <el-table-column prop="number" label="课程编号"  />
    <el-table-column prop="institute" label="开课院系"  />
    <el-table-column prop="major" label="专业" />
    <el-table-column prop="credit" label="学分" width="60%" />
    <el-table-column prop="classHour" label="学时" width="60%" />
    <el-table-column prop="capacity" label="容量" width="60%" />
    <el-table-column prop="description" label="课程描述" />
    <el-table-column prop="applicationResult" label="审核状态" />
    <el-table-column fixed="right" label="操作">
      <template #header>
        <el-input v-model="search" size="small" placeholder="搜索" />
      </template>
      <template #default="scope">
        <!--          查看更多信息-->
        <el-button circle size="small" type="info" @click="showApplicationCourseInfo(scope.$index)"><el-icon><More/></el-icon></el-button>
<!--        老师用来撤销-->
        <el-popconfirm v-if="!ifAdminOperating&&applicationCourseInfoList[scope.$index].applicationResult==='待审核'" title="是否确认撤销该课程申请?" @confirm="deleteClick(scope.$index)">
          <template #reference>
            <el-button type="text" size="large">撤销</el-button>
          </template>
<!--          管理员来同意-->
        </el-popconfirm>
        <el-popconfirm v-if="ifAdminOperating&&applicationCourseInfoList[scope.$index].applicationResult==='待审核'" title="是否确认通过该课程申请?" @confirm="approveClick(scope.$index)">
          <template #reference>
            <el-button circle size="small" type="success"><el-icon><Check/></el-icon></el-button>
          </template>
        </el-popconfirm>
<!--        管理员来拒绝-->
        <el-popconfirm v-if="ifAdminOperating&&applicationCourseInfoList[scope.$index].applicationResult==='待审核'" title="是否确认拒绝该课程申请?" @confirm="refuseClick(scope.$index)">
          <template #reference>
            <el-button circle size="small" type="danger"><el-icon><Close/></el-icon></el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>


<!--  审核课程的dialog-->
  <el-dialog width="80%" v-model="ifShowApplicationCourseInfoDialog" :destroy-on-close="true">
    <show-course-info-detail-view v-bind:course-info="applicationCourseInfo"/>
  </el-dialog>

</template>

<script>
import {ElMessage} from "element-plus";
import {Check, Close, More} from "@element-plus/icons";
import AddCourseForm from "@/views/CommonViews/addCourseForm";
import ShowCourseInfoDetailView from "@/views/CommonViews/ShowCourseInfoDetailView";
export default {
  inject:['reload'],
  name: "ShowApplicationCourseInfo",
  components:{
    ShowCourseInfoDetailView,
    AddCourseForm,
    More,
    Check,Close
  },
  props:{
    applicationCourseInfoList:{
      type:Array,
      default(){
        return [];
      }
    },
    ifAdminOperating:Boolean
  },
  data(){
    return{
      applicationCourseInfo:'',
      ifShowApplicationCourseInfoDialog:false
    }
  },
  methods:{
    //根据用户状态显示背景色
    tableRowClassName({row,rowIndex}){
      if(row.applicationResult==='待审核'){
        return 'pendingApproval'
      }
      if(row.applicationResult==='已通过'){
        return 'approved'
      }
      if(row.applicationResult==='已拒绝'){
        return 'refused'
      }
    },
    //展示具体信息
    showApplicationCourseInfo(applicationCoursesIndex){
      this.applicationCourseInfo =  this.applicationCourseInfoList[applicationCoursesIndex]
      this.ifShowApplicationCourseInfoDialog = true
    },
    //老师撤销申请
    deleteClick(){
      ElMessage.info({
        message:'待开发',
        duration:1500
      })
    },
    //管理员同意申请
    approveClick(applicationCoursesIndex){
      // ElMessage.info({
      //   message:'待开发',
      //   duration:1500
      // })
      this.axios({
        method:'PUT',
        url:'http://121.37.98.3:9090/courses/applications/accept',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          applicationId:this.applicationCourseInfoList[applicationCoursesIndex].applicationId
        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:'已同意',
            duration:1500
          })
          this.reload()
        }
        else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          })
        }
      })
    },
    //管理员拒绝申请
    refuseClick(applicationCoursesIndex){
      this.axios({
        method:'PUT',
        url:'http://121.37.98.3:9090/courses/applications/refuse',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          applicationId:this.applicationCourseInfoList[applicationCoursesIndex].applicationId
        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:'已拒绝',
            duration:1500
          })
          this.reload()
        }
        else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          })
        }
      })
    },
  }
}
</script>

<style scoped>
/deep/ .pendingApproval{
  background: #fdf6ec;
}
/deep/ .approved{
  background: #f0f9eb;
}
/deep/ .refused{
  background: #fef0f0;
}
</style>
