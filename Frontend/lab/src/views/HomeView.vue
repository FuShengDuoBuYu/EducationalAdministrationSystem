<template>
  <HeaderNavigation/>
  <div style="display: flex;">
    <admin-side-navigation v-if="identity==='管理员'" />
    <student-side-navigation v-else-if="identity==='学生'" />
    <teacher-side-navigation v-else-if="identity==='教师'" />
  </div>
</template>

<script>

import HeaderNavigation from "@/components/HeaderNavigation";
import SideNavigation from "@/components/SideNavigation";
import {Edit} from "@element-plus/icons";
import AdminSideNavigation from "./AdminViews/AdminSideNavigation";
import StudentSideNavigation from "@/views/StudentsViews/StudentSideNavigation";
import TeacherSideNavigation from "@/views/TeacherViews/TeacherSideNavigation";
export default {
  name: 'HomeView',
  components: {
    TeacherSideNavigation,
    StudentSideNavigation,
    AdminSideNavigation,
    HeaderNavigation,
    SideNavigation,Edit
  },
  computed:{
    identity(){
      return this.$store.state.identity
    }
  },
  data(){
    return {
      itemList:[
        {
          icon:Edit,
          text:"学生"
        },
        {
          icon:Edit,
          text:"学生"
        },
        {
          icon:Edit,
          text:"学生"
        },
        {
          icon:Edit,
          text:"学生"
        },

      ]
    }
  },
  methods: {
  },

}

</script>
