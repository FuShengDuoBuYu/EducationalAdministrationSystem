<template>
  <el-table :data="pendingSelectCourseInfoList" border :header-cell-style="{background:'#79bbff', color:'#fff'}">
    <el-table-column>
      <template #header>
        <el-input v-model="searchForm.searchClassName" size="small"></el-input>
      </template>
      <el-table-column prop="name" label="课程名称" />
    </el-table-column>

    <el-table-column>
      <template #header>
        <el-input v-model="searchForm.searchNumber" size="small"></el-input>
      </template>
      <el-table-column prop="number" label="课程编号"  />
    </el-table-column>

    <el-table-column>
      <template #header>
        <el-input v-model="searchForm.searchTeacherName" size="small"></el-input>
      </template>
      <el-table-column prop="teacherName" label="授课老师"  />
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
      <el-table-column prop="availableType" label="课程类型"  />
    </el-table-column>

    <el-table-column>
      <el-table-column prop="major" label="开课专业" />
    </el-table-column>

    <el-table-column>
      <el-table-column prop="credit" label="学分" width="60%"/>
    </el-table-column>

    <el-table-column>
      <el-table-column prop="classHour" label="学时" width="60%"/>
    </el-table-column>

    <el-table-column>
      <el-table-column prop="selectedStudents" label="已选" width="60%"/>
    </el-table-column>

    <el-table-column>
      <el-table-column prop="capacity" label="容量" width="60%"/>
    </el-table-column>

    <el-table-column>
      <template #header>
        <el-button size="small" @click="searchCourses">查询</el-button>
      </template>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <!--          查看更多信息-->
          <el-button circle size="small" type="info" @click="showSelectCourseDetailInfo(scope.$index)"><el-icon><More/></el-icon></el-button>
          <!--        选课-->
          <el-popconfirm title="是否确认选课?" @confirm="selectCourse(scope.$index)">
            <template #reference>
              <el-button circle type="success" size="small"><el-icon><Select/></el-icon></el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table-column>
  </el-table>


<!--  展示选课详细信息-->
  <el-dialog width="80%" v-model="ifShowSelectCourseDetail" :destroy-on-close="true">
    <show-course-info-detail-view v-bind:course-info="this.selectCourseInfo" v-bind:if-select-course="true"/>
  </el-dialog>
</template>

<script>
import {More,Select} from "@element-plus/icons";
import {ElMessage, ElMessageBox} from "element-plus";
import ShowCourseInfoDetailView from "@/views/CommonViews/ShowCourseInfoDetailView";
import StudentSelectedCourseBoard
  from "@/views/StudentsViews/SelectCourseView/ChildComponent/StudentSelectedCourseBoard";

