<template>
  <header-navigation/>
  <div style="display: flex;">
    <student-side-navigation/>
    <el-empty style="width: 100%" v-if="!ifStartSelectCourse" description="未开始选课" />
    <el-scrollbar style="width: 100%" height="80vh" v-if="ifStartSelectCourse">
      <el-row v-if="ifStartSelectCourse">
        <el-divider content-position="left" style="width: 80%">{{term+' 课表状态预览'}}</el-divider>
        <el-switch v-model="ifShowSelectedCourseBoard" style="margin: 1%;width: 15%" active-text="已预览" inactive-text="未预览"></el-switch>
      </el-row>
<!--      已选课表-->
      <student-selected-course-board style="margin-top: 1%" v-if="ifShowSelectedCourseBoard"/>
      <el-row>
        <el-divider content-position="left" style="width: 80%">{{'第'+selectCourseRound+'轮选课 可选课程查看'}}</el-divider>
        <el-switch v-model="ifShowPendingSelectCourses" style="margin: 1%;width: 15%" active-text="已查看" inactive-text="未查看"></el-switch>
      </el-row>
      <el-tabs v-model="activeTabName" v-if="ifShowPendingSelectCourses">
        <el-tab-pane label="可选课程" name="pendingSelectCourses">
          <show-select-course-info
            v-if="ifShowPendingSelectCourses"
            v-bind:if-start-select-course="ifStartSelectCourse"
            v-bind:selection-round="selectCourseRound"
          />
        </el-tab-pane>
        <el-tab-pane label="已选课程" name="selectedCourses">
          <show-selected-course-info
              v-if="ifShowPendingSelectCourses"
              v-bind:if-start-select-course="ifStartSelectCourse"
          />
        </el-tab-pane>
      </el-tabs>
    </el-scrollbar>
  </div>

</template>

<script>
import HeaderNavigation from "@/components/HeaderNavigation";
import StudentSideNavigation from "@/views/StudentsViews/StudentSideNavigation";
import CourseInfoView from "@/views/CommonViews/CourseInfoView";
import ShowApplicationCourseInfo from "@/views/CommonViews/ShowApplicationCourseInfo";
import ShowCourseInfo from "@/views/AdminViews/ManageCourseView/ChildComponent/ShowCourseInfo";
import ShowSelectCourseInfo from "@/views/StudentsViews/SelectCourseView/ChildComponent/ShowSelectCourseInfo";
import {ElLoading} from "element-plus";
import StudentSelectedCourseBoard
  from "@/views/StudentsViews/SelectCourseView/ChildComponent/StudentSelectedCourseBoard";
import ShowSelectedCourseInfo from "@/views/StudentsViews/SelectCourseView/ChildComponent/ShowSelectedCourseInfo";
export default {
  name: "SelectCourseView",
  components: {
    ShowSelectedCourseInfo,
    StudentSelectedCourseBoard,
    ShowSelectCourseInfo,
    ShowApplicationCourseInfo, ShowCourseInfo, CourseInfoView, StudentSideNavigation, HeaderNavigation},
  computed:{
  },
  data(){
    return{
      ifStartSelectCourse:true,
      pendingSelectCourse:[],
      ifShowSelectedCourseBoard:true,
      selectCourseRound:'',
      term:'',
      ifShowPendingSelectCourses:true,
      activeTabName:'pendingSelectCourses'
    }
  },
  created() {
    ElLoading.service();
    //查看是否开启了选课
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/admin/course',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).
    then(res=>{
      console.log(res.data.data)
      this.ifStartSelectCourse = res.data.data.isCourseSelection
      this.selectCourseRound = res.data.data.courseSelectionRound
    });
    //获取当前学期
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/term',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).
    then(res=>{
      this.term = res.data.data.year+res.data.data.term
    });
  }
}
</script>

<style scoped>

</style>
