<template>
  <header-navigation/>
  <div style="display: flex" >
    <teacher-side-navigation/>
    <el-tabs v-model="activeTabName" type="border-card" style="width: 100%;background-color: white">
      <el-tab-pane label="我的开课" name="myTeachingCourse">
        <el-scrollbar height="60vh">
          <show-course-info v-bind:course-info-list="teachingCourses" @delete-click="applyForDeleteCourse" @edit-click="applyForEditCourse">
          </show-course-info>
        </el-scrollbar>
      </el-tab-pane>
      <el-tab-pane label="我的申请" name="myApplication">
        <div style="text-align: right">
          <el-button type="primary" @click="applyForCourse">申请开课</el-button>
        </div>
        <show-application-course-info v-bind:application-course-info-list="applicationCourseInfoList">
        </show-application-course-info>
      </el-tab-pane>
    </el-tabs>
  </div>

  <el-dialog width="80%" v-model="ifShowDialog" :destroy-on-close="true" :close-on-press-escape="false" :close-on-click-modal="false" :show-close="false">
    <add-course-form
        v-bind:if-admin-operating="false"
        v-bind:course_info_form_from_parent="courseInfoForm"
        @cancel-submit="ifShowDialog=false"
        @user-submit="handleUserSubmit"
        key="applyForEditCourse">
    </add-course-form>
  </el-dialog>

</template>

