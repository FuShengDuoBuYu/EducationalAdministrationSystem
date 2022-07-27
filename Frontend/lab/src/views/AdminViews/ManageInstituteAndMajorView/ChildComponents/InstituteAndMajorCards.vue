<template>
  <div style="height: 75vh">
    <el-slider v-model="size" />
    <el-space wrap :size="size">
      <el-card v-for="(item,index) in instituteAndMajor" :index="item.index" class="box-card" style="width: 300px">
        <template #header>
          <div class="card-header" style="float: none">
<!--            展示院系-->
            <span>{{item.name}}</span>
            <el-button style="float: right" type="primary" size="small" @click="editInstitute(item)">操作</el-button>
          </div>
        </template>
        <div>
<!--          展示专业-->
          <el-table :show-header="false" :data="item.majors" height="100px">
            <el-table-column prop="name" label="专业名" />
            <!--            编辑操作按钮-->
            <el-table-column label="编辑操作" width="35px">
              <template #default="scope">
                <el-button type="primary" style="width: 20px;height: 20px" circle @click="editMajor(scope.$index,index)">
                  <el-icon><Edit /></el-icon>
                </el-button>
              </template>
            </el-table-column>
<!--            删除操作按钮-->
            <el-table-column label="删除操作" width="35px">
              <template #default="scope">
                <el-popconfirm title="是否确认删除该专业信息?" @confirm="deleteMajor(scope.$index,index)">
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

<!--  处理院系的dialog-->
  <el-dialog v-model="ifShowEditInstitute">
    <template #title>
      <span>{{currentEditInstitute}}</span>
    </template>
    <el-tabs style="height: 200px" :show-close="false" :destroy-on-close="true">
      <el-tab-pane label="新增专业">
        <el-input style="margin-top: 5%" placeholder="请输入要新增的专业名" v-model="addNewMajorName"></el-input>
        <el-button type="primary" style="margin-top: 5%" @click="addNewMajor">确定新增</el-button>
      </el-tab-pane>
      <el-tab-pane label="修改院系名称">
        <el-input style="margin-top: 5%" placeholder="请输入要修改的学院名" v-model="editNewInstituteName"></el-input>
        <el-button type="primary" style="margin-top: 5%" @click="editNewInstitute">确定修改</el-button>
      </el-tab-pane>
      <el-tab-pane label="删除院系" >
        <el-button type="danger" style="margin-top: 10%" @click="confirmDelete">确定删除</el-button>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script>
import {ref} from 'vue'
import {ElLoading, ElMessage, ElMessageBox} from 'element-plus'
import {Edit,Delete} from "@element-plus/icons";
export default {
  inject:['reload'],
  name: "InstituteAndMajorCards",
  components: {Edit,Delete},
  data(){
    return{
      currentEditInstituteId:'',
      addNewMajorName:'',
      editNewInstituteName:'',
      ifShowEditInstitute : false,
      size:ref(20),
      instituteAndMajor:[]
    }
  },
  created() {
    ElLoading.service();
    //获取专业学院信息
    this.axios({
      method:'GET',
      url:'http://121.37.98.3:9090/institutes',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      }
    }).then(res=>{
      // console.log(res.data.data)
      this.instituteAndMajor.push(...res.data.data)
      const loading = ElLoading.service();
      loading.close()
    });
  },
  methods:{
    editInstitute(institute){
      console.log(institute)
      this.ifShowEditInstitute=true
      this.currentEditInstituteId =institute.instituteId
      this.editNewInstituteName = institute.name
    },
    editMajor(majorIndex,instituteIndex){
      ElMessageBox.prompt('院系:'+this.instituteAndMajor[instituteIndex].name+
          '  原专业名:'+this.instituteAndMajor[instituteIndex].majors[majorIndex].name, '修改专业名称', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then((newMajorName) => {
        // console.log(this.instituteAndMajor)
        // if(this.instituteAndMajor)
        //点击确定
        this.axios({
          method:'PUT',
          url:'http://121.37.98.3:9090/majors',
          headers:{
            'token':localStorage.getItem("token"),
            'Context-Type':'application/json'
          },
          data:{
            majorId:this.instituteAndMajor[instituteIndex].majors[majorIndex].majorId,
            newMajorName:newMajorName.value,
            newInstituteId:this.instituteAndMajor[instituteIndex].instituteId
          }
        }).then(res=>{
          if(res.data.success){
            ElMessage.success({
              message:'修改成功',
              duration:1500
            })
            this.ifShowEditInstitute=false
            this.reload()
          }
          else{
            ElMessage.error({
              message:res.data.message,
              duration:1500
            })
          }
        })
      })
    },
    deleteMajor(majorIndex,instituteIndex){
      this.axios({
        method:'DELETE',
        url:'http://121.37.98.3:9090/majors',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          majorId:this.instituteAndMajor[instituteIndex].majors[majorIndex].majorId
        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:'已删除',
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
    confirmDelete(){
      ElMessageBox.confirm(
              '是否确认删除该学院?',
              '确认删除?', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                center: true,
              }
      ).then(() => {
        this.axios({
          method:'DELETE',
          url:'http://121.37.98.3:9090/institutes',
          headers:{
            'token':localStorage.getItem("token"),
            'Context-Type':'application/json'
          },
          data:{
            instituteId:this.currentEditInstituteId
          }
        }).then(res=>{
          if(res.data.success){
            ElMessage.success({
              message:'删除成功',
              duration:1500
            })
            this.ifShowEditInstitute=false
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
        this.ifShowEditInstitute=false
      })
    },
    editNewInstitute(){
      this.axios({
        method:'PUT',
        url:'http://121.37.98.3:9090/institutes',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          instituteId:this.currentEditInstituteId,
          newInstituteName:this.editNewInstituteName
        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:' 修改成功',
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
    addNewMajor(){
      this.axios({
        method:'POST',
        url:'http://121.37.98.3:9090/majors',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          majorName:this.addNewMajorName,
          instituteId:this.currentEditInstituteId
        }
      }).then(res=>{
        if(res.data.success){
          ElMessage.success({
            message:' 添加成功',
            duration:1500
          })
          this.ifShowEditInstitute=false
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
  }
}
</script>

<style scoped>

</style>
