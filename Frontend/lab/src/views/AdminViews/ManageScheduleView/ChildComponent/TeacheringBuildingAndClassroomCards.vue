<template>
  <el-scrollbar style="height: 70vh">
    <div>
      <el-slider v-model="size" />
      <el-space wrap :size="size">
        <el-card v-for="(item,index) in teachingBuildingAndClassroom" :index="item.index" class="box-card" style="width: 300px">
          <template #header>
            <div class="card-header" style="float: none">
              <!--            展示教学楼名字-->
              <span>{{item.name}}</span>
              <el-button style="float: right" type="primary" size="small" @click="editTeachingBuilding(item)">操作</el-button>
            </div>
          </template>
          <div>
            <!--          展示教室-->
            <el-table :data="item.classrooms" height="300px">
              <el-table-column prop="name" label="教室名" />
              <el-table-column prop="capacity" label="容量" />
              <!--            编辑操作按钮-->
              <el-table-column label=" " width="35px">
                <template #default="scope">
                  <el-button type="primary" style="width: 20px;height: 20px" circle @click="editClassroom(scope.$index,index)">
                    <el-icon><Edit /></el-icon>
                  </el-button>
                </template>
              </el-table-column>
              <!--            删除操作按钮-->
              <el-table-column label=" " width="35px">
                <template #default="scope">
                  <el-popconfirm title="是否确认删除该教室?" @confirm="deleteClassroom(scope.$index,index)">
                    <template #reference>
                      <div style="display: flex">
                        <el-button type="danger" style="width: 20px;height: 20px" circle><el-icon><Delete/></el-icon></el-button>
                      </div>
                    </template>
                  </el-popconfirm>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-space>
    </div>
  </el-scrollbar>

  <!--  处理院系的dialog-->
  <el-dialog v-model="ifShowEditTeachingBuilding">
    <template #title>
      <span>{{editNewTeachingBuildingName}}</span>
    </template>
    <el-tabs style="height: 300px" :show-close="false" :destroy-on-close="true">
      <el-tab-pane label="新增教室">
        <el-row>
          <span>教室名</span>
          <el-input placeholder="请输入要新增的教室名" v-model="addNewClassroomName"></el-input>
        </el-row>
        <el-row style="margin-top: 5%">
          <span>教室容量</span>
          <el-input placeholder="请输入要新增的教室容量" v-model.number="addNewClassroomCapacity"></el-input>
        </el-row>
          <el-button type="primary" style="margin-top: 5%" @click="addNewClassroom">确定新增</el-button>
      </el-tab-pane>
      <el-tab-pane label="修改教学楼名称">
        <el-input style="margin-top: 5%" placeholder="请输入要修改的教学楼名" v-model="editNewTeachingBuildingName"></el-input>
        <el-button type="primary" style="margin-top: 5%" @click="editNewTeachingBuilding">确定修改</el-button>
      </el-tab-pane>
      <el-tab-pane label="删除教学楼" >
        <el-button type="danger" style="margin-top: 10%" @click="confirmDelete">确定删除</el-button>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>

<!--  修改教室信息的dialog-->
  <el-dialog v-model="ifShowEditClassroom" :destroy-on-close="true">
    <template #title>
      <span>修改教室信息</span>
    </template>
    <el-row>
      <span>教室名</span>
      <el-input style="width: 30%;margin: 1%" v-model="editClassroomName"></el-input>
      <span>教室容量</span>
      <el-input-number min="1" style="margin: 1%" v-model="editClassroomCapacity"></el-input-number>
    </el-row>
      <el-row style="margin-top: 5%">
      <el-button style="margin-left: 40%" @click="this.ifShowEditClassroom=false">取消</el-button>
      <el-button type="primary" @click="confirmSubmitEditClassroom">确定</el-button>
    </el-row>
  </el-dialog>
</template>

