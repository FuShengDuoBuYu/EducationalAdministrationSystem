import { createApp } from 'vue'
import axios from 'axios'
import VueAxios from "vue-axios";
import App from './App.vue'
import router from './router/index'
import store from './assets/store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './util/StatusCheck.js'

//初始化localstorage中的token
if (localStorage.getItem('token') == null){
    localStorage.setItem('token', '')
}
// Vue.prototype.GLOBAL = global
const app = createApp(App)
app.use(ElementPlus)
app.use(store)
app.use(router)
app.use(VueAxios, axios)
app.mount('#app')
