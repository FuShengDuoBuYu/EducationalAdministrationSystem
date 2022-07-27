<template>
  <span>{{'当前学年 '+this.currentYearAndTerm.currentYear+' '+this.currentYearAndTerm.currentTerm}}</span>
  <el-table :data="showCurrentTermCoursesInfoList" style="width:100%" border  height="70vh"
            :header-cell-style="{background:'#79bbff', color:'#fff'}"
            @row-dblclick="getStudents">
    <!--    课程名称-->
    <el-table-column>
      <template #header>
        <el-input v-model="searchForm.searchClassName" size="small" />
      </template>
      <el-table-column prop="name" label="课程名称"  />
    </el-table-column>
    <!--    学年-->
    <el-table-column>
      <el-table-column prop="year" label="开课学年"  />
    </el-table-column>
    <!--    学期-->
    <el-table-column>
      <el-table-column prop="term" label="开课学期"  />
    </el-table-column>
    <!--    编号-->
    <el-table-column>
      <template #header>
        <el-input v-model="searchForm.searchNumber" size="small" />
      </template>
      <el-table-column prop="number" label="课程编号"  />
    </el-table-column>
    <!--    教师-->
    <el-table-column>
      <template #header>
        <el-input v-model="searchForm.searchTeacherName" size="small" />
      </template>
      <el-table-column prop="teacherName" label="任课教师"/>
    </el-table-column>
    <!--    教室地点-->
    <el-table-column>
      <template #header>
        <el-cascader
            size="small"
            placeholder="选择教室"
            :options="teachingBuildingAndClassroom"
            v-model="searchForm.searchClassroom"
            filterable
            style="width: 100%"
        />
      </template>
      <el-table-column prop="timetableList[0].classroomName" label="上课地点"/>
    </el-table-column>
    <!--    上课时间-->
    <el-table-column>
      <template #header>
        <el-cascader
            :options="courseTime"
            :props="props"
            v-model="searchForm.searchCourseTime"
            collapse-tags
            collapse-tags-tooltip
            clearable
            placeholder="..."
            size="small"
        />
      </template>
      <el-table-column prop="courseTime" label="上课时间" />
    </el-table-column>
    <el-table-column>
      <el-table-column prop="major" label="开课专业" />
    </el-table-column>
    <el-table-column>
      <el-table-column prop="capacity" label="容量" />
    </el-table-column>
    <el-table-column>
      <template #header>
        <el-button size="small" @click="searchCourses">查询</el-button>
      </template>
      <el-table-column prop="selectedStudents" label="操作">
        <template #header>
          已选
        </template>
      </el-table-column>
    </el-table-column>
    <el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-popconfirm v-if="!this.showCurrentTermCoursesInfoList[scope.$index].finished" title="是否确认该课程已结课?" @confirm="finishCourse(scope.$index)">
            <template #reference>
              <el-button circle size="small" type="success"><el-icon><Finished/></el-icon></el-button>
            </template>
          </el-popconfirm>
          <span v-if="this.showCurrentTermCoursesInfoList[scope.$index].finished">已结课</span>
        </template>
      </el-table-column>
    </el-table-column>
  </el-table>
</template>

