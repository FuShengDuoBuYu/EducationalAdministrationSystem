<template>
  <side-navigation :item-list="itemList"></side-navigation>
</template>

<script>
import SideNavigation from "../../components/SideNavigation";
import {Edit} from "@element-plus/icons";

export default {
  name: "TeacherSideNavigation",
  components:{
    SideNavigation,
    Edit                //图标
  },
  data(){
    return{
      itemList:[
        {
          icon:Edit,
          text:"我的授课",
          index:'/teachingCourse'
        },
      ]
    }
  }
}
</script>

<style scoped>

</style>
