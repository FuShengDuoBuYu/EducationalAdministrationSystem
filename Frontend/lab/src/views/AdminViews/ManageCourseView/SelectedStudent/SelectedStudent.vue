<template>
  <el-descriptions
      :title="courseInfo.name+' 选课名单'"
      :column="4"
      direction="vertical"
      border
  >
    <el-descriptions-item label="课程代码">{{courseInfo.number}}</el-descriptions-item>
    <el-descriptions-item label="教师姓名">{{courseInfo.teacherName}}</el-descriptions-item>
    <el-descriptions-item label="学分">{{courseInfo.credit}}</el-descriptions-item>
    <el-descriptions-item label="开课学期" :span="2">{{ courseInfo.year+courseInfo.term }}</el-descriptions-item>
    <el-descriptions-item label="课程容量" :span="2">{{ courseInfo.capacity }}</el-descriptions-item>
    <el-descriptions-item label="选课总人数">{{selectedStudentList.length}}</el-descriptions-item>
    <el-descriptions-item label="开课院系">{{ courseInfo.institute+"  "+courseInfo.major}}</el-descriptions-item>
  </el-descriptions>

  <el-table :data="selectedStudentList" style="width:100%" border  height="70vh"
            :header-cell-style="{background:'#79bbff', color:'#fff'}"
  >
    <el-table-column prop="jobNum" label="学工号"  />
    <el-table-column prop="username" label="姓名"  />
    <el-table-column prop="idcardNum" label="身份证号" />
    <el-table-column prop="phoneNum" label="电话"  />
    <el-table-column prop="institute" label="学院" />
    <el-table-column prop="major" label="专业" />
    <el-table-column prop="email" label="邮箱" />
  </el-table>
</template>

<script>

export default {
  name: "SelectedStudent",
  data(){
    return{
      selectedStudentList:[],
      courseInfo:""
    }
  },
  created() {
    //获取关于这个课程的所有信息
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/courses/students/'+this.$route.query.courseId,
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      },
    }).then(res=>{
      console.log(res.data)
      this.selectedStudentList = res.data.data.students;
      this.courseInfo = res.data.data.course
    })
}
}

</script>

<style scoped>

</style>
