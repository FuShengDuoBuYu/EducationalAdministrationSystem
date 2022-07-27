<template>
  <el-form
      style="padding-left: 5%" ref="addCourseForm" :model="courseInfoForm" label-width="120px" :rules="verificationRules">
    <!--        课程名称-->
    <el-form-item label="课程名称" prop="name" >
      <el-input v-model="courseInfoForm.name" placeholder="请输入课程名"></el-input>
    </el-form-item>
    <!--    院校专业级联选择器-->
    <el-form-item label="开课专业" prop="institute_major" >
      <el-cascader
          placeholder="请选择学院和专业"
          :options="instituteAndMajor"
          v-model="defaultInstituteAndMajor"
          filterable
          ref="cascader"
          @change="editInstituteAndMajor"
          style="width: 100%"
      />
    </el-form-item>
     <!--    开课时间-->
    <el-row>
      <!--年-->
      <el-form-item label="开课年份" prop="year" >
        <el-select v-model="courseInfoForm.year" placeholder="选择学年">
          <el-option
              v-for="item in years"
              :key="item.value"
              :label="item.label"
              :value="item.label"
          />
        </el-select>
      </el-form-item>
      <!--学期-->
      <el-form-item label="开课学期" prop="term" >
        <el-select v-model="courseInfoForm.term" placeholder="选择学期">
          <el-option
              v-for="item in terms"
              :key="item.value"
              :label="item.label"
              :value="item.label"
          />
        </el-select>
      </el-form-item>
    </el-row>
    <!--  可选专业信息-->
    <el-form-item label="可选专业信息" prop="ableToSelectInfo1">
      <el-select v-model="courseInfoForm.availableTypeId" placeholder="选择可选专业信息">
        <el-option
            v-for="item in availableTypes"
            :key="item.availableTypeId"
            :label="item.label"
            :value="item.availableTypeId"
            @change="this.courseInfoForm.availableTypeId = this.availableTypeId;"
        />
      </el-select>
    </el-form-item>
    <el-form-item v-if="courseInfoForm.availableTypeId==='3'" label="可选专业设置" prop="ableToSelectInfo">
      <el-cascader
          placeholder="请选择学院和专业"
          :options="instituteAndMajor"
          v-model="defaultAbleToSelectInstituteAndMajor"
          filterable
          ref="cascaders"
          @change="editAbleToSelectInstituteAndMajor"
          style="width: 100%"
          :props="props"
      />
    </el-form-item>
      <!--    课程编号-->
    <el-form-item label="课程编号" prop="number" >
      <el-input v-model="courseInfoForm.number" placeholder="请输入课程编号"></el-input>
    </el-form-item>
    <el-row>
      <!--        学时-->
      <el-form-item label="学时" prop="classHour">
        <el-input v-model.number="courseInfoForm.classHour" placeholder="请输入学时" ></el-input>
      </el-form-item>
      <!--        学分-->
      <el-form-item label="学分" prop="credit">
        <el-input v-model="courseInfoForm.credit" placeholder="请输入学分"></el-input>
      </el-form-item>
    </el-row>
    <el-row>
      <!--        任课教师-->
      <el-form-item label="任课教师" prop="teacherName">
        <el-input v-model="courseInfoForm.teacherName" placeholder="请输入教师姓名" :disabled="!ifAdminOperating"></el-input>
      </el-form-item>
      <!--        上课地点-->
      <el-form-item label="上课地点" prop="classroom">
