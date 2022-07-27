<template>
  <el-table :data="selectedCourseTableList" style="width: 100%" :cell-style="{'text-align':'center'}" height="60vh" border :header-cell-style="{'text-align':'center',background:'#79bbff', color:'#fff'}">
    <el-table-column prop="time" label="节次"/>
    <el-table-column prop="Monday" label="周一"/>
    <el-table-column prop="Tuesday" label="周二" />
    <el-table-column prop="Wednesday" label="周三" />
    <el-table-column prop="Thursday" label="周四" />
    <el-table-column prop="Friday" label="周五" />
  </el-table>
</template>

<script>
import {ElLoading} from "element-plus";

export default {
  name: "StudentSelectedCourseBoard.vue",
  props:{
  },
  computed:{
    selectedCourseTableList(){
      let res = new Array(this.courseSchedule.length)
      //处理课程数据为便于表格显示的格式
      //先节次
      for (let i = 0; i < this.courseSchedule.length; i++) {
        res[i] = {time: ('第' + (i + 1) + '节 ' + this.courseSchedule[i]['startTime'] + '~' + this.courseSchedule[i]['endTime'])}
        res[i].Monday = []
        res[i].Tuesday = []
        res[i].Wednesday = []
        res[i].Thursday = []
        res[i].Friday = []
      }
      //处理各个课程
      for(let i = 0;i < this.selectedCourseList.length;i++){
        //处理某个课程的timetableList
        for(let j = 0;j < this.selectedCourseList[i].timetableList.length;j++){
          //某个timetable
          let timetable = this.selectedCourseList[i].timetableList[j]
          switch (timetable.day){
            //周一
            case 1:
              for(let k = 0;k < timetable.sessions.length;k++){
                //不含有该课程就加入
                if(res[timetable.sessions[k]-1].Monday.indexOf(this.selectedCourseList[i].name)===-1)
                  res[timetable.sessions[k]-1].Monday.push(this.selectedCourseList[i].name)
              }
              break
              //周二
            case 2:
              for(let k = 0;k < timetable.sessions.length;k++){
                //不含有该课程就加入
                if(res[timetable.sessions[k]-1].Tuesday.indexOf(this.selectedCourseList[i].name)===-1)
                  res[timetable.sessions[k]-1].Tuesday.push(this.selectedCourseList[i].name)
              }
              break
              //周三
            case 3:
              for(let k = 0;k < timetable.sessions.length;k++){
                //不含有该课程就加入
                if(res[timetable.sessions[k]-1].Wednesday.indexOf(this.selectedCourseList[i].name)===-1)
                  res[timetable.sessions[k]-1].Wednesday.push(this.selectedCourseList[i].name)
              }
              break
              //周四
            case 4:
              for(let k = 0;k < timetable.sessions.length;k++){
                //不含有该课程就加入
                if(res[timetable.sessions[k]-1].Thursday.indexOf(this.selectedCourseList[i].name)===-1)
                  res[timetable.sessions[k]-1].Thursday.push(this.selectedCourseList[i].name)
              }
              break
              //周五
            case 5:
              for(let k = 0;k < timetable.sessions.length;k++){
                //不含有该课程就加入
                if(res[timetable.sessions[k]-1].Friday.indexOf(this.selectedCourseList[i].name)===-1)
                  res[timetable.sessions[k]-1].Friday.push(this.selectedCourseList[i].name)
              }
              break
          }
        }
      }
      return res
    }
  },
  data(){
    return {
      courseSchedule:[],
      selectedCourseList:[]
    }
  },
  methods:{

  },
  created() {
    // 获取节数和时间
    ElLoading.service();
    //获取学生已选课程
    this.axios({
      method:"GET",
      url: 'http://121.37.98.3:9090/courses/student/selected/'+this.$store.state.uuid,
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      },
    }).then(res=>{
      console.log(res.data.data)
      this.selectedCourseList = res.data.data
    })
    //获取原本的课程时间安排信息
    this.axios({
      method:"GET",
      url: 'http://121.37.98.3:9090/schedules',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      },
    }).then(res=>{
      if(res.data.data.length!==0){
        this.courseSchedule = res.data.data
      }
      const loading = ElLoading.service();
      loading.close()
    })
  }
}
</script>

<style scoped>

</style>
