<template>
  <div v-if="invalidRoute">
    <center> <h1>...</h1> </center>
    <hr/>
  </div>
  <router-view v-if="isRouterAlive"></router-view>
</template>
<script type="text/babel">
  export default {
    name: 'App',
    computed: {
      invalidRoute () {
        return !this.$route.matched || this.$route.matched.length === 0;
      }
    },
    created () {
      // 在页面加载时读取sessionStorage
      if (sessionStorage.getItem('store')) {
        this.$store.replaceState(Object.assign({}, this.$store.state, JSON.parse(sessionStorage.getItem('store'))))
      }
      // 在页面刷新时将store保存到sessionStorage里
      window.addEventListener('beforeunload', () => {
        sessionStorage.setItem('store', JSON.stringify(this.$store.state))
      })
    },
    provide () {    //父组件中通过provide来提供变量，在子组件中通过inject来注入变量。
      return {
        reload: this.reload
      }
    },
    data() {
      //用于静默刷新
      return{
        isRouterAlive: true                    //控制视图是否显示的变量
      }
    },
    methods: {
      //用于静默刷新页面
      reload () {
        this.isRouterAlive = false;            //先关闭，
        this.$nextTick(function () {
          this.isRouterAlive = true;         //再打开
        })
      }
    },
  };
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