<script>
import addCourseForm from "../../../CommonViews/addCourseForm";
import {Finished} from "@element-plus/icons";
import {ElMessage} from "element-plus";
export default {
  inject:['reload'],
  name: "ShowCurrentTermCoursesInfo",
  computed:{
    //当前学年
    currentYearAndTerm(){
      let date = new Date();
      if(date.getMonth()>=3&&date.getMonth()<9){
          return {'currentYear':(date.getFullYear()-1)+'-'+date.getFullYear(),'currentTerm': '春'}
      }
      else{
        return {'currentYear':date.getFullYear()+'-'+(date.getFullYear()+1),'currentTerm': '秋'}
      }
    }
  },
  data(){
    return{
      props: {
        multiple: true,
      },
      showCurrentTermCoursesInfoList:[],
      currentTermCoursesInfoList:[],
      isShowDialog:false,
      courseTime:[],
      teachingBuildingAndClassroom:[],
      searchForm:{
        searchTeacherName:'',
        searchYear:'',
        searchTerm:'',
        searchNumber:'',
        searchClassName:'',
        searchClassroom:[''],
        searchCourseTime:[]
      }
    }
  },
  props:{
  },
  components:{
    addCourseForm,
    Finished
  },
  watch:{

  },
  methods:{
    //向父组件传递要删除的信息
    deleteClick(index){
      this.$emit('deleteClick',index);
    },
    //向父组件传递要修改的信息
    editClick(index){
      this.$emit('editClick',index);
    },
    //查看选课名单
    getStudents(row, column, event){
      this.$router.push({
        path:"/selectedStudent",
        query:{
          courseId: row.courseId
        }
      })
    },
    //根据条件查询课程
    searchCourses(){
      console.log('2+6+62+61')
      console.log(this.searchForm)
      console.log(this.currentTermCoursesInfoList)
      //先清空
      this.showCurrentTermCoursesInfoList = []
      //根据条件查找
      for(let i = 0;i < this.currentTermCoursesInfoList.length;i++){
        if(
            this.currentTermCoursesInfoList[i].teacherName.indexOf(this.searchForm.searchTeacherName)!==-1&&
            this.currentTermCoursesInfoList[i].name.indexOf(this.searchForm.searchClassName)!==-1&&
            this.currentTermCoursesInfoList[i].number.indexOf(this.searchForm.searchNumber)!==-1&&
            this.currentTermCoursesInfoList[i].term.indexOf(this.searchForm.searchTerm)!==-1&&
            this.currentTermCoursesInfoList[i].year.indexOf(this.searchForm.searchYear)!==-1&&
            this.searchCoursesByClassroom(this.currentTermCoursesInfoList[i],this.searchForm.searchClassroom)&&
            this.searchCoursesByTime(this.currentTermCoursesInfoList[i],this.searchForm.searchCourseTime)
        ){
          this.showCurrentTermCoursesInfoList.push(this.currentTermCoursesInfoList[i])
        }
      }
    },
    //查找上课地点
    searchCoursesByClassroom(sourceData,searchData){
      //如果搜索为空,代表全局搜索
      if(searchData.length===1){
        return true;
      }

      if(searchData.length===2){
        //某个教学楼的所有教室
        if(searchData[1]===''){
          if(sourceData.timetableList[0].buildingId===searchData[0]){
            return true
          }
          else
            return false
        }
        else{
          if(sourceData.timetableList[0].classroomId===searchData[1]){
            return true
          }
          else{
            return false
          }

        }
      }
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
    courseTimeSting(courseInfo){
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
    //查找上课时间
    searchCoursesByTime(sourceData,searchData){
      let flag=true;
      for(let i=0;i<searchData.length;i++){
        if(!(searchData[i][0]==='')){
          if(!this.isIncludesOneClassTime(sourceData,searchData[i])){
            flag=false;
            break;
          }
        }
      }
      console.log('flag')
      return flag;
    },
    //是否包含一个时间段
    isIncludesOneClassTime(sourceData,classTime){
      let isInclude=false;
      sourceData.timetableList.forEach(value =>{
        if(value.day===classTime[0]){
          if(!(classTime[1]==='')){
            value.sessions.forEach(val=>{
              if(val===classTime[1]){
                isInclude=true;
              }
            })
          }else{
            isInclude=true;
          }
        }
      })
      return isInclude;
    },
    //结课
    finishCourse(index){
      this.axios({
        method:'PUT',
        url:'http://121.37.98.3:9090/courses/finish/'+this.showCurrentTermCoursesInfoList[index].courseId,
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:'结课成功',
            duration:1500
          })
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
    //获取上课时间
    this.axios({
      method:"GET",
      url: 'http://121.37.98.3:9090/schedules',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      },
    }).
    then(res=>{
      if(res.data.data.length!==0){
        let schedule = res.data.data
        let courseTime=[];
        courseTime.push({label:'...',value:'',children:[]})
        let weeks=['周一','周二','周三','周四','周五']
        for(let i=0;i<weeks.length;i++){
          let weekTime={
            label:'',
            value:0,
            children:[]
          }
          weekTime.label=weeks[i];
          weekTime.value=i+1;
          weekTime.children.push({label:'...',value:''})
          for(let j=1;j<=schedule.length;j++){
            let dayTime={
              label:'',
              value:0
            };
            dayTime.value=j;
            dayTime.label='第'+j+'节课 '+schedule[j-1].startTime+'~'+schedule[j-1].endTime;
            weekTime.children.push(dayTime);
          }
          courseTime.push(weekTime);
        }
        this.courseTime = courseTime
      }
    })
    //获取教学楼和教室信息
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/buildings',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      let data=res.data.data;
      this.teachingBuildingAndClassroom.push({label:'...', value:'', children: []})
      data.forEach((value)=>{
        let building={
          label:'',
          value:'',
          children: []
        }
        building.children.push({label:'...', value:''})
        value.classrooms.forEach((val)=>{
          let classroom={
            label:'',
            value:'',
          }
          classroom.label=val.name;
          classroom.value=val.classroomId;
          building.children.push(classroom)
        })
        building.value=value.buildingId;
        building.label=value.name;
        this.teachingBuildingAndClassroom.push(building);
      })
    });
    //获取课程信息
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/courses',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      console.log(res.data.data)
      //获取本学期的课程
      for (let i = 0; i < res.data.data.length; i++) {
        if(
            res.data.data[i].term===this.currentYearAndTerm.currentTerm&&
            res.data.data[i].year===this.currentYearAndTerm.currentYear
        ){
          this.currentTermCoursesInfoList.push(res.data.data[i])
        }
      }
      for (let i = 0; i < this.currentTermCoursesInfoList.length; i++) {
        this.currentTermCoursesInfoList[i].courseTime = this.courseTimeSting(this.currentTermCoursesInfoList[i]);
      }
      //显示本学期课程
      this.showCurrentTermCoursesInfoList = this.currentTermCoursesInfoList
    });
  },
}
</script>

<style scoped>

</style>
