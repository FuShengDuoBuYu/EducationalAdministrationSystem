<template>
  <header-navigation/>
  <div style="display: flex;">
    <student-side-navigation/>
    <show-apply-for-seclect-courses/>
  </div>
</template>

<script>
import HeaderNavigation from "@/components/HeaderNavigation";
import StudentSideNavigation from "@/views/StudentsViews/StudentSideNavigation";
import ShowApplyForSeclectCourses from "./ShowApplyForSeclectCourses";
export default {
  name: "ApplyForSelectingCourses",
  components: {StudentSideNavigation, HeaderNavigation,ShowApplyForSeclectCourses}
}
</script>

<style scoped>

</style>
