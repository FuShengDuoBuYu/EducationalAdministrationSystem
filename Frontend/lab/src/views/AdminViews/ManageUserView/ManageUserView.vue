<template>
  <!--  顶部导航栏-->
    <HeaderNavigation/>
  <div style="display: flex">
    <!--    侧边栏-->
    <admin-side-navigation/>
    <!--    标签栏-->
    <el-tabs v-model="activeTabName" type="card" style="width: 100%">
      <el-tab-pane label="学生用户信息" name="studentInfo">
        <div style="width: 100%;text-align: center">
          <!--                只显示一页的内容-->
          <show-users-info :user-info-list="getStudentData" @edit-item="editStudentItem"></show-users-info>
          <!--          修改学生信息的dialog-->
          <el-dialog v-model="ifShowChangeStudentInfoDialog" destroy-on-close :close-on-press-escape="false" :close-on-click-modal="false" :show-close="false">
            <add-user-form v-bind:if-add-user="false"
                           v-bind:form-data="studentData[currentStudentIndex]"
                           v-bind:institute-and-major="instituteAndMajor"
                           v-bind:statuses="studentStatus"
                           @cancel-submit="ifShowChangeStudentInfoDialog=false"
                           key="changeStudent"
            />
          </el-dialog>
          <el-pagination
              v-model:currentPage="studentCurrentPage"
              v-model:page-size="studentPageSize"
              :page-sizes="[5,10,20]"
              :small="false"
              :background="true"
              layout="total, sizes, prev, pager, next, jumper"
              :total="studentData.length"
              :default-page-size="10"

          />
        </div>
      </el-tab-pane>
      <el-tab-pane label="教师用户信息" name="teacherInfo">
        <div style="width: 100%;text-align: center">
          <!--                只显示一页的内容-->
          <show-users-info :user-info-list="getTeacherData" @delete-item="deleteTeacherItem" @edit-item="editTeacherItem"></show-users-info>
          <el-dialog v-model="ifShowChangeTeacherInfoDialog">
            <add-user-form v-bind:if-add-user="false"
                           v-bind:form-data="teacherData[currentTeacherIndex]"
                           v-bind:institute-and-major="instituteAndMajor"
                           v-bind:statuses="teacherStatus"
                           key="changeTeacher"
                           @cancel-submit="ifShowChangeTeacherInfoDialog=false"
            />
          </el-dialog>
          <el-pagination
              v-model:currentPage="teacherCurrentPage"
              v-model:page-size="teacherPageSize"
              :page-sizes="[5,10,20]"
              :small="false"
              :background="true"
              layout="total, sizes, prev, pager, next, jumper"
              :total="teacherData.length"
              :default-page-size="10"

          />
        </div>
      </el-tab-pane>
      <el-tab-pane label="查找用户" name="findUsers">
        <div style="display: flex;margin-left: 5%;margin-right: 5%">
          <el-input v-model="searchKey" placeholder="支持学工号,身份证号等查找" clearable />
          <el-button><el-icon><Search></Search></el-icon></el-button>
        </div>
      </el-tab-pane>
      <el-tab-pane label="添加用户" name="addUsers">
        <div style="display: flex">
          <div style="width: 69%">
            <h5>单个用户录入</h5>
            <add-user-form v-bind:form-data="form"
                           v-bind:institute-and-major="instituteAndMajor"
                           v-bind:if-add-user="true"
                           v-bind:statuses=[]
                           key="addUser"

            />
          </div>
          <el-divider direction="vertical" style="width: 1%;min-height: 70vh"/>
          <div style="width: 30%">
            <multi-save-data ref="multidata" @save-data="saveUsers">
              <template v-slot>
                <h5>批量用户导入</h5>
              </template>
            </multi-save-data>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import HeaderNavigation from "@/components/HeaderNavigation";
