import { createStore } from 'vuex'

const store = createStore({
    state: {
        jobNum: '',
        username: '',
        identity: "",
        passwordChange: true,
        email: "",
        idcardNum: "",
        institute: "",
        major: "",
        phoneNum: "",
        status: "",
        instituteId:"",
        majorId:"",
        statusId:"",
        uuid:"",
        teacherData:[],
    },
    getters:{
        getJobNum: state => state.jobNum,
        getUserName: state => state.username,
        getIdentity: state => state.identity,
        getPasswordChange: state => state.passwordChange,
        getEmail: state => state.email,
        getIdcardNum: state => state.idcardNum,
        getInstitute: state => state.institute,
        getMajor: state => state.major,
        getPhoneNum: state => state.phoneNum,
        getStatus: state => state.status,
        getInstituteId: state=>state.instituteId,
        getMajorId: state=>state.majorId,
        getStatusId: state=>state.statusId,
        getUuid: state=>state.uuid,
        getTeacherData: state=>state.teacherData
    },
    mutations: {
        SET_jobNum(state, value){
            state.jobNum = value;
        },
        SET_username(state, value){
            state.username = value;
        },
        SET_identity(state, value){
            state.identity = value;
        },
        SET_Email(state, value){
            state.email = value;
        },
        SET_IdcardNum(state, value){
            state.idcardNum = value;
        },
        SET_Institute(state, value){
            state.institute = value;
        },
        SET_Major(state, value){
            state.major = value;
        },
        SET_PhoneNum(state, value){
            state.phoneNum = value;
        },
        SET_Status(state, value){
            state.status = value;
        },
        SET_passwordChange(state, value){
            state.passwordChange = value;
        },
        SET_InstituteId(state,value){
            state.instituteId = value;
        },
        SET_MajorId(state,value){
            state.majorId = value;
        },
        SET_StatusId(state,value){
            state.statusId = value;
        },
        SET_Uuid(state,value){
            state.uuid = value;
        },
        SET_TeacherData(state,value){
            state.teacherData = value;
        },
    },
    actions: {
        setjobNum(content){
            content.commit('SET_jobNum')
        },
        setUsername(content){
            content.commit('SET_username')
        },
        setIdentity(content){
            content.commit('SET_identity')
        },
        setPasswordChange(content){
            content.commit('SET_passwordChange')
        },
        setEmail(content){
            content.commit('SET_Email')
        },
        setIdcardNum(content){
            content.commit('SET_IdcardNum')
        },
        setInstitute(content){
            content.commit('SET_Institute')
        },
        setMajor(content){
            content.commit('SET_Major')
        },
        setPhoneNum(content){
            content.commit('SET_PhoneNum')
        },
        setStatus(content){
            content.commit('SET_Status')
        },
        setInstituteId(content){
            content.commit('SET_InstituteId')
        },
        setMajorId(content){
            content.commit('SET_MajorId')
        },
        setStatusId(content){
            content.commit('SET_StatusId')
        },
        setUuid(content){
            content.commit('SET_Uuid')
        },
        setTeacherData(content){
            content.commit('SET_TeacherData')
        }
    }
})

export default store
