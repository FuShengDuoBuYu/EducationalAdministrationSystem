package com.se.lab2_backend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.se.lab2_backend.common.UserVO;
import com.se.lab2_backend.common.VerifyToken;
import com.se.lab2_backend.entity.Admin;
import com.se.lab2_backend.entity.Student;
import com.se.lab2_backend.entity.Teacher;
import com.se.lab2_backend.service.*;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import com.se.lab2_backend.util.TokenUtil;
//import jdk.nashorn.internal.parser.JSONParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Api(description = "登录鉴权相关接口")
@RestController
public class SystemController {

    private AdminService adminService;
    private StudentService studentService;
    private TeacherService teacherService;
    private MajorService majorService;
    private StatusService statusService;
    private InstituteService instituteService;

    @Autowired
    public SystemController(AdminService adminService, StudentService studentService, TeacherService teacherService, MajorService majorService, StatusService statusService, InstituteService instituteService) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.majorService = majorService;
        this.statusService = statusService;
        this.instituteService = instituteService;
    }

//    @ApiOperation("用户登录")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "jobNum", value = "学工号", dataType = "string", paramType = "body", example="220001"),
//        @ApiImplicitParam(name = "pwd", value = "密码", dataType = "string", paramType = "body", example="123456")
//    })
    /**
     * "jobNum" : "220001"
     * "pwd" : "123456"
     * */
    @ApiOperation(value = "登录接口")
    @PostMapping("/login")
    public Response<?> login(@RequestBody(required = true) JSONObject body){
        String jobNum = (String) body.get("jobNum");
        String pwd = (String) body.get("pwd");
        Response<?> res = null;
        //根据学工号去三个表里找用户
        Admin admin =adminService.getAdminByJobNum(jobNum);
        Response<?> adminResponse =  adminService.judgeLoginStatus(admin,pwd);
        Student student = studentService.getStudentByJobNum(jobNum);
        Response<?> studentResponse = studentService.judgeLoginStatus(student,pwd);
        Teacher teacher = teacherService.getTeacherByJobNum(jobNum);
        Response<?> teacherResponse = teacherService.judgeLoginStatus(teacher,pwd);
        //判断用户状态是否非法
        if(student!=null && !student.getStatus().getStatusId().equals(ConstVariable.INITIAL_STUDENT_STATUS)){
            return new Response<>(false,"您的登录权限已被关闭");

        }
        else if(teacher!=null && !teacher.getStatus().getStatusId().equals(ConstVariable.INITIAL_TEACHER_STATUS)){
            return new Response<>(false,"您的登录权限已被关闭");
        }

        //判断用户输入的信息是否有效
        res = userLoginStatus(adminResponse,teacherResponse,studentResponse);
        return res;
    }


    //用于判断用户登录状态的方法
    private Response<?> userLoginStatus(Response<?> adminResponse,Response<?> teacherResponse,Response<?> studentResponse){
        Response<?> res = null;
        //用户输入的学工号不正确
        if(adminResponse.getMessage().equals(ConstVariable.USER_NOT_EXIST)&&
            teacherResponse.getMessage().equals(ConstVariable.USER_NOT_EXIST)&&
            studentResponse.getMessage().equals(ConstVariable.USER_NOT_EXIST)
        ){
            res = new Response<>(false,ConstVariable.USER_NOT_EXIST,null);
        }
        //用户输入的学工号正确,但是密码不正确
        else if(adminResponse.getMessage().equals(ConstVariable.P_INVALID)||
                teacherResponse.getMessage().equals(ConstVariable.P_INVALID)||
                studentResponse.getMessage().equals(ConstVariable.P_INVALID)
        ){
            res = new Response<>(false,ConstVariable.P_INVALID,null);
        }

        if(res != null) return res;
        //用户输入的账号密码都正确,返回一个token
        //admin
        else if(adminResponse.isSuccess()){
            res = adminResponse;
        }
        //teacher
        else if(teacherResponse.isSuccess()){
            res = teacherResponse;
        }
        //student
        else {
            res = studentResponse;
        }
        return res;
    }

    @ApiOperation(value = "载入用户信息")
    @VerifyToken
    @PostMapping("load")
    public Response<?> loadIdentity(@RequestHeader(value = "token") String token){
        String uuid = JWT.decode(token).getAudience().get(0);
        JSONObject res = updateToken(uuid);
        return new Response<>(true, "载入用户信息成功！", res);
    }

    private JSONObject updateToken(String uuid) {
        JSONObject res = new JSONObject();
        Admin admin = adminService.getAdminByUuid(uuid);
        Student student = studentService.getStudentByUuid(uuid);
        Teacher teacher = teacherService.getTeacherByUuid(uuid);
        if(admin != null) {
            res.put("token", TokenUtil.getToken(admin.getUuid(), ConstVariable.IDENTITY_ADMIN, admin.getJobNum(), admin.getUsername(), admin.getPassword()));
            res.put("passwordChange" , !admin.getPassword().equals(ConstVariable.INITIAL_P));
            res.put("userInfo", new UserVO(
                    admin.getUuid(),
                    admin.getJobNum(),
                    admin.getUsername(),
                    admin.getIdcardNum(),
                    admin.getPhoneNum(),
                    admin.getEmail(),
                    ConstVariable.IDENTITY_ADMIN,
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""));
        } else if(student != null) {
            res.put("token", TokenUtil.getToken(student.getUuid(), ConstVariable.IDENTITY_STUDENT, student.getJobNum(), student.getUsername(), student.getPassword()));
            res.put("passwordChange" , !student.getPassword().equals(ConstVariable.INITIAL_P));
            res.put("userInfo", new UserVO(
                    student.getUuid(),
                    student.getJobNum(),
                    student.getUsername(),
                    student.getIdcardNum(),
                    student.getPhoneNum(),
                    student.getEmail(),
                    ConstVariable.IDENTITY_STUDENT,
                    student.getMajor().getName(),
                    student.getMajor().getMajorId(),
                    student.getMajor().getInstitute().getName(),
                    student.getMajor().getInstitute().getInstituteId(),
                    student.getStatus().getName(),
                    student.getStatus().getStatusId()));
        } else if(teacher != null) {
            res.put("token", TokenUtil.getToken(teacher.getUuid(), ConstVariable.IDENTITY_TEACHER, teacher.getJobNum(), teacher.getUsername(), teacher.getPassword()));
            res.put("passwordChange", !teacher.getPassword().equals(ConstVariable.INITIAL_P));
            res.put("userInfo", new UserVO(
                    teacher.getUuid(),
                    teacher.getJobNum(),
                    teacher.getUsername(),
                    teacher.getIdcardNum(),
                    teacher.getPhoneNum(),
                    teacher.getEmail(),
                    ConstVariable.IDENTITY_TEACHER,
                    teacher.getMajor().getName(),
                    teacher.getMajor().getMajorId(),
                    teacher.getMajor().getInstitute().getName(),
                    teacher.getMajor().getInstitute().getInstituteId(),
                    teacher.getStatus().getName(),
                    teacher.getStatus().getStatusId()));
        }
        return res;
    }

    //TODO: 实现对load函数降低负载
    private <T> JSONObject generateUserInfo(JSONObject res, T user, String identity) {
        return res;
    }

    //修改密码
    /**
     * "jobNum": "220001"
     * "pwd": "111aaa"
     * */
    @ApiOperation(value = "修改密码")
    @VerifyToken
    @PostMapping("/alter/pwd")
    public Response<?> alterPassword(@RequestBody JSONObject body){
        String jobNum = (String) body.get("jobNum");
        String pwd = (String) body.get("pwd");
        Response<?> res = null;
        JSONObject data = new JSONObject();
        String token = null;
        //根据学工号去三个表里找用户
        Admin admin =adminService.getAdminByJobNum(jobNum);
        Student student = studentService.getStudentByJobNum(jobNum);
        Teacher teacher = teacherService.getTeacherByJobNum(jobNum);
        //判断用户输入的信息是否有效
        if(admin != null){
            if(!admin.getPassword().equals(pwd)){
                admin.setPassword(pwd);
                adminService.saveAdmin(admin);
                token = TokenUtil.getToken(admin.getUuid(), ConstVariable.IDENTITY_ADMIN, admin.getJobNum(), admin.getUsername(), admin.getPassword());
            } else res = new Response<>(false, "新旧密码一致！修改无效！", null);
        } else if(student != null) {
            if(!student.getPassword().equals(pwd)){
                student.setPassword(pwd);
                studentService.saveStudent(student);
                token = TokenUtil.getToken(student.getUuid(), ConstVariable.IDENTITY_STUDENT, student.getJobNum(), student.getUsername(), student.getPassword());
            } else res = new Response<>(false, "新旧密码一致！修改无效！", null);
        } else if(teacher != null) {
            if(!teacher.getPassword().equals(pwd)){
                teacher.setPassword(pwd);
                teacherService.saveTeacher(teacher);
                token = TokenUtil.getToken(teacher.getUuid(), ConstVariable.IDENTITY_TEACHER, teacher.getJobNum(), teacher.getUsername(), teacher.getPassword());
            } else res = new Response<>(false, "新旧密码一致！修改无效！", null);
        } else {
            res = new Response<>(false, "未找到用户信息，无法修改密码！", null);
        }
        if(res != null) return res;
        data.put("token", token);
        data.put("passwordChange", true);
        return new Response<>(true, "成功修改密码！", data);
    }

    //修改个人信息
    /**
     * "jobNum": "220001"
     * "pwd": "111aaa"
     * */
    @ApiOperation(value = "修改个人信息")
    @VerifyToken
    @PostMapping("/alter/info")
    public Response<?> alterInfo(@RequestBody JSONObject body){
        String jobNum = (String) body.get("jobNum");
        String email = (String) body.get("email");
        String phoneNum = (String) body.get("phoneNum");
        JSONObject res = new JSONObject();
        String token = null;
        //根据学工号去三个表里找用户
        Admin admin =adminService.getAdminByJobNum(jobNum);
        Student student = studentService.getStudentByJobNum(jobNum);
        Teacher teacher = teacherService.getTeacherByJobNum(jobNum);
        //判断用户输入的信息是否有效
        if(admin != null){
            admin.setEmail(email);
            admin.setPhoneNum(phoneNum);
            adminService.saveAdmin(admin);
            token = TokenUtil.getToken(admin.getUuid(), ConstVariable.IDENTITY_ADMIN, admin.getJobNum(), admin.getUsername(), admin.getPassword());
        } else if(student != null) {
            student.setEmail(email);
            student.setPhoneNum(phoneNum);
            studentService.saveStudent(student);
            token = TokenUtil.getToken(student.getUuid(), ConstVariable.IDENTITY_STUDENT, student.getJobNum(), student.getUsername(), student.getPassword());
        } else if(teacher != null) {
            teacher.setEmail(email);
            teacher.setPhoneNum(phoneNum);
            teacherService.saveTeacher(teacher);
            token = TokenUtil.getToken(teacher.getUuid(), ConstVariable.IDENTITY_TEACHER, teacher.getJobNum(), teacher.getUsername(), teacher.getPassword());
        } else {
            return new Response<>(false, "未找到用户信息，无法修改信息！", null);
        }
        res.put("token", token);
        return new Response<>(true, "成功修改个人信息！", res);
    }

}