<script>
import HeaderNavigation from "@/components/HeaderNavigation";
import TeacherSideNavigation from "@/views/TeacherViews/TeacherSideNavigation";
import AddCourseForm from "@/views/CommonViews/addCourseForm";
import {ElLoading, ElMessage} from "element-plus";
import ShowApplicationCourseInfo from "@/views/CommonViews/ShowApplicationCourseInfo";
import ShowCourseInfo from "@/views/AdminViews/ManageCourseView/ChildComponent/ShowCourseInfo";
export default {
  inject:['reload'],
  name: "TeachingCourseView",
  components: {ShowCourseInfo, ShowApplicationCourseInfo, AddCourseForm, TeacherSideNavigation, HeaderNavigation},
  data(){
    return{
      activeTabName:'myTeachingCourse',
      ifShowDialog:false,
      instituteAndMajor:[],
      courseFormOperationType:[],
      applicationCourseInfoList: [],
      applicationTypeId:'',
      teachingCourses:[],
      courseInfoForm:{
        capacity: '',
        classHour: '',
        courseId: "",
        credit: '',
        description: "",
        identifier: "",
        institute: this.$store.state.institute,
        instituteId: this.$store.state.instituteId,
        major: this.$store.state.major,
        majorId: this.$store.state.majorId,
        name: "",
        number: "",
        teacherName: this.$store.state.username,
        applicationTypeId: '',
        teacherId: '',
        timetableList:[]
      },
    }
  },
  methods:{
    //申请新增课程
    applyForCourse(){
      this.ifShowDialog = true
      //找到新增的id
      this.courseFormOperationType.map(item=>{
        if(item.name==="新增"){
          this.applicationTypeId = item.applicationTypeId
        }
      })
      this.courseInfoForm={}
      this.courseInfoForm.teacherName = this.$store.state.username
      this.courseInfoForm.teacherId = this.$store.state.uuid
      this.courseInfoForm.applicationTypeId = this.applicationTypeId
    },
    //申请删除课程
    applyForDeleteCourse(index){
      //找到删除的id
      this.courseFormOperationType.map(item=>{
        if(item.name==="删除"){
          this.teachingCourses[index].applicationTypeId = item.applicationTypeId
        }
      })
      // console.log("申请删除")
      // console.log(this.teachingCourses[index])
      ElLoading.service();
      // 申请删除
      this.axios({
        method:'POST',
        url:'http://121.37.98.3:9090/courses/applications',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          number:this.teachingCourses[index].number,
          name:this.teachingCourses[index].name,
          classHour: this.teachingCourses[index].classHour,
          credit: this.teachingCourses[index].credit.toString(),
          capacity: this.teachingCourses[index].capacity,
          majorId:this.teachingCourses[index].majorId,
          teacherId:this.teachingCourses[index].teacherId,
          description: this.teachingCourses[index].description,
          applicationTypeId:this.teachingCourses[index].applicationTypeId,
          courseId: this.teachingCourses[index].courseId,
          timetableList: this.teachingCourses[index].timetableList,
        }
      }).then(res=>{
        const loading = ElLoading.service();
        loading.close()
        if(res.data.success){
          ElMessage.success({
            message:'申请提交成功',
            duration:1500
          })
          this.reload();
        }
        else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          })
        }
      })
    },
    //申请修改课程
    applyForEditCourse(index){
      //找到修改的id
      this.courseFormOperationType.map(item=>{
        if(item.name==="修改"){
          this.applicationTypeId = item.applicationTypeId
        }
      })
      this.courseInfoForm=this.getEditCourseInfo(index);
      // console.log(this.teachingCourses[index])
      this.ifShowDialog=true
      // console.log(this.ifShowDialog)
    },
    //对要修改的课程信息做一次整理
    getEditCourseInfo(index){
      let editCourseInfo={};
      editCourseInfo.applicationTypeId = this.applicationTypeId;
      editCourseInfo.capacity=this.teachingCourses[index].capacity;
      editCourseInfo.classHour=this.teachingCourses[index].classHour;
      editCourseInfo.credit=this.teachingCourses[index].credit;
      editCourseInfo.year=this.teachingCourses[index].year;
      editCourseInfo.term=this.teachingCourses[index].term;
      editCourseInfo.availableTypeId = this.teachingCourses[index].availableTypeId;
      editCourseInfo.availableMajorList = this.teachingCourses[index].availableMajorList;
      editCourseInfo.description=this.teachingCourses[index].description;
      editCourseInfo.instituteId=this.teachingCourses[index].instituteId;
      editCourseInfo.majorId=this.teachingCourses[index].majorId;
      editCourseInfo.number=this.teachingCourses[index].number;
      editCourseInfo.name=this.teachingCourses[index].name;
      editCourseInfo.teacherName=this.teachingCourses[index].teacherName;
      editCourseInfo.courseId=this.teachingCourses[index].courseId;
      editCourseInfo.week=[];
      editCourseInfo.courseTimeList=[];
      editCourseInfo.classroom=[this.teachingCourses[index].timetableList[0].buildingId,this.teachingCourses[index].timetableList[0].classroomId];
      this.teachingCourses[index].timetableList.forEach(value => {
        if(!editCourseInfo.week.includes(value.week)){editCourseInfo.week.push(value.week)}
        value.sessions.forEach(val => {
          let item=[value.day,val];
          editCourseInfo.courseTimeList.push(item);
        })
      })
      return editCourseInfo;
    },
    //处理用户提交
    handleUserSubmit(courseInfoForm){
      ElLoading.service();
      this.axios({
        method:'POST',
        url:'http://121.37.98.3:9090/courses/applications',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          number:courseInfoForm.number,
          name:courseInfoForm.name,
          classHour: courseInfoForm.classHour,
          credit: courseInfoForm.credit.toString(),
          capacity: courseInfoForm.capacity,
          majorId:courseInfoForm.majorId,
          teacherId:courseInfoForm.teacherId,
          description: courseInfoForm.description,
          applicationTypeId:courseInfoForm.applicationTypeId,
          courseId: courseInfoForm.courseId,
          timetableList: courseInfoForm.timetableList,
          year: courseInfoForm.year,
          term: courseInfoForm.term,
          availableTypeId : courseInfoForm.availableTypeId,
          availableMajorList : courseInfoForm.availableMajorList
        }
      }).then(res=>{
        console.log(res.data)
        const loading = ElLoading.service();
        loading.close()
        if(res.data.success){
          ElMessage.success({
            message:'申请提交成功',
            duration:1500
          })
          this.reload();
        }
        else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          })
        }
      })
    },
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
    }
  },
  created() {
    ElLoading.service();
    //获取对课程不同操作的id
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/applications/types',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      this.courseFormOperationType = res.data.data
    });
    //获取该老师申请的所有课程
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/courses/applications/'+this.$store.state.uuid,
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      // console.log(res.data)
      this.applicationCourseInfoList = res.data.data
    });
    //获取该老师正在开课的所有课程
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/courses/teacher/'+this.$store.state.uuid,
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      console.log(res)
      this.teachingCourses = res.data.data
      for (let i = 0; i < this.teachingCourses.length; i++) {
        this.teachingCourses[i].courseTime = this.courseTime(this.teachingCourses[i]);
      }
      const loading = ElLoading.service();
      loading.close()
    });
  }
}
</script>

<style scoped>

</style>
