<template>
  <header-navigation/>
  <div style="display: flex" >
    <admin-side-navigation/>
    <el-tabs v-model="activeTabName" type="card" style="width: 100%" @tab-click="handleClick">
      <el-tab-pane label="当前课程信息" name="courseInfo">
        <div style="width: 100%;text-align: center">
          <show-course-info :course-info-list="courseInfoList" @edit-click="editCourseItem" @delete-click="deleteCourseItem"></show-course-info>
          <el-dialog width="80%" v-model="isShowDialog" :destroy-on-close="true" :close-on-press-escape="false" :close-on-click-modal="false" :show-close="false">
            <add-course-form ref="editForm"
                             v-bind:course_info_form_from_parent="courseInfoForm"
                             v-bind:if-admin-operating="true"
                             @cancel-submit="cancelSubmit"
                             key="editCourse"
                             @user-submit="editCourseInfo"
            >
            </add-course-form>
          </el-dialog>
        </div>

      </el-tab-pane>
      <el-tab-pane label="审核课程" name="pendingApprovalCourseInfo">
        <div style="width: 100%;text-align: center">
          <show-application-course-info :application-course-info-list="applicationCourseInfoList" :ifAdminOperating="true"/>
          <el-pagination
              v-model:currentPage="applicationCourseCurrentPage"
              v-model:page-size="applicationCoursePageSize"
              :page-sizes="[5,10,20]"
              :small="false"
              :background="true"
              layout="total, sizes, prev, pager, next, jumper"
              :total="applicationCourseInfoList.length"
              :default-page-size="10"
          />
        </div>
      </el-tab-pane>
      <el-tab-pane label="添加课程" name="addCourses">
        <el-scrollbar style="width: 100%;" height="70vh">
          <div style="display: flex">
            <div style="width: 69%">
              <h5>单个课程录入</h5>
              <add-course-form v-bind:course_info_form_from_parent="courseInfoForm"
                               v-bind:if-admin-operating="true"
                               @user-submit="addNewCourse"
                               :is-add-course="isAddCourse"
                               key="addCourse"
                               ref="addCourseForm"
              >
              </add-course-form>
            </div>
            <el-divider direction="vertical" style="width: 1%;min-height: 70vh"/>
            <div style="width: 30%">
              <multi-save-data ref="multidata" @save-data="saveCourses">
                <template v-slot>
                  <h5>批量课程导入</h5>
                </template>
              </multi-save-data>
            </div>
          </div>
        </el-scrollbar>
      </el-tab-pane>
    </el-tabs>
    </div>
</template>