<script>
import {ref} from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {Edit,Delete} from "@element-plus/icons";
export default {
  inject:['reload'],
  name: "TeacheringBuildingAndClassroomCards",
  components: {Edit,Delete},
  data(){
    return{
      currentEditTeachingBuilding:'',
      addNewClassroomName:'',
      addNewClassroomCapacity:'',
      editClassroomName:'',
      editClassroomCapacity:'',
      editClassroomId:'',
      editNewTeachingBuildingName:'',
      ifShowEditTeachingBuilding : false,
      ifShowEditClassroom : false,
      size:ref(20),
      teachingBuildingAndClassroom:[]
    }
  },
  created() {
    //获取教学楼与教室信息
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/buildings',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      console.log(res.data.data)
      this.teachingBuildingAndClassroom.push(...res.data.data)
    });
  },
  methods:{
    editTeachingBuilding(teachingBuilding){
      this.ifShowEditTeachingBuilding=true
      this.currentEditTeachingBuilding =teachingBuilding
      this.editNewTeachingBuildingName = teachingBuilding.name
    },
    editClassroom(classroomIndex,teachingBuildingIndex){
      this.ifShowEditClassroom=true
      this.editClassroomCapacity=this.teachingBuildingAndClassroom[teachingBuildingIndex].classrooms[classroomIndex].capacity
      this.editClassroomName=this.teachingBuildingAndClassroom[teachingBuildingIndex].classrooms[classroomIndex].name
      this.editClassroomId=this.teachingBuildingAndClassroom[teachingBuildingIndex].classrooms[classroomIndex].classroomId
      this.currentEditTeachingBuilding = this.teachingBuildingAndClassroom[teachingBuildingIndex]
    },
    confirmSubmitEditClassroom(){
      this.axios({
        method:'PUT',
        url:'http://121.37.98.3:9090/classrooms',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          classroomId:this.editClassroomId,
          newClassroomName:this.editClassroomName,
          newClassroomCapacity:this.editClassroomCapacity,
          newBuildingId:this.currentEditTeachingBuilding.buildingId
        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:'修改成功',
            duration:1500
          })
          this.ifShowEditClassroom=false
          this.reload()
        }
        else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          })
        }
      })
    },
    deleteClassroom(classroomIndex,teachingBuildingIndex){
      this.axios({
        method:'DELETE',
        url:'http://121.37.98.3:9090/classrooms',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          classroomId:this.teachingBuildingAndClassroom[teachingBuildingIndex].classrooms[classroomIndex].classroomId
        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:'删除成功',
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
    },
    addNewClassroom() {
      this.axios({
        method: 'POST',
        url: 'http://121.37.98.3:9090/classrooms',
        headers: {
          'token': localStorage.getItem("token"),
          'Context-Type': 'application/json'
        },
        data: {
          classroomName: this.addNewClassroomName,
          capacity: this.addNewClassroomCapacity,
          buildingId: this.currentEditTeachingBuilding.buildingId
        }
      }).then(res => {
        if (res.data.success) {
          ElMessage.success({
            message:this.addNewClassroomName+'添加成功',
            duration:1500
          })
          this.ifShowEditTeachingBuilding=false
          this.reload()
        } else {
          ElMessage.error({
            message: res.data.message,
            duration: 1500
          })
        }
      })
    },
    confirmDelete(){
      ElMessageBox.confirm(
          '是否确认删除该教学楼?',
          '确认删除?', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            center: true,
          }
      ).then(() => {
        this.axios({
          method:'DELETE',
          url:'http://121.37.98.3:9090/buildings',
          headers:{
            'token':localStorage.getItem("token"),
            'Context-Type':'application/json'
          },
          data:{
            buildingId:this.currentEditTeachingBuilding.buildingId
          }
        }).then(res=>{
          if(res.data.success){
            ElMessage.success({
              message:'删除成功',
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
      }).catch(()=>{
        this.ifShowEditTeachingBuilding=false
      })
    },
    editNewTeachingBuilding(){
      this.axios({
        method:'PUT',
        url:'http://121.37.98.3:9090/buildings',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          buildingId:this.currentEditTeachingBuilding.buildingId,
          newBuilding:this.editNewTeachingBuildingName
        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:this.editNewTeachingBuildingName,
            duration:1500
          })
          this.ifShowEditTeachingBuilding=false
          this.reload()
        }
        else{
          ElMessage.error({
            message:res.data.message,
            duration:1500
          })
        }
      })
    },
  },
}
</script>

<style scoped>

</style>