export default {
  inject:['reload'],
  name: "ShowSelectCourseInfo",
  props:{
    ifStartSelectCourse:Boolean,
    selectionRound:''
  },
  computed:{

  },
  data(){
    return{
      props: {
        multiple: true,
      },
      selectCourseInfo:{},
      ifShowSelectCourseDetail:false,
      pendingSelectCourseInfoList:[],
      selectCourseInfoList:[],
      courseTime:[],
      searchForm:{
        searchTeacherName:'',
        searchNumber:'',
        searchClassName:'',
        searchClassroom:[''],
        searchCourseTime:[]
      },
      teachingBuildingAndClassroom:[],
      years:[
        {label:'...',value:''},
        {label:'2010-2011',value:'2010-2011'},
        {label:'2011-2012',value:'2011-2012'},
        {label:'2012-2013',value:'2012-2013'},
        {label:'2013-2014',value:'2013-2014'},
        {label:'2014-2015',value:'2014-2015'},
        {label:'2015-2016',value:'2015-2016'},
        {label:'2016-2017',value:'2016-2017'},
        {label:'2017-2018',value:'2017-2018'},
        {label:'2018-2019',value:'2018-2019'},
        {label:'2019-2020',value:'2019-2020'},
        {label:'2020-2021',value:'2020-2021'},
        {label:'2021-2022',value:'2021-2022'},
        {label:'2022-2023',value:'2022-2023'},
      ],
    }
  },
  components:{
    StudentSelectedCourseBoard,
    ShowCourseInfoDetailView,
    More,
    Select
  },
  methods:{
    //根据条件查询课程
    searchCourses(){
      //先清空
      this.pendingSelectCourseInfoList = []
      //根据条件查找
      for(let i = 0;i < this.selectCourseInfoList.length;i++){
        if(
            this.selectCourseInfoList[i].teacherName.indexOf(this.searchForm.searchTeacherName)!==-1&&
            this.selectCourseInfoList[i].name.indexOf(this.searchForm.searchClassName)!==-1&&
            this.selectCourseInfoList[i].number.indexOf(this.searchForm.searchNumber)!==-1&&
            this.searchCoursesByClassroom(this.selectCourseInfoList[i],this.searchForm.searchClassroom)&&
            this.searchCoursesByTime(this.selectCourseInfoList[i],this.searchForm.searchCourseTime)
        ){
          this.pendingSelectCourseInfoList.push(this.selectCourseInfoList[i])
        }
      }
    },
    //选课
    selectCourse(index){
      //第一轮随便选
      if(this.selectionRound==='1' || (this.selectionRound==='2' && this.pendingSelectCourseInfoList[index].capacity>this.pendingSelectCourseInfoList[index].selectedStudents)){
        console.log(this.selectionRound)
        this.axios({
          method: "post",
          url: 'http://121.37.98.3:9090/courses/student/selected',
          headers: {
            'token': localStorage.getItem("token"),
            'Context-Type': 'application/json'
          },
          data: {
            studentId : this.$store.state.uuid,
            courseId : this.pendingSelectCourseInfoList[index].courseId
          },
        }).then(res => {
          if (res.data.success) {
            ElMessage.success({
              message: '选课成功',
            });
            this.reload()
          } else {
            ElMessage.error({
              message: res.data.message,
            })
          }
        })
      }
      //无余量,事务申请
      else{
        if(this.pendingSelectCourseInfoList[index].capacity>=this.pendingSelectCourseInfoList[index].selectedStudents){
          ElMessageBox.prompt('您要申请的课程是<strong>'+this.pendingSelectCourseInfoList[index].name+'</strong>'+'<br>请输入申请理由', '选课事务申请', {
            confirmButtonText: '提交',
            cancelButtonText: '取消',
            dangerouslyUseHTMLString: true,
          })
          .then(({ value }) => {
            this.axios({
              method:'POST',
              url:'http://121.37.98.3:9090/courses/student/applications',
              headers:{
                'token':localStorage.getItem("token"),
                'Context-Type':'application/json'
              },
              data:{
                studentId: this.$store.state.uuid,
                courseId: this.pendingSelectCourseInfoList[index].courseId,
                reason: value
              }
            }).then(res=>{
              if(res.data.success){
                ElMessage.success({
                  message:'已提交申请',
                  duration:1500
                })
              }
              else{
                ElMessage.error({
                  message:res.data.message,
                  duration:1500
                })
              }
            })
          })
        }
      }
    },
    //展示所选课程详细信息
    showSelectCourseDetailInfo(index){
      this.selectCourseInfo = this.pendingSelectCourseInfoList[index]
      this.ifShowSelectCourseDetail = true
    },

    //查找上课地点
    searchCoursesByClassroom(sourceData,searchData){
      //如果搜索为空,代表全局搜索
      // console.log(sourceData);
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
    //查找上课时间
    searchCoursesByTime(sourceData,searchData){
      // console.log(searchData)
      // console.log(sourceData)
      let flag=true;
      for(let i=0;i<searchData.length;i++){
        if(!(searchData[i][0]==='')){
          if(!this.isIncludesOneClassTime(sourceData,searchData[i])){
            flag=false;
            break;
          }
        }

      }
      return flag;
    },
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
    showCourseTime(courseInfo){
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
    //如果是开始选课了,就获取选课信息
    if(this.ifStartSelectCourse){
      this.axios({
        method:'GET',
        url:'http://121.37.98.3:9090/courses/student/'+this.$store.state.majorId,
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        }
      }).then(res=>{
        this.selectCourseInfoList = res.data.data
        this.pendingSelectCourseInfoList=res.data.data;
        //获取上课时间的展示
        this.pendingSelectCourseInfoList.forEach(item=>{
          item.courseTime = this.showCourseTime(item)
        })
      })
    }
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
      // console.log(this.teachingBuildingAndClassroom)
    });

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
  }
}
</script>

<style scoped>

</style>
