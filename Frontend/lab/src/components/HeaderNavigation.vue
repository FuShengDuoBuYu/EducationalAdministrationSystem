<template>
<el-menu style="min-height: 10vh" mode="horizontal" >
  <el-image style="width: 20%; height: 12vh;padding: 5px"  :src="require(`@/assets/img/title.jpeg`)"  />
  <h2 style="text-align-all: center;width: 35%;margin-left: 4%;text-align: left;font-family: '微软雅黑 Light'">欢迎登录教务系统</h2>
  <h4 style="width: 35%;margin-left: 14%;text-align: right;font-family: '微软雅黑 Light'">[{{username}}({{jobNum}})]</h4>

  <el-sub-menu>
    <template #title>
      <el-icon style="width: 30px"><UserFilled /></el-icon>个人中心</template>
    <el-menu-item index="personal_info" @click="navigateToUserInfoView">我的信息</el-menu-item>
    <el-menu-item index="logout" @click="homeLogout">退出登录</el-menu-item>
  </el-sub-menu>
</el-menu>

</template>

<script>
import {UserFilled} from "@element-plus/icons";
import {logout} from '@/util/StatusCheck'
export default {
  name: "HeaderNavigation",
  computed: {
    jobNum(){
      return this.$store.state.jobNum
    },
    username(){
      return this.$store.state.username
    }
  },
  components:{
    UserFilled
  },
  methods: {
    homeLogout(){
      logout('已登出！')
    },
    navigateToUserInfoView(){
      console.log(this.$store.state)
      this.$router.push('/userInfo')
    }
  },
}
</script>

<style scoped>

</style>
