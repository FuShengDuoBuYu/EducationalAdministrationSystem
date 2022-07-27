<template>
  <div>
    <HeaderNavigation/>
  </div>
  <div style="display: flex; padding: 1vh">
    <SideNavigation/>
  </div>
</template>


<script>
import HeaderNavigation from "@/components/HeaderNavigation";
import SideNavigation from "@/components/SideNavigation";
export default {
  name: "CourseInfoView",
  components: {
    HeaderNavigation,
    SideNavigation
  }
}
</script>

<style scoped>

</style>