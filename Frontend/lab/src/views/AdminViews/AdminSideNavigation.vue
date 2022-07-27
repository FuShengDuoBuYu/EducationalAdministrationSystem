<template>
  <side-navigation :item-list="itemList"></side-navigation>
</template>

<script>
import SideNavigation from "../../components/SideNavigation";
import {Edit, User, Collection, Calendar, Management, SetUp} from "@element-plus/icons";

export default {
  name: "AdminSideNavigation",
  components:{
    SideNavigation,
    Edit,
    Calendar,
    Management,
    User//图标
  },

  data(){
    return{
      itemList:[
        {
          icon:User,
          text:"用户信息管理",
          index:'/manageUser'
        },
        {
          icon:Collection,
          text:"学院/专业信息管理",
          index:'/manageInstituteAndMajor'
        },
        {
          icon:Calendar,
          text:"课程信息管理",
          index:'/manageCourse'
        },
        {
          icon:SetUp,
          text:"选课管理",
          index:'/manageSelectCourse'
        },
        {
          icon:Management,
          text:"教务安排管理",
          index:'/manageSchedule'
        }
      ]
    }
  }


}
</script>

<style scoped>

</style>
