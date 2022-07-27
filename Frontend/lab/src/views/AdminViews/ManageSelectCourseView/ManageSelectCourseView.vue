<template>
  <!--  顶部导航栏-->
  <HeaderNavigation/>
  <div style="display: flex">
    <!--    侧边栏-->
    <admin-side-navigation/>
    <el-tabs v-model="activeTabName" type="card" style="width: 100%">
      <el-tab-pane label="选课信息配置" name="selectCourseInfo">
        <div>
          <div>
            <span style="margin-right: 10%">选课开关按钮</span>
            <el-switch
                v-model="ifStartSelectCourse"
                :loading="ifLoading"
                active-color="#13ce66"
                inactive-color="#ff4949"
                active-text="已开始选课"
                inactive-text="未开始选课"
                @change="selectCourseStatusChange"
            />
          </div>
          <el-divider></el-divider>
          <span style="margin-right: 10%" v-if="ifStartSelectCourse">当前选课轮次选择</span>
          <el-select v-model="selectFound" placeholder="选择选课轮次" v-if="ifStartSelectCourse" @change="changeSelectCourseRound">
            <el-option
                v-for="item in courseSelectFound"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </div>
      </el-tab-pane>
      <el-tab-pane label="当前学期课程" name="currentTermCourses">
        <show-current-term-courses-info />
      </el-tab-pane>
      <el-tab-pane label="选课申请审核" name="pendingApprovalApplyForSelectingCourseInfo">
<!--        todo:显示学生事务申请-->
      <show-student-select-course-application></show-student-select-course-application>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import AdminSideNavigation from "@/views/AdminViews/AdminSideNavigation";
import HeaderNavigation from "@/components/HeaderNavigation";
import ShowStudentSelectCourseApplication from "./ChildComponent/ShowStudentSelectCourseApplication";
import {ElLoading, ElMessage, ElMessageBox} from "element-plus";
import {toRaw} from "vue";
import ShowCurrentTermCoursesInfo
  from "@/views/AdminViews/ManageSelectCourseView/ChildComponent/ShowCurrentTermCoursesInfo";
export default {
  name: "ManageSelectCourseView",
  components: {ShowCurrentTermCoursesInfo, AdminSideNavigation, HeaderNavigation,ShowStudentSelectCourseApplication},
  data(){
    return{
      activeTabName:"selectCourseInfo",
      selectFound:'1',
      ifStartSelectCourse: '',
      ifLoading : false,
      courseSelectFound:[
        {label:'第一轮选课',value:'1'},
        {label:'第二轮选课',value:'2'}
      ],
      courseInfoList:[]
    }
  },
  created() {
    ElLoading.service();
    //获取当前是开始选课还是未开始
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/admin/course',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      const loading = ElLoading.service();
      loading.close();
      this.ifStartSelectCourse = res.data.data.isCourseSelection
      this.selectFound = res.data.data.courseSelectionRound
    });
  },
  methods:{
    //把时间进行展示
    getTimeString(nums){
      let stringDetail='';
      let start=nums[0];
      let end=nums[0];
      for(let i=1;i<nums.length;i++){
        if(end!==nums[i]-1){
          if(start===end){
            stringDetail+=start;
          }else{
            stringDetail+=start+"-"+end;
          }
          start=nums[i]
        }
        end=nums[i];
      }
      if(start===end){
        stringDetail+=start;
      }else{
        stringDetail+=start+"-"+end;
      }
      return stringDetail;
    },
    //节次时间
    courseTime(courseInfo){
      let courseTimeString=''
      let existDays=[];
      courseInfo.timetableList.forEach(value =>{
        if(!existDays.includes(value.day)){
          existDays.push(value.day);
        }
      });
      existDays.sort(function(a, b){return a-b})
      existDays.forEach(value => {
        for(let i=0;i<courseInfo.timetableList.length;i++){
          if(courseInfo.timetableList[i].day===value){
            switch (value){
              case 1:
                courseTimeString+="周一 ";
                break;
              case 2:
                courseTimeString+="周二 ";
                break;
              case 3:
                courseTimeString+="周三 ";
                break;
              case 4:
                courseTimeString+="周四 ";
                break;
              case 5:
                courseTimeString+="周五 ";
                break;
              case 6:
                courseTimeString+="周六 ";
                break;
              case 7:
                courseTimeString+="周日";
                break;
            }
            courseInfo.timetableList[i].sessions.sort(function(a, b){return a-b})
            courseTimeString+=this.getTimeString(courseInfo.timetableList[i].sessions);
            courseTimeString+=' ';
            break;
          }
        }
      })
      return courseTimeString;
    },
    //获取当前选课状态
    selectCourseStatusChange(){
      this.ifLoading=true
      //给后台传过去
      this.axios({
        method:'PUT',
        url:'http://121.37.98.3:9090/admin/course',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          adminId: this.$store.state.uuid,
          courseSelection: this.ifStartSelectCourse,

        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:'修改成功',
            duration:'1500'
          })
        }else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          })
        }
        this.ifLoading = false
      });
    },
    //修改选课轮次
    changeSelectCourseRound(val){
      ElMessageBox.confirm('继续操作将修改选课轮次!是否继续?', '修改轮次', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
      )
      .then(() => {
        ElLoading.service();
        //上传修改
        this.axios({
          method:"put",
          url: 'http://121.37.98.3:9090/admin/course/round',
          headers:{
            'token':localStorage.getItem("token"),
            'Context-Type':'application/json'
          },
          data:{
            adminId: this.$store.state.uuid,
            courseSelectionRound : this.selectFound
          },
        }).then(res=>{
          const loading = ElLoading.service();
          loading.close();
          if(res.data.success){
            ElMessage.success({
              message: '修改轮次成功',
              grouping: true
            });
          }else{
            ElMessage.error({
              message: res.data.message,
              grouping: true
            })
          }
        })
      })
    },

  }
}
</script>

<style scoped>

</style>
