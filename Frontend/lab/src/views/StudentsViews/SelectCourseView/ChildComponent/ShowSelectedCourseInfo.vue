<template>
  <el-table :data="selectedCourseInfoList" border :header-cell-style="{background:'#79bbff', color:'#fff'}">
    <el-table-column prop="name" label="课程名称" />
    <el-table-column prop="number" label="课程编号"  />
    <el-table-column prop="teacherName" label="授课老师"  />
    <el-table-column prop="availableType" label="课程类型"  />
    <el-table-column prop="major" label="开课专业" />
    <el-table-column prop="credit" label="学分" width="60%" />
    <el-table-column prop="classHour" label="学时" width="60%" />
    <el-table-column prop="selectedStudents" label="已选" width="60%"/>
    <el-table-column prop="capacity" label="容量" width="60%" />
    <el-table-column fixed="right" label="操作">
      <template #header>
        <el-input v-model="search" size="small" placeholder="搜索" />
      </template>
      <template #default="scope">
        <!--          查看更多信息-->
        <el-button circle size="small" type="info" @click="showSelectedCourseDetailInfo(scope.$index)"><el-icon><More/></el-icon></el-button>
        <!--        选课-->
        <el-popconfirm title="是否确认退课?" @confirm="deleteCourse(scope.$index)">
          <template #reference>
            <el-button circle type="danger" size="small"><el-icon><Delete/></el-icon></el-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>


  <!--  展示选课详细信息-->
  <el-dialog width="80%" v-model="ifShowSelectCourseDetail" :destroy-on-close="true">
    <show-course-info-detail-view v-bind:course-info="selectedCourseInfo" v-bind:if-select-course="true"/>
  </el-dialog>
</template>

<script>
import {Delete,More} from "@element-plus/icons";
import {ElMessage} from "element-plus";
import ShowCourseInfoDetailView from "@/views/CommonViews/ShowCourseInfoDetailView";
import StudentSelectedCourseBoard
  from "@/views/StudentsViews/SelectCourseView/ChildComponent/StudentSelectedCourseBoard";

export default {
  inject:['reload'],
  name: "ShowSelectedCourseInfo",
  props:{
    ifStartSelectCourse:Boolean
  },
  data(){
    return{
      selectedCourseInfo:{},
      ifShowSelectCourseDetail:false,
      selectedCourseInfoList:[]
    }
  },
  components:{
    StudentSelectedCourseBoard,
    ShowCourseInfoDetailView,
    Delete,More
  },
  methods:{
    //退课
    deleteCourse(index){
      this.axios({
        method: "delete",
        url: 'http://121.37.98.3:9090/courses/student/selected',
        headers: {
          'token': localStorage.getItem("token"),
          'Context-Type': 'application/json'
        },
        data: {
          studentId : this.$store.state.uuid,
          courseId : this.selectedCourseInfoList[index].courseId
        },
      }).then(res => {
        if (res.data.success) {
          ElMessage.success({
            message: '退课成功',
          });
          this.reload()
        } else {
          ElMessage.error({
            message: res.data.message,
          })
        }
      })
    },
    //展示所选课程详细信息
    showSelectedCourseDetailInfo(index){
      this.selectedCourseInfo = this.selectedCourseInfoList[index]
      this.ifShowSelectCourseDetail = true
    }
  },
  created() {
    //如果是开始选课了,就获取已选信息
    if(this.ifStartSelectCourse){
      this.axios({
        method:'GET',
        url:'http://121.37.98.3:9090/courses/student/selected/'+this.$store.state.uuid,
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
      }).then(res=>{
        console.log(res.data)
        this.selectedCourseInfoList = res.data.data
      })
    }
  }
}
</script>

<style scoped>

</style>

