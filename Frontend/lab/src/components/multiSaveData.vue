<template>
  <slot></slot>
  <upload-filled style="width: 35%;height: 35%;margin-top: 15%"/>
  <el-upload ref="upload" accept="csv" :limit="1" :auto-upload="false" :on-change="dealUplaodFiles" :on-remove="removeFile" style="margin-top: 20%">
    <template #trigger>
      <el-button type="primary" @click="dealUplaodFiles">上传csv文件</el-button>
    </template>
  </el-upload>
  <el-button @click="saveUsers" >提交</el-button>
</template>

<script>
import {csvParse} from "@/util/CsvPrase";
import {Search,UploadFilled} from "@element-plus/icons"
export default {
  name: "multiSaveData",
  components:{Search,UploadFilled},
  data(){
    return{
      multiData:[]
    }
  },
  methods:{
    //批量导入用户信息
    saveUsers(){
      this.$emit('saveData');
    },
    dealUplaodFiles(file){
      let fileType=file.name.split('.').pop()
      if(!(fileType==='csv')){
        // console.log('-------------------------')
        this.$message({
          message:"上传文件类型错误，请重新上传"
        })
        this.$refs['upload'].clearFiles();
        return ;
      }
      csvParse(file.raw).then((results)=>{
        this.multiData=[];
        this.multiData.push(...results.data);
        // console.log(this.multiData[0].week)
        // console.log(JSON.parse(this.multiData[0].week))
        console.log(this.multiData)
      })
    },
    removeFile(){
      this.multiData=[];
    }
  }
}
</script>

<style scoped>

</style>
