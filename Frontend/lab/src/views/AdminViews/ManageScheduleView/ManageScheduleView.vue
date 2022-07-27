<template>
  <HeaderNavigation/>
  <div style="display: flex">
    <!--    侧边栏-->
    <admin-side-navigation/>
    <el-tabs v-model="activeTabName" type="border-card" style="width: 100%;background-color: white">
      <el-tab-pane label="课程时间管理" name="manageClassSchedule">
        <div style="text-align: right">
          <el-button type="danger" @click="deleteAndRefresh">恢复初始</el-button>
          <el-button type="primary" @click="saveChange">确定保存</el-button>
        </div>
        <el-scrollbar height="60vh">
          <el-divider content-position="left">课程设置</el-divider>
            <span>课程节数:</span>
            <el-input-number v-model="classNum" :min="1" :max="20" @change="changeClassNum" />
          <div style="margin-top: 2%" v-for="(item,index) in classNum">
            <span style="margin-right: 10%">第{{index+1}}节课:</span>
            <el-time-select v-model="classInfo[index].startTime"
                            placeholder="上课时间"
                            :start="index===0?'08:00':classInfo[index-1].endTime"
                            step="00:05"
                            :end="findStartTime(index)"
            />
            <el-time-select v-model="classInfo[index].endTime"
                            placeholder="下课时间"
                            :start="classInfo[index].startTime"
                            step="00:05"
                            :end="findEndTime(index)"/>
          </div>
          <div style="display: flex">
            <el-divider content-position="left" style="width: 80%">课表状态预览</el-divider>
            <el-switch v-model="ifShowPreview" style="margin: 1%;width: 15%" active-text="已预览" inactive-text="未预览"></el-switch>
          </div>
          <class-schedule-table v-bind:class-num="classNum" v-bind:class-info="classInfo" v-if="ifShowPreview"></class-schedule-table>
        </el-scrollbar>
      </el-tab-pane>
      <el-tab-pane label="教室信息管理" name="manageClassRoom">
        <div style="display: flex">
          <teachering-building-and-classroom-cards style="width: 80%"></teachering-building-and-classroom-cards>
          <el-button type="success" circle class="addIcon" @click="ifShowDrawer=true"><el-icon size="large"><Plus /></el-icon></el-button>
          <el-drawer ref="drawerRef" v-model="ifShowDrawer" title="添加教学楼" direction="rtl" custom-class="demo-drawer" :destroy-on-close="true">
            <el-input placeholder="请输入要添加的教学楼名称" v-model="buildingName"></el-input>
            <div style="margin-top:10% ">
              <el-button @click="ifShowDrawer=false">取消</el-button>
              <el-button type="primary" @click="addTeachingBuilding">确认</el-button>
            </div>
          </el-drawer>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import ClassScheduleTable from "@/views/AdminViews/ManageScheduleView/ChildComponent/ClassScheduleTable";
import HeaderNavigation from "@/components/HeaderNavigation";
import AdminSideNavigation from "@/views/AdminViews/AdminSideNavigation";
import { ElLoading, ElMessage} from "element-plus";
import {Plus} from "@element-plus/icons";
import TeacheringBuildingAndClassroomCards
  from "@/views/AdminViews/ManageScheduleView/ChildComponent/TeacheringBuildingAndClassroomCards";
export default {
  inject:['reload'],
  name: "ManageScheduleView",
  components: {TeacheringBuildingAndClassroomCards,
    ClassScheduleTable, AdminSideNavigation, HeaderNavigation,Plus},
  computed:{
  },
  data(){
    return{
      //展示课程当前预览
      ifShowPreview:false,
      classNum:'',
      activeTabName:'manageClassSchedule',
      classInfo:[{
        session:'',
        startTime:'',
        endTime:''
      }],
      ifShowDrawer:false,
      buildingName:''
    }
  },
  methods:{
    //修改课程节数
    changeClassNum(){
      //如果大于,添加
      if(this.classNum > this.classInfo.length){
        for (let i = 0; i < (this.classNum-this.classInfo.length); i++) {
          this.classInfo.push(new Object({'session':this.classInfo.length+1,'startTime':'','endTime':''}))
        }
      }
      //如果小于,裁切
      else{
        this.classInfo = this.classInfo.slice(0,this.classNum)
      }
    },
    //限制每节课的上课时间
    findStartTime(index){
      //前面无数据
      if(this.classInfo[index].endTime===''){
        return "24:00";
      }
      else{
        return this.classInfo[index].endTime
      }
    },
    //限制每节课的下课时间
    findEndTime(index){
      //后面有数据
      if(this.classInfo.length>(index+1)){
        //已经赋值
        if(this.classInfo[index+1].startTime===''){
          return "24:00";
        }
        return this.classInfo[index+1].startTime;
      }
      else{
        return '24:00'
      }
    },
    deleteAndRefresh(){
      this.reload()
    },
    saveChange(){
      //修改时间表
      this.axios({
        method:'POST',
        url:'http://121.37.98.3:9090/schedules',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          scheduleList: this.classInfo
        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:'已修改',
            duration:1500
          });
        }
        else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          })
        }
      })
    },
    addTeachingBuilding(){
      this.axios({
        method:'POST',
        url:'http://121.37.98.3:9090/buildings',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          buildingName:this.buildingName
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
  created() {
    ElLoading.service();
    var _this = this;
    //获取原本的课程安排信息
    this.axios({
      method:"GET",
      url: 'http://121.37.98.3:9090/schedules',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      },
    }).then(res=>{
      // console.log(res.data.data)
      if(res.data.data.length!==0){
        _this.classNum = res.data.data.length
        _this.classInfo = res.data.data
      }
      const loading = ElLoading.service();
      loading.close()
    })
  }
}
</script>

<style scoped>
  .addIcon{
    position:absolute;
    right:2%;
    bottom:15%;
    width: 50px;
    height: 50px;
  }
</style>