<!--        <el-input v-model.number="courseInfoForm.classroom" placeholder="请输入上课地点"></el-input>-->
        <el-cascader
            placeholder="请选择教室"
            :options="teachingBuildingAndClassroom"
            v-model="courseInfoForm.classroom"
            filterable
            style="width: 100%"
        />
      </el-form-item>
    </el-row>
    <el-row>
      <el-form-item label="周次" prop="week">
        <div style="display: inline-block">
          <el-select
              v-model="courseInfoForm.week"
              multiple
              collapse-tags
              collapse-tags-tooltip
              clearable="true"
              placeholder="请选择上课周次"
              style="width: 100%"
          >
            <el-option
                v-for="item in getWeeks"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </div>
      </el-form-item>
      <!--        上课时间-->
      <el-form-item label="上课时间" prop="courseTimeList">
        <el-cascader
            :options="courseTime"
            :props="props"
            v-model="courseInfoForm.courseTimeList"
            ref="courseTimeList"
            collapse-tags
            collapse-tags-tooltip
            clearable
            placeholder="请选择上课时间"
        />
      </el-form-item>
    </el-row>

    <!--          选课容量-->
    <el-form-item label="选课容量" prop="capacity">
      <el-input v-model.number="courseInfoForm.capacity" placeholder="请输入课程容量"></el-input>
    </el-form-item>
    <el-form-item label="课程描述" prop="description">
      <el-input v-model="courseInfoForm.description" placeholder="请输入课程描述"></el-input>
    </el-form-item>
    <el-button @click="cancelSubmit">取消</el-button>
    <el-button type="primary" @click="confirmSubmit">提交</el-button>
  </el-form>
</template>

<script>
import {ElLoading, ElMessage} from "element-plus";

