<template>
  <side-navigation :item-list="itemList"></side-navigation>
</template>

<script>
import SideNavigation from "../../components/SideNavigation";
import {Collection, Edit} from "@element-plus/icons";

export default {
  name: "StudentSideNavigation",
  components:{
    Collection,
    SideNavigation,
    Edit                //图标
  },

  data(){
    return{
      itemList:[
        {
          icon:Collection,
          text:"选课信息",
          index:'/selectCourse'
        },
        {
          icon:Collection,
          text:"已修课程",
          index:'/takenCourses'
        },
        {
          icon:Collection,
          text:"选课事务申请",
          index:'/applyForSelectingCourses'
        },
      ]
    }
  }
}
</script>

<style scoped>

</style>
