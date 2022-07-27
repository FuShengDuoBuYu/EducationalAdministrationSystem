<template>
  <el-table :data="userInfoList" style="width:100%" border  height="70vh"
            :header-cell-style="{background:'#79bbff', color:'#fff'}"
            :row-class-name="tableRowClassName"
  >
    <el-table-column prop="jobNum" label="学工号" />
    <el-table-column prop="username" label="姓名"  />
    <el-table-column prop="idcardNum" label="身份证号" />
    <el-table-column prop="phoneNum" label="电话"  />
    <el-table-column prop="institute" label="学院" />
    <el-table-column prop="major" label="专业" />
    <el-table-column prop="email" label="邮箱" />
    <el-table-column prop="status" label="用户状态"/>
    <el-table-column fixed="right" label="操作">
      <template #header>
        <el-input v-model="search" size="small" placeholder="搜索" />
      </template>
      <template #default="scope">
        <el-popconfirm title="是否确认删除该用户信息?" @confirm="deleteClick(scope.$index)">
          <template #reference>
            <el-button type="text" size="small">删除</el-button>
          </template>
        </el-popconfirm>
        <el-button type="text" size="small" @click="editClick(scope.$index)">编辑</el-button>
      </template>
    </el-table-column>
  </el-table>

</template>
<script>
import {ElMessage} from "element-plus";

export default {
  inject:['reload'],
  name: "ShowUersInfo",
  props:{
    userInfoList:{
      type:Array,
      default() {
        return [];
      }
    }

  },
  methods:{
    deleteClick(index){
      this.$emit('deleteItem',index);
      this.axios({
        url:'http://121.37.98.3:9090/users',
        method:'delete',
        headers:{
          'token':localStorage.getItem("token"),
          'Context-Type':'application/json'
        },
        data:{
          jobNum:this.userInfoList[index].jobNum,
          identity:this.userInfoList[index].identity
        }

      }).then((res)=>{
        ElMessage.success({
          message:'已删除'+this.userInfoList[index].username,
          duration:2000
        })
        this.reload();
      })
    },
    editClick(index){
      this.$emit("editItem",index);
    },
    //根据用户状态显示背景色
    tableRowClassName({row,rowIndex}){
      if(row.status==="在读"||row.status==="在岗"){
        return 'activeUser'
      }
      else{
        return 'notActiveUser'
      }
    }
  }
}

</script>

<style scoped>
  /deep/ .activeUser{
    background: #f0f9eb;
  }
  /deep/ .notActiveUser{
    background: #fdf6ec;
  }
</style>