<script>
import AdminSideNavigation from "@/views/AdminViews/AdminSideNavigation";
import HeaderNavigation from "@/components/HeaderNavigation";
import {Search} from "@element-plus/icons";
import MultiSaveData from "@/components/multiSaveData";
import AddCourseForm from "@/views/CommonViews/addCourseForm";
import ShowCourseInfo from "./ChildComponent/ShowCourseInfo";
import ShowApplicationCourseInfo from "@/views/CommonViews/ShowApplicationCourseInfo";
import {ElLoading, ElMessage} from "element-plus";
export default {
  inject:['reload'],
  name: "ManageCourseView",
  components: {
    ShowApplicationCourseInfo,
    AddCourseForm, MultiSaveData, AdminSideNavigation, HeaderNavigation,Search,ShowCourseInfo},
  data(){
    return{
      isAddCourse:false,
      activeTabName:'courseInfo',
      //tab当前课程信息
      isShowDialog:false,
      courseInfoList:[],
      courseInfoForm:{},
      //tab待审核课程
      applicationCourseInfoList:[],
      applicationCourseCurrentPage:1,
      applicationCoursePageSize:10
    }
  },
  methods:{
    cancelSubmit(){
      this.isShowDialog=false;
    },
    saveCourses(){
      let outFunction=false;
      let data=[];
      let courseList=[];
      data.push(...this.$refs['multidata'].multiData);
      console.log(data);
      data.forEach(value => {
        let courseInfoItem={
          number:'',
          name:'',
          classHour: '',
          credit: '',
          capacity: '',
          majorId:'',
          teacherId:'',
          timetableList:[],
          description:''
        }
        courseInfoItem.number=value.number;
        courseInfoItem.name=value.name;
        courseInfoItem.classHour=parseInt(value.classHour);
        courseInfoItem.credit=value.credit;
        courseInfoItem.capacity=parseInt(value.capacity);
        courseInfoItem.majorId=this.$refs.addCourseForm.getMajorId(value.major);
        courseInfoItem.teacherId=this.$refs.addCourseForm.getTeacherIdFormParent(value.teacherName);
        if(courseInfoItem.teacherId==='false'){
          ElMessage.error({
            message:'该教师不存在'
          })
          outFunction=true;
          return
        }else if(courseInfoItem.teacherId==='离职'){
          ElMessage.error({
            message:'该教师已离职'
          })
          outFunction=true
          return;
        }

        let weeks=[];
        value.week.substring(1,value.week.length-1).split(',').forEach(value=>{
          weeks.push(parseInt(value));
        })
        let courseTime=[];
        value.courseTime.substring(1,value.courseTime.length-1).split(',').forEach(value=>{
          let courseTimeItem=[];
          value.substring(1,value.length-1).split('-').forEach(val=>{
            courseTimeItem.push(parseInt(val));
          })
          courseTime.push(courseTimeItem)
        })
        let classroom=this.$refs.addCourseForm.getClassroomId(value.classroom);
        courseInfoItem.timetableList.push(...this.$refs.addCourseForm.getTimetableListFormParent(weeks,classroom,courseTime));
        courseInfoItem.description=value.description;
        courseList.push(courseInfoItem);
      })
      if(outFunction){return;}
      console.log(courseList);
      this.axios({
        method:'post',
        url:'http://121.37.98.3:9090/courses/batch',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          list:courseList
        }
      }).then(res=>{
        console.log(res)
        if(res.data.success){
          ElMessage.success({
            message:"批量导入成功！"
          })
          this.reload();
        }else {
          ElMessage.error({
            message:res.data.message
          })
        }
      })
      // this.reload();
    },
    editCourseItem(index){
      // this.courseInfoForm=this.courseInfoList[index]
      this.isShowDialog=true;
      this.courseInfoForm=this.getEditCourseInfo(index);
    },
    //删除课程
    deleteCourseItem(index){
      console.log(this.courseInfoList[index].courseId)
      this.axios({
        method:'delete',
        url:'http://121.37.98.3:9090/courses',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          courseId:this.courseInfoList[index].courseId
        }
      }).then(res=>{
        console.log(res.data)
        if(res.data.success){
          ElMessage.success({
            message:'删除成功',
            duration:1500
          });
          this.reload();
        }
        else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          });
        }
      })
    },
    //如果去了新增课程,就把表单设置为空
    handleClick(tabName){
      // console.log(tabName.props.name)
      if(tabName.props.name==='addCourses'){
        this.courseInfoForm = {}
      }
    },
    getEditCourseInfo(index){
      let editCourseInfo={};
      editCourseInfo.capacity=this.courseInfoList[index].capacity;
      editCourseInfo.classHour=this.courseInfoList[index].classHour;
      editCourseInfo.credit=this.courseInfoList[index].credit;
      editCourseInfo.year=this.courseInfoList[index].year;
      editCourseInfo.term=this.courseInfoList[index].term;
      editCourseInfo.availableTypeId = this.courseInfoList[index].availableTypeId;
      editCourseInfo.availableMajorList = this.courseInfoList[index].availableMajorList;
      editCourseInfo.description=this.courseInfoList[index].description;
      editCourseInfo.instituteId=this.courseInfoList[index].instituteId;
      editCourseInfo.majorId=this.courseInfoList[index].majorId;
      editCourseInfo.number=this.courseInfoList[index].number;
      editCourseInfo.name=this.courseInfoList[index].name;
      editCourseInfo.teacherName=this.courseInfoList[index].teacherName;
      editCourseInfo.courseId=this.courseInfoList[index].courseId;
      editCourseInfo.week=[];
      editCourseInfo.courseTimeList=[];
      editCourseInfo.classroom=[this.courseInfoList[index].timetableList[0].buildingId,this.courseInfoList[index].timetableList[0].classroomId];
      this.courseInfoList[index].timetableList.forEach(value => {
        if(!editCourseInfo.week.includes(value.week)){editCourseInfo.week.push(value.week)}
        value.sessions.forEach(val => {
          let item=[value.day,val];
          editCourseInfo.courseTimeList.push(item);
        })
      })
      return editCourseInfo;
    },

    //管理员添加课程
    addNewCourse(addCourseInfoForm){
      ElLoading.service();
      this.axios({
        method:'POST',
        url:'http://121.37.98.3:9090/courses',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          number:addCourseInfoForm.number,
          name:addCourseInfoForm.name,
          classHour: parseInt(addCourseInfoForm.classHour),
          credit: addCourseInfoForm.credit,
          capacity: parseInt(addCourseInfoForm.capacity),
          majorId:addCourseInfoForm.majorId,
          teacherId:addCourseInfoForm.teacher,
          timetableList:addCourseInfoForm.timetableList,
          description:addCourseInfoForm.description,
          availableTypeId:addCourseInfoForm.availableTypeId,
          availableMajorIdList:addCourseInfoForm.availableMajorIdList,
          year:addCourseInfoForm.year,
          term:addCourseInfoForm.term
        }
      }).then(res=>{
        const loading = ElLoading.service();
        loading.close()
        if(res.data.success){
          ElMessage.success({
            message:'提交成功',
            duration:1500
          });
          this.reload();
        }
        else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          });
        }
      })
    },
    //管理员修改课程信息
    editCourseInfo(editCourseInfoForm){
      this.axios({
        method:'PUT',
        url:'http://121.37.98.3:9090/courses',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          number:editCourseInfoForm.number,
          name:editCourseInfoForm.name,
          classHour: parseInt(editCourseInfoForm.classHour),
          credit: editCourseInfoForm.credit.toString(),
          capacity: parseInt(editCourseInfoForm.capacity),
          majorId:editCourseInfoForm.majorId,
          teacherId:editCourseInfoForm.teacher,
          timetableList:editCourseInfoForm.timetableList,
          description:editCourseInfoForm.description,
          courseId:editCourseInfoForm.courseId,
          availableTypeId:editCourseInfoForm.availableTypeId,
          availableMajorIdList:editCourseInfoForm.availableMajorIdList,
          year:editCourseInfoForm.year,
          term:editCourseInfoForm.term
        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:'提交成功',
            duration:1500
          });
          this.reload();
        }
        else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          });
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
  computed:{

  },
  created() {
    ElLoading.service();
    //获取课程信息
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/courses',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      let data=res.data.data
      this.courseInfoList.push(...data);
      for (let i = 0; i < this.courseInfoList.length; i++) {
        this.courseInfoList[i].courseTime = this.courseTime(this.courseInfoList[i]);
      }
      console.log(this.courseInfoList)
    });
    //待审核课程的信息
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/courses/applications',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      this.applicationCourseInfoList = res.data.data
      const loading = ElLoading.service();
      loading.close()
    });
  }
}
</script>

<style scoped>

</style>
