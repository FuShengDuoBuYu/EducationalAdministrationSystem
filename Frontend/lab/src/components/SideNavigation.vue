<template>
  <div style="margin-right: 1%;display: flex">
    <el-menu
        default-active="this.$route.path" router
        background-color="#F8FFFF"
        style="width: 200px;min-height: 80vh"
        v-if="!ifHideSideBar"
    >
      <!--      传入的数组有三个参数
                  1.icon 是图标组件
                  2.标题头
                  3.跳转路由-->
      <el-menu-item v-for="(item,index) in itemList" :index="item.index" style="width: 200px">
        <div id=index :style="selectItemStyle(index)" @click="handleChoose(index)" >
          <el-icon><component :is="item.icon"></component></el-icon>
          <span>{{item.text}}</span>
        </div>
      </el-menu-item>

    </el-menu>
    <div v-if="!(this.$route.path==='/home')" class="hiddenSideBar" @click="ifHideSideBar = !ifHideSideBar"></div>
  </div>

</template>

<script>
export default {
  name: "SideNavigation",
  data(){
    return{
      ifHideSideBar:false,
      isActiveList:[]
    }
  },

  methods:{
    selectItemStyle(index){
      return this.isActiveList[index]
          ?{'width': '100%','text-align': 'left','color':'#409EFF'}
          :{'width': '100%','text-align': 'left'};
    }
  },
  props:{
    itemList:{
      type:Array,
      default(){
        return [];
      }
    }
  },
  created() {
    this.itemList.forEach((value,index) => {
      this.isActiveList.push(value.index === this.$route.path);
    })
  }
}
</script>

<style scoped>

.hiddenSideBar {
  width: 8px;
  height: 100%;
  background-color: #e9e9eb;
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
}

</style>

