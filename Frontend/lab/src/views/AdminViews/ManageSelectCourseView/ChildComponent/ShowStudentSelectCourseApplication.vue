<template>
  <span>{{'选课事务申请'}}</span>
  <el-table  :data="applicationCourseInfoList" style="width:100%" border  height="70vh"
            :header-cell-style="{background:'#79bbff', color:'#fff'}"
            @row-dblclick="getStudents">
    <!--    课程名称-->
      <el-table-column prop="course.name" label="课程名称"  />
    <!--    学年-->
      <el-table-column prop="course.year" label="开课学年"  />
    <!--    学期-->
      <el-table-column prop="course.term" label="开课学期"  />
    <!--    编号-->
      <el-table-column prop="course.number" label="课程编号"  />
    <!--    教师-->
      <el-table-column prop="course.teacherName" label="任课教师"/>
    <!--    教室地点-->
      <el-table-column prop="student.jobNum" label="学生学号"/>
    <!--    上课时间-->
      <el-table-column prop="student.username" label="学生姓名" />
      <el-table-column prop="reason" label="申请理由" />
      <el-table-column prop="applicationResult" label="申请状态" />
      <el-table-column prop="selectedStudents" label="操作">
        <template #default="scope">
          <span v-if="!(applicationCourseInfoList[scope.$index].applicationResult==='待审核')">已操作</span>
          <el-popconfirm v-if="applicationCourseInfoList[scope.$index].applicationResult==='待审核'" title="是否确认通过该课程申请?" @confirm="approveClick(scope.$index)">
            <template #reference>
              <el-button circle size="small" type="success"><el-icon><Check/></el-icon></el-button>
            </template>
          </el-popconfirm>
          <!--        管理员来拒绝-->
          <el-popconfirm v-if="applicationCourseInfoList[scope.$index].applicationResult==='待审核'" title="是否确认拒绝该课程申请?" @confirm="refuseClick(scope.$index)">
            <template #reference>
              <el-button circle size="small" type="danger"><el-icon><Close/></el-icon></el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
  </el-table>
</template>

<script>
import {Finished,Check,Close} from "@element-plus/icons";
import ShowApplyForSeclectCourses from "../../../StudentsViews/ApplyForSelectingCourses/ShowApplyForSeclectCourses";
import {ElMessage} from "element-plus";
export default {
  inject :['reload'],
  name: "ShowStudentSelectCourseApplication",
  data(){
    return{
      applicationCourseInfoList:[],
    }
  },
  components:{Finished,Check,Close},
  created() {
    this.axios({
      method:"GET",
      url: 'http://121.37.98.3:9090/courses/student/applications',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      },
    }).then(res=>{
      this.applicationCourseInfoList.push(...res.data.data)
      console.log('lalalalala');
      console.log(res);
      console.log(this.applicationCourseInfoList);
    })
  },
  methods:{
    approveClick(index){
      console.log(this.applicationCourseInfoList[index].applicationId);
      this.axios({
        method:"PUT",
        url: 'http://121.37.98.3:9090/courses/student/applications/accept',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          'applicationId':this.applicationCourseInfoList[index].applicationId,
        }
      }).then(res=>{
        if(res.data.success){
          this.reload();
        }else{
          ElMessage.error({
            message: res.data.message,
          })
        }
      })

    },
    refuseClick(index){
      console.log(this.applicationCourseInfoList[index].applicationId);
      this.axios({
        method:"PUT",
        url: 'http://121.37.98.3:9090/courses/student/applications/refuse',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          'applicationId':this.applicationCourseInfoList[index].applicationId,
        }
      }).then(res=>{
        if(res.data.success){
          this.reload();
        }else{
          ElMessage.error({
            message: res.data.message,
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
