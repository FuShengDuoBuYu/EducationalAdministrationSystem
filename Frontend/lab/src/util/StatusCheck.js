import axios from 'axios'
import store from '../assets/store'
import router from '../router/index'
import {ElMessage} from "element-plus";

// ...mapMutations({
//     //this.setName('cooldream');   即可将值变为cooldream
//     setUserNum:'SET_userNum',
//     setUserStatus:'SET_userStatus'
//   })
// ...mapActions({
//     //this.asyncSetName();   即可调用commit('setUsername')
//     asyncSetNum:'setUserNum',
//     asyncSetStatus:'setUserStatus'
// })




export function logout(e){
  localStorage.setItem("token", "")
  store.commit('SET_jobNum', "")
  store.commit('SET_username', "")
  store.commit('SET_identity', "")
  store.commit('SET_passwordChange', true)
  store.commit('SET_IdcardNum', "")
  store.commit('SET_Major', "")
  store.commit('SET_Email', "")
  store.commit('SET_PhoneNum', "")
  store.commit('SET_Institute', "")
  store.commit('SET_Status', "")
  // console.log(store.state)
  ElMessage({
    message: e,
    type: 'success'
  })
  router.push('index')
}

//Promise语法保证了 调用方会等待函数运行结束之后再进行接下来的操作(then语句)
//该方法向后端发送了检查token的请求，如果成功会更新token，失败会返回相关失败信息
export function onCheck(){
  return new Promise(function(resolve, reject) {
    axios({
      method:'post',
      url: 'http://121.37.98.3:9090/load',
      headers:{
        'token':localStorage.getItem("token"),
        'Context-Type':'application/json'
      },
    }).then(res =>{
      if(res.data.success){
        //设置token与密码标志
        localStorage.setItem("token", res.data.data.token)
        store.commit('SET_passwordChange', res.data.data.passwordChange)
        //更新用户基本信息
        refreshStore(res.data.data.userInfo)
      }
      resolve(res.data)
    })
  })
}

export function refreshStore(userTotalInfo){
  console.log(userTotalInfo)
  store.commit('SET_jobNum', userTotalInfo.jobNum)
  store.commit('SET_username', userTotalInfo.username)
  store.commit('SET_IdcardNum', userTotalInfo.idcardNum)
  store.commit('SET_Institute', userTotalInfo.institute)
  store.commit('SET_Status', userTotalInfo.status)
  store.commit('SET_Major', userTotalInfo.major)
  store.commit('SET_Email', userTotalInfo.email)
  store.commit('SET_identity', userTotalInfo.identity)
  store.commit('SET_PhoneNum', userTotalInfo.phoneNum)
  store.commit('SET_StatusId',userTotalInfo.statusId)
  store.commit('SET_InstituteId',userTotalInfo.instituteId)
  store.commit('SET_MajorId',userTotalInfo.majorId)
  store.commit('SET_Uuid',userTotalInfo.uuid)
}