export default {
  inject:['reload'],
  name: "addCourseForm",
  props:{
    ifAdminOperating:Boolean,
    course_info_form_from_parent:Object,
  },
  computed:{
    //设置为18周
    getWeeks(){
      let weeks=[];
      const weekNum=18;
      for(let i=1;i<=weekNum;i++){
        let week={
          label:'',
          value:0,
        }
        week.label='第'+i+'周';
        week.value=i;
        weeks.push(week);
      }
      return weeks;
    },
    courseInfoForm(){
      return this.course_info_form_from_parent
    },
    defaultInstituteAndMajor(){
      return [this.courseInfoForm.instituteId,this.courseInfoForm.majorId]
    },
    defaultAbleToSelectInstituteAndMajor(){
      return this.courseInfoForm.availableMajorList;
    },
    defaultClassroom(){
      // console.log(this.courseInfoForm)
      return [this.courseInfoForm.timetableList[0].buildingId,this.courseInfoForm.timetableList[0].classroomId];
    },
    defaultWeek(){
      let tempWeek=[];
      this.courseInfoForm.timetableList.forEach(value => {
        if(!tempWeek.includes(value.week)){tempWeek.push(value.week)}
      })
      return tempWeek;
    },
    defaultCourseTimeList(){
      let tempCourseTimeList=[];
      this.courseInfoForm.timetableList.forEach(value => {
        value.sessions.forEach(val => {
          let item=[value.day,val];
          tempCourseTimeList.push(item);
        })
      })
      return tempCourseTimeList;
    }

  },
  created() {
    // console.log(this.allClassAbleToSelectTypes)
    ElLoading.service();
    //只有管理员才需要获取所有的教师信息
    //教师申请时不需要获取所有的教师信息
    if(this.ifAdminOperating){
      //获取所有的教师信息
      this.axios({
        method:'GET',
        url:'http://121.37.98.3:9090/users',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        }
      }).then(res=>{
        // console.log(res.data)
        res.data.data.forEach((value)=>{
              switch (value.identity){
                case "学生":
                  break;
                case "教师":
                  this.teacherData.push(value);
                  break;
              }
            }
        )
      });
    }
    //设置申请人的id等
    else{
      this.courseInfoForm.teacherId = this.$store.state.uuid
    }
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
        let weeks=['周一','周二','周三','周四','周五']
        for(let i=0;i<weeks.length;i++){
          let weekTime={
            label:'',
            value:0,
            children:[]
          }
          weekTime.label=weeks[i];
          weekTime.value=i+1;
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
      data.forEach((value)=>{
        let building={
          label:'',
          value:'',
          children: []
        }
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
    //获取专业和学院
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/institutes',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      // console.log(res.data)
      res.data.data.forEach((value)=>{
        let itemList={
          label: '',
          value: '',
          children:[]
        }
        value.majors.forEach((value)=>{
          let item={
            label:'',
            value:'',
          }
          item.label=value.name;
          item.value=value.majorId;
          itemList.children.push(item);
        })
        itemList.label=value.name;
        itemList.value=value.instituteId;
        this.instituteAndMajor.push(itemList);
      })
      // console.log(this.instituteAndMajor)
      const loading = ElLoading.service();
      loading.close()
    });
    // this.initData()
  },
  data(){
    const validateTeacherName = (rule, value, callback) =>{
      //只有管理员才需要校验是否输入教师合格
      if(this.ifAdminOperating){
        if(this.getTeacherId()==='false'){
          return callback(new Error("未找到该教师"));
        }else{
          if(this.getTeacherId()==='离职'){
            return callback(new Error("该教师已离职"));
          }
        }
        return callback()
      }
      //教师自己操作时不需要校验,因为已经定死了
      else{
        return callback()
      }
    }

    const validateNumber = (rule, value, callback) =>{
     if(value<=0){
       return callback(new Error("数字必须大于0"));
     }
     return callback();
    }
    return{
      teacherData:[],
      courseTime:[],
      test:'',
      teachingBuildingAndClassroom:[],
      instituteAndMajor:[],
      availableTypeId:"",
      availableTypes:[
        {label:'通用课程',availableTypeId:'1'},
        {label:'专业课程',availableTypeId:'2'},
        {label:'面向部分专业课程',availableTypeId:'3'},
      ],
      years:[
        {label:'2010-2011'},
        {label:'2011-2012'},
        {label:'2012-2013'},
        {label:'2013-2014'},
        {label:'2014-2015'},
        {label:'2015-2016'},
        {label:'2016-2017'},
        {label:'2017-2018'},
        {label:'2018-2019'},
        {label:'2019-2020'},
        {label:'2020-2021'},
        {label:'2021-2022'},
        {label:'2022-2023'},
      ],
      terms:[
        {label:'春'},
        {label:'秋'}
      ],
      props: {
        multiple: true,
      },
      verificationRules:{
        name:[
          {required: true,message: '请输入课程名称',trigger: 'change'}
        ],
        institute_major:[
          {required:true,message:"请选择院系与专业",trigger:'change',type:Array}
        ],
        year:[
          {required:true,message:"请选择开课年份",trigger:'change'}
        ],
        term:[
          {required:true,message:"请选择开课学期",trigger:'change'}
        ],
        ableToSelectInfo1:[
          {required:true,message:"请选择可选专业信息",trigger:'change',type:Array}
        ],
        number:[
          {required: true,message: '请输入课程编号',trigger: 'change'}
        ],
        classHour:[
          {required: true,message: '请输入学时',trigger: 'change'},
          {pattern: /^[0-9]*$/,message: '请输入数字'},
          {validator: validateNumber, trigger:['change', 'blur']}
        ],
        credit:[
          {required: true,message: '请输入课程学分',trigger: 'change'},
          {pattern: /^[0-9]*$|^\d+(\.\d+)?$/,message: '请输入数字'},
          {validator: validateNumber, trigger:['change', 'blur']}
        ],
        teacherName:[
          {required: true,message: '请输入教师姓名',trigger: 'change'},
          {validator: validateTeacherName, trigger:['change', 'blur']},
        ],
        classroom:[
          {required: true,message: '请选择教室',trigger: 'change',type:Array}
        ],
        week:[
          {required: true,message: '请选择周次',trigger: 'change',type:Array}
        ],
        courseTimeList:[
          {required: true,message: '请选择上课节次',trigger: 'change',type:Array}
        ],
        capacity:[
          {required: true,message: '请输入课程容量',trigger: 'change'},
          {pattern: /^[0-9]*$/,message: '请输入数字'},
          {validator: validateNumber, trigger:['change', 'blur']}
        ],
        description:[
          {required: true,message: '请输入课程描述',trigger: 'change'}
        ]
      }
    }
  },
  methods:{
    editInstituteAndMajor(value){
      console.log(value)
      this.courseInfoForm.instituteId = value[0]
      this.courseInfoForm.majorId = value[1]
    },
    editAbleToSelectInstituteAndMajor(value){
      //转为后端需要的数据格式
      let availableMajorIdList = []
      for(let i = 0;i < value.length;i++) {
        availableMajorIdList.push(value[i][1])
      }
      this.courseInfoForm.availableMajorIdList = availableMajorIdList
      this.courseInfoForm.availableMajorList = value
      // console.log(this.courseInfoForm)
    },
    getTeacherId(){
      let id='false';
      this.teacherData.forEach(value => {
        if(value.username===this.courseInfoForm.teacherName){
          if(value.status==='在岗'){
            id=value.uuid;
          }else {
            id=value.status;
          }
        }
      })
      return id;
    },
    getTeacherIdFormParent(teacherName){
      let id='false';
      this.teacherData.forEach(value => {
        if(value.username===teacherName){
          if(value.status==='在岗'){
            id=value.uuid;
          }else {
            id=value.status;
          }
        }
      })
      return id;
    },
    getMajorId(majorName){
      let id='false';
      this.instituteAndMajor.forEach(value => {
        value.children.forEach(val=>{
          if(majorName===val.label){
            id=val.value;
          }
        })
      })
      return id;

    },
    cancelSubmit(){
      // 清空表单
      this.$refs["addCourseForm"].resetFields();
      this.$emit("cancelSubmit",)
    },
    getTimetableList(){
      let tableList=[];
      let tableListItems=[];
      for(let i=1;i<=5;i++){
        let tableListItem={
          week:this.courseInfoForm.week,
          day:i,
          sessions:[],
          classroomId:this.courseInfoForm.classroom[1],
        }
        tableListItems.push(tableListItem);
      }
      this.courseInfoForm.courseTimeList.forEach(value => {
        tableListItems[value[0]-1].sessions.push(value[1]);
      })
      tableListItems.forEach(value => {
        if(value.sessions.length>0){
          tableList.push(value);
        }
      })
      return tableList;
    },
    getClassroomId(classroom){
      let id='false';
      this.teachingBuildingAndClassroom.forEach(value => {
        value.children.forEach(val=>{
          if(classroom===val.label){
            id=val.value;
          }
        })
      })
      return id;
    },
    getTimetableListFormParent(week,classroomId,courseTimeList){
      let tableList=[];
      let tableListItems=[];
      for(let i=1;i<=5;i++){
        let tableListItem={
          week:week,
          day:i,
          sessions:[],
          classroomId:classroomId,
        }
        tableListItems.push(tableListItem);
      }
      courseTimeList.forEach(value => {
        tableListItems[value[0]-1].sessions.push(value[1]);
      })
      tableListItems.forEach(value => {
        if(value.sessions.length>0){
          tableList.push(value);
        }
      })
      return tableList;
    },

    confirmSubmit(){
      this.$refs.addCourseForm.validate(valid =>{
        //表单数据是符合规则的
        if(valid){
          //将可选专业id转为后端需要的形式
          if(typeof (this.courseInfoForm.availableMajorIdList)=="undefined"&&typeof (this.courseInfoForm.availableMajorList)!="undefined"){
            let availableMajorIdList = []
            for(let i = 0;i < this.courseInfoForm.availableMajorList.length;i++) {
              availableMajorIdList.push(this.courseInfoForm.availableMajorList[i][1])
            }
            this.courseInfoForm.availableMajorIdList = availableMajorIdList
          }
          //面向部分专业时专业不可为空
          if(this.courseInfoForm.availableTypeId==='3'&&this.courseInfoForm.availableMajorIdList.length===0) {
            ElMessage.error({
              message: "表单数据有误,请检查!",
              grouping: true
            })
            return
          }
          this.courseInfoForm.timetableList = this.getTimetableList();
          // console.log(this.courseInfoForm.timetableList)
          this.courseInfoForm.teacher = this.getTeacherId();
          // console.log(this.getTeacherId())
          this.$emit('user-submit',this.courseInfoForm)
        }
        else{
          ElMessage.error({
            message: "表单数据有误,请检查!",
            grouping: true
          })
        }//end 表单数据不合格
      })
    },
  }
}
</script>

<style scoped>

</style>
