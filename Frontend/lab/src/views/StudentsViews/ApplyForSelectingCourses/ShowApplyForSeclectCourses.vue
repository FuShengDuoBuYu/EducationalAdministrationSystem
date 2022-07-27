<template>
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
  </el-table>
</template>

<script>
import {Finished,Check,Close} from "@element-plus/icons";

export default {
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
      url: 'http://121.37.98.3:9090/courses/student/applications/'+this.$store.state.uuid,
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      },
    }).then(res=>{
      this.applicationCourseInfoList.push(...res.data.data);
    })

  },
}
</script>

<style scoped>

</style>