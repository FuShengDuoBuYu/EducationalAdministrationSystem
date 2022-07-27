<template>
  <el-table :data="showCourseInfoList" style="width:100%" border  height="70vh"
            :header-cell-style="{background:'#79bbff', color:'#fff'}"
            :key="timeStampKey"
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
      <template #header>
        <el-select v-model="searchForm.searchYear" placeholder="选择学年" size="small">
          <el-option
              v-for="item in years"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </template>
      <el-table-column prop="year" label="开课学年"  />
    </el-table-column>
<!--    学期-->
    <el-table-column>
      <template #header>
        <el-select v-model="searchForm.searchTerm" size="small" placeholder="选择学期">
          <el-option
              v-for="item in terms"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </template>
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
      <el-table-column prop="selectedStudents" label="已选" />
    </el-table-column>
    <el-table-column>
      <template #header>
        <el-button size="small" @click="searchCourses">查询</el-button>
      </template>
      <el-table-column fixed="right" label="操作">
        <template #header>
          操作
        </template>
        <template #default="scope">
          <el-popconfirm title="是否确认删除该课程?" @confirm="deleteClick(scope.$index)">
            <template #reference>
              <el-button type="text" size="small">删除</el-button>
            </template>
          </el-popconfirm>
          <el-button type="text" size="small" @click="editClick(scope.$index)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table-column>
  </el-table>
</template>

<script>

import addCourseForm from "../../../CommonViews/addCourseForm";

export default {
  name: "ShowCourseInfo",
  computed:{
  },
  data(){
    return{
      props: {
        multiple: true,
      },
      showCourseInfoList:[],
      isShowDialog:false,
      courseTime:[],
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
      terms:[
        {label:'...',value:''},
        {label:'春',value:'春'},
        {label:'秋',value:'秋'}
      ],
      searchForm:{
        searchTeacherName:'',
        searchYear:'',
        searchTerm:'',
        searchNumber:'',
        searchClassName:'',
        searchClassroom:[''],
        searchCourseTime:[]
      },
      timeStampKey:''
    }
  },
  props:{
    courseInfoList:{
      type:Array,
      default(){
        return [];
      }
    }

  },
  components:{
    addCourseForm
  },
  watch:{
    courseInfoList:{
      handler(val,old){
        this.showCourseInfoList = val
      },
      deep:true
    },

  },
  methods:{
    //向父组件传递要删除的信息
    deleteClick(index){
      this.$emit('deleteClick',index);
    },
    //向父组件传递要修改的信息
    editClick(index){
      // console.log("in showCourseInfo")
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
      // console.log(row)
    },
    //根据条件查询课程
    searchCourses(){
      //先清空
      this.showCourseInfoList = []
      //根据条件查找
      for(let i = 0;i < this.courseInfoList.length;i++){
        if(
            this.courseInfoList[i].teacherName.indexOf(this.searchForm.searchTeacherName)!==-1&&
            this.courseInfoList[i].name.indexOf(this.searchForm.searchClassName)!==-1&&
            this.courseInfoList[i].number.indexOf(this.searchForm.searchNumber)!==-1&&
            this.courseInfoList[i].term.indexOf(this.searchForm.searchTerm)!==-1&&
            this.courseInfoList[i].year.indexOf(this.searchForm.searchYear)!==-1&&
            this.searchCoursesByClassroom(this.courseInfoList[i],this.searchForm.searchClassroom)&&
            this.searchCoursesByTime(this.courseInfoList[i],this.searchForm.searchCourseTime)
        ){
          this.showCourseInfoList.push(this.courseInfoList[i])
        }
      }
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
    }

  },
  created() {
    this.showCourseInfoList = this.courseInfoList
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
      // console.log(this.teachingBuildingAndClassroom)
    });
  }
}
</script>

<style scoped>

</style>