import SideNavigation from "@/components/SideNavigation";
import {ElLoading, ElMessage} from "element-plus";
import AdminSideNavigation from "../AdminSideNavigation";
import ShowUsersInfo from "./childComps/ShowUsersInfo";
import {Search,UploadFilled} from "@element-plus/icons";
import addUserForm from "@/views/AdminViews/ManageUserView/childComps/addUserForm";
import multiSaveData from "../../../components/multiSaveData";
export default {
  name: "ManageUserView",
  inject:['reload'],
  components: {
    addUserForm,
    Search,
    ShowUsersInfo,
    AdminSideNavigation,
    HeaderNavigation,
    SideNavigation,
    UploadFilled,
    multiSaveData
  },
  data() {
    return {
      activeTabName:"studentInfo",
      //学生页面
      studentCurrentPage:1,
      studentPageSize:10,
      studentData:[],
      currentStudentIndex:'',
      ifShowChangeStudentInfoDialog:false,
      studentStatus:[],
      //老师页面
      teacherCurrentPage:1,
      teacherPageSize:10,
      teacherData:[],
      currentTeacherIndex:'',
      ifShowChangeTeacherInfoDialog:false,
      teacherStatus:[],
      //搜索key
      searchKey:"",
      labelPosition : 'left',
      dialogAddFormVisible: false,
      form:{
        email: "",
        idcardNum: "",
        identity: "",
        institute: "",
        instituteId: "",
        jobNum: "",
        major: "",
        majorId: "",
        phoneNum: "",
        status: "",
        statusId: "",
        username: "",
        uuid: "",
      },
      //新增用户
      instituteAndMajor:[],
      //批量导入数据
    };
  },
  created() {
    ElLoading.service();
    //获取老师和学生信息
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/users',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      // console.log(res.data.data)
      res.data.data.forEach((value)=>{
        switch (value.identity){
          case "学生":
            this.studentData.push(value);
            break;
          case "教师":
            this.teacherData.push(value);
            break;
        }
      }
      )
    });
    //获取专业院系信息
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/institutes',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
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
    });
    //获取用户总状态信息
    this.axios({
      method:"GET",
      url:'http://121.37.98.3:9090/statuses',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      // console.log(res.data.data)
      this.studentStatus=(res.data.data["学生"])
      // console.log(this.studentStatus)
      this.teacherStatus=(res.data.data["教师"])
    });
    const loading = ElLoading.service();
    loading.close()
  },
  computed:{
    getStudentData(){
      return this.studentData.filter((value,index)=>{
        if(index>=(this.studentCurrentPage-1)*this.studentPageSize&&index<this.studentCurrentPage*this.studentPageSize){
          return value;
        }
      })
    },
    getTeacherData(){
      return this.teacherData.filter((value,index)=>{
        if(index>=(this.teacherCurrentPage-1)*this.teacherPageSize&&index<this.teacherCurrentPage*this.teacherPageSize){
          return value;
        }
      })
    }
  },

  methods: {
    //学生
    editStudentItem(index){
      this.ifShowChangeStudentInfoDialog = true;
      this.currentStudentIndex=index+(this.studentCurrentPage-1)*this.studentPageSize
    },
    editTeacherItem(index){
      this.currentTeacherIndex=index+(this.teacherCurrentPage-1)*this.teacherPageSize;
      this.ifShowChangeTeacherInfoDialog = true;
    },
    //批量导入用户信息
    saveUsers(){
      this.axios({
        method:"post",
        url: 'http://121.37.98.3:9090/users/batch',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          list:this.$refs['multidata'].multiData
        },
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message: res.data.message,
            grouping: true
          });
          this.reload()
        }else{
          ElMessage.error({
            message: res.data.message,
            grouping: true
          })
        }
      })
    },
  }
}
</script>

<style scoped>
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
.demo-pagination-block + .demo-pagination-block {
  margin-top: 10px;
}
.demo-pagination-block .demonstration {
  margin-bottom: 16px;
}
</style>
