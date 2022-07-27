<template>
  <header-navigation/>
  <div style="display: flex" >
    <admin-side-navigation/>
    <el-scrollbar style="height: 100%;  width: 100%; margin: 1%">
      <institute-and-major-cards class="CardsStyle"/>
    </el-scrollbar>
    <el-button type="success" circle class="addIcon" @click="ifShowDrawer=true"><el-icon size="large"><Plus /></el-icon></el-button>
    <el-drawer ref="drawerRef" v-model="ifShowDrawer" title="添加学院" direction="rtl" custom-class="demo-drawer" :destroy-on-close="true">
      <el-input placeholder="请输入要添加的院系" v-model="instituteName"></el-input>
      <div style="margin-top:10% ">
        <el-button @click="ifShowDrawer=false">取消</el-button>
        <el-button type="primary" @click="addInstitute">确认</el-button>
      </div>
    </el-drawer>
  </div>

</template>

<script>
import AdminSideNavigation from "../AdminSideNavigation";
import HeaderNavigation from "../../../components/HeaderNavigation";
import InstituteAndMajorCards
  from "@/views/AdminViews/ManageInstituteAndMajorView/ChildComponents/InstituteAndMajorCards";
import {Plus} from "@element-plus/icons";
import {ElMessage} from "element-plus";

export default {
  inject:['reload'],
  name: "ManageInstituteAndMajorView",
  components: {InstituteAndMajorCards, HeaderNavigation, AdminSideNavigation,Plus},
  methods:{
    addInstitute(){
      this.axios({
        method:'POST',
        url:'http://121.37.98.3:9090/institutes',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          instituteName:this.instituteName
        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:'添加成功',
            duration:1500
          })
          this.ifShowDrawer=false
          this.reload()
        }
        else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          })
        }
      })
    }
  },
  data(){
    return{
      ifShowDrawer:false,
      instituteName:'',
    }
  }
}
</script>

<style scoped>
  .CardsStyle{
    width: 100%;
    background-color: white;
    padding: 2%;
    min-height: 70vh;
  }
  .addIcon{
    position:absolute;
    right:5%;
    bottom:15%;
    width: 50px;
    height: 50px;
  }
</style>
