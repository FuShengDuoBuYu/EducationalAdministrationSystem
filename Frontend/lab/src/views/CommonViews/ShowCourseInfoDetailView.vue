<template>
  <h1>{{courseInfo.name}}</h1>
  <el-divider content-position="left">课程基本信息</el-divider>
  <el-descriptions border>
    <el-descriptions-item>
      <template #label>
          课程编号
      </template>
      {{courseInfo.number}}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
          开课院系
      </template>
      {{courseInfo.institute}}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        开课专业
      </template>
      {{courseInfo.major}}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        学分
      </template>
      {{courseInfo.credit}}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        学时
      </template>
      {{courseInfo.classHour}}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        课程容量
      </template>
      {{courseInfo.capacity}}
    </el-descriptions-item>
  </el-descriptions>

  <el-divider content-position="left">课程详细信息</el-divider>
  <el-descriptions border>
    <el-descriptions-item>
      <template #label>
        教师姓名
      </template>
      {{courseInfo.teacherName}}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        上课地点
      </template>
      {{location}}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        上课周次
      </template>
      {{weeks}}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        上课时间
      </template>
      {{courseTime}}
    </el-descriptions-item>
  </el-descriptions>


  <el-divider content-position="left">课程设置信息</el-divider>
  <el-descriptions border>
    <el-descriptions-item>
      <template #label>
        课程描述
      </template>
      {{courseInfo.description}}
    </el-descriptions-item>
    <el-descriptions-item v-if="!ifSelectCourse">
      <template #label>
        审核状态
      </template>
      {{courseInfo.applicationResult}}
    </el-descriptions-item>
  </el-descriptions>
</template>

<script>
export default {
  name: "ShowCourseInfoDetailView",
  props:{
    courseInfo:Object,
    ifSelectCourse:{
      type:Boolean,
      default: false
    }
  },
  computed:{
    //上课地点
    location(){
        return this.courseInfo.timetableList[0].classroomName;
    },
    //上课周次
    weeks(){
      let weeks=[];
      // console.log(this.courseInfo)
      this.courseInfo.timetableList.forEach(value => {
        // console.log("value" ,value.week)
        if(!weeks.includes(value.week)){
          console.log(value.week)
          weeks.push(value.week)
        }
      })
      weeks.sort(function(a, b){return a-b})
      return this.getTimeString(weeks)
    },
    //节次时间
    courseTime(){
      let courseTimeString=''
      let existDays=[];
      this.courseInfo.timetableList.forEach(value =>{
        if(!existDays.includes(value.day)){
          existDays.push(value.day);
        }
      });
      existDays.sort(function(a, b){return a-b})
      existDays.forEach(value => {
        for(let i=0;i<this.courseInfo.timetableList.length;i++){
          if(this.courseInfo.timetableList[i].day===value){
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
            this.courseInfo.timetableList[i].sessions.sort(function(a, b){return a-b})
            courseTimeString+=this.getTimeString(this.courseInfo.timetableList[i].sessions);
            courseTimeString+=' ';
            break;
          }
        }
      })
      return courseTimeString;
    }
  },
  methods:{
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
    }
  }
}
</script>

<style scoped>

</style>
