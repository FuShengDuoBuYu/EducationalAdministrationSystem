import { createRouter, createWebHashHistory } from 'vue-router';
import store from '../assets/store'
import HomeView from '../views/HomeView.vue'
import IndexView from "../views/IndexView.vue"
import CourseInfoView from "@/views/CommonViews/CourseInfoView"
import ManageUserView from "@/views/AdminViews/ManageUserView/ManageUserView"
import AlterPasswordView from "@/views/CommonViews/AlterPasswordView"
import {onCheck} from '@/util/StatusCheck'
import {ElMessage} from "element-plus";
import UserInfoView from "@/views/CommonViews/UserInfoView";
import ManageInstituteAndMajorView from "../views/AdminViews/ManageInstituteAndMajorView/ManageInstituteAndMajorView";
import ManageSelectCourseView from "@/views/AdminViews/ManageSelectCourseView/ManageSelectCourseView";
import ManageScheduleView from "@/views/AdminViews/ManageScheduleView/ManageScheduleView";
import ManageCourseView from "@/views/AdminViews/ManageCourseView/ManageCourseView";
import SelectCourseView from "@/views/StudentsViews/SelectCourseView/SelectCourseView";
import TeachingCourseView from "@/views/TeacherViews/TeachingCourse/TeachingCourseView";
import SelectedStudent from "@/views/AdminViews/ManageCourseView/SelectedStudent/SelectedStudent";
import TakenCourses from "@/views/StudentsViews/TakenCourses/TakenCourses";
import ApplyForSelectingCourses from "@/views/StudentsViews/ApplyForSelectingCourses/ApplyForSelectingCourses";

const routes = [
  {
    //重定向
    path: '/',
    redirect: '/home'
  },
  {
    path: '/index',
    name: 'index',
    component: IndexView,
    meta:{
      title:'登录'
    }
  },
  {
    path: '/home',
    name: 'home',
    component: HomeView,
    meta:{
      title:'首页'
    }
  },
  {
    path: '/manageUser',
    name: 'manageUser',
    component: ManageUserView,
    meta:{
      title:'用户信息管理'
    }
  },
  {
    path: '/courseInfo',
    name: 'CourseInfo',
    component: CourseInfoView,
    meta:{
      title:'课程信息管理'
    }
  },
  {
    path: '/alterPassword',
    name: 'alterPassword',
    component: AlterPasswordView,
    meta:{
      title:'重置密码'
    }
  },
  {
    path: '/userInfo',
    name: 'userInfo',
    component: UserInfoView,
    meta:{
      title:'个人信息'
    }
  },
  {
    path: '/manageInstituteAndMajor',
    name: 'manageInstituteAndMajor',
    component: ManageInstituteAndMajorView,
    meta:{
      title:'院系专业管理'
    }
  },
  {
    path: '/manageSelectCourse',
    name: 'manageSelectCourse',
    component: ManageSelectCourseView,
    meta:{
      title:'选课管理'
    }
  },
  {
    path: '/manageSchedule',
    name: 'manageSchedule',
    component: ManageScheduleView,
    meta:{
      title:'教务安排管理'
    }
  },
  {
    path: '/manageCourse',
    name: 'manageCourse',
    component: ManageCourseView,
    meta:{
      title:'课程信息管理'
    }
  },
  {
    path:'/selectedStudent',
    name: 'selectedStudent',
    component:SelectedStudent,
    meta:{
      title:'已选学生查询'
    },
  },
  {
    path: '/selectCourse',
    name: 'selectCourse',
    component: SelectCourseView,
    meta:{
      title:'我的选课'
    }
  },
  {
    path: '/takenCourses',
    name: 'takenCourses',
    component: TakenCourses,
    meta:{
      title:'已修课程'
    }
  },
  {
    path: '/applyForSelectingCourses',
    name: 'applyForSelectingCourses',
    component: ApplyForSelectingCourses,
    meta:{
      title:'选课事务申请'
    }
  },
  {
    path: '/teachingCourse',
    name: 'teachingCourse',
    component: TeachingCourseView,
    meta:{
      title:'我的授课'
    }
  },
]

const router = createRouter({
  mode: 'hash',
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  //如果找不到这个页面或者长度为零,就放行,产生404
  if(!to.matched || to.matched.length===0){
    document.title = to.meta.title
    next(true);
  }
  //如果是进入index,放行
  if(to.name === 'index'){
    document.title = to.meta.title
    next(true);
  }
  //拦截修改密码
  if(store.state.passwordChange===false && to.name!== 'alterPassword'){
    document.title = to.meta.title
    next('alterPassword')
  }
  //修改过密码的不允许再进入修改密码
  if(to.name==='alterPassword'&&store.state.passwordChange===true){
    ElMessage.error("不允许在初始界面再次修改密码")
    document.title = to.meta.title
    next('home')
  }
  //否则进行token检验
  onCheck().then((res)=>{
    //校验登陆状态
    if(res.success){
      document.title = to.meta.title
      next(true)
    }
    //若校验密码,token等不成功
    else {
      //只有在未登录的情况下进入非404页面才弹窗
      if(to.name!=='index' && to.matched.length!==0)
          ElMessage.error(res.message)
      document.title = to.meta.title
      // document
      next('index')
    }
  })
})

export default router
