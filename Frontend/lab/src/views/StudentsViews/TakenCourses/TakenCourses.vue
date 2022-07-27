<template>
  <header-navigation/>
  <div style="display: flex;">
    <student-side-navigation/>
    <el-table :data="showTakenCourses" style="width:100%" border  height="70vh"
              :header-cell-style="{background:'#79bbff', color:'#fff'}">
      <!--    课程名称-->
      <el-table-column>
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
        <el-table-column prop="number" label="课程编号"  />
      </el-table-column>
      <!--    教师-->
      <el-table-column>
        <el-table-column prop="teacherName" label="任课教师"/>
      </el-table-column>
      <el-table-column>
        <el-table-column prop="major" label="开课专业" />
      </el-table-column>
      <el-table-column>
        <template #header>
          <el-button size="small" @click="searchCourses">查询</el-button>
        </template>
        <el-table-column prop="grade" label="成绩" />
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import HeaderNavigation from "@/components/HeaderNavigation";
import StudentSideNavigation from "@/views/StudentsViews/StudentSideNavigation";
export default {
  name: "TakenCourses",
  components: {StudentSideNavigation, HeaderNavigation},
  data(){
    return{
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
        searchYear:'',
        searchTerm:''
      },
      showTakenCourses:[],
      allTakenCourses:[]
    }
  },
  methods:{
    searchCourses(){
      console.log(this.searchForm)
      this.showTakenCourses = []
      for (let i = 0; i < this.allTakenCourses.length; i++) {
        if(
            this.allTakenCourses[i].year.indexOf(this.searchForm.searchYear)!==-1 &&
            this.allTakenCourses[i].term.indexOf(this.searchForm.searchTerm)!== -1
        ){
          this.showTakenCourses.push(this.allTakenCourses[i])
        }
      }
    }
  },
  //获取已修课程
  created() {
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/courses/student/finished/'+this.$store.state.uuid,
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      },
    }).then(res=>{
      this.allTakenCourses = res.data.data
      this.allTakenCourses.forEach(takenCourse=>{
        takenCourse.grade = 'SSS+'
      })
      this.showTakenCourses = res.data.data
    })
  }
}
</script>

<style scoped>

</style>
