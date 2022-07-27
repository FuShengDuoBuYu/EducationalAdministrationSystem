package com.se.lab2_backend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.se.lab2_backend.common.TimetableRequest;
import com.se.lab2_backend.common.VerifyToken;
import com.se.lab2_backend.entity.Course;
import com.se.lab2_backend.entity.CourseApplication;
import com.se.lab2_backend.entity.Schedule;
import com.se.lab2_backend.mapper.AvailableTypeMapper;
import com.se.lab2_backend.mapper.CourseMapper;
import com.se.lab2_backend.mapper.StudentCourseMapper;
import com.se.lab2_backend.mapper.TimetableMapper;
import com.se.lab2_backend.service.*;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.se.lab2_backend.util.ConstVariable.IDENTITY_ADMIN;
import static com.se.lab2_backend.util.ConstVariable.IDENTITY_TEACHER;

@Api(description = "公共资源接口")
@RestController
public class PublicResourcesController {
    private AdminService adminService;
    private TeacherService teacherService;
    private StudentService studentService;
    private MajorService majorService;
    private CourseService courseService;
    private CourseMapper courseMapper;
    private TimetableMapper timetableMapper;
    private InstituteService instituteService;
    private BuildingService buildingService;
    private ClassroomService classroomService;
    private AvailableTypeMapper availableTypeMapper;

    @Autowired
    public PublicResourcesController(AdminService adminService, TeacherService teacherService, StudentService studentService, MajorService majorService, CourseService courseService, CourseMapper courseMapper, TimetableMapper timetableMapper, InstituteService instituteService, BuildingService buildingService, ClassroomService classroomService, AvailableTypeMapper availableTypeMapper) {
        this.adminService = adminService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.majorService = majorService;
        this.courseService = courseService;
        this.courseMapper = courseMapper;
        this.timetableMapper = timetableMapper;
        this.instituteService = instituteService;
        this.buildingService = buildingService;
        this.classroomService = classroomService;
        this.availableTypeMapper = availableTypeMapper;
    }

    //获取所有学院信息
    @ApiOperation(value = "获取所有学院信息")
    @VerifyToken(identity = {IDENTITY_ADMIN,IDENTITY_TEACHER})
    @GetMapping("/institutes")
    public Response<?> testInstitutes(){
        return new Response<>(true, ConstVariable.getInfoSuccess("专业院系"), instituteService.getAll());
    }

    //获取所有专业信息
    @ApiOperation(value = "获取所有专业信息")
    @VerifyToken(identity = {IDENTITY_ADMIN,IDENTITY_TEACHER})
    @GetMapping("/majors")
    public Response<?> testMajors(){
        return new Response<>(true, ConstVariable.getInfoSuccess("专业"), majorService.getAll());
    }

    //获取所有教学楼教室信息
    @ApiOperation(value = "获取所有教学楼教室信息")
    @VerifyToken()
    @GetMapping("/buildings")
    public Response<?> testBuildings(){
        return new Response<>(true, ConstVariable.getInfoSuccess("教学楼教室"), buildingService.getAll());
    }

    //获取所有教室信息
    @ApiOperation(value = "获取所有教室信息")
    @VerifyToken()
    @GetMapping("/classrooms")
    public Response<?> testClassrooms(){
        return new Response<>(true, ConstVariable.getInfoSuccess("教室"), classroomService.getAll());
    }

    //获取选课开关
    @ApiOperation(value = "获取选课开关")
    @VerifyToken()
    @GetMapping("/admin/course")
    public Response<?> getCourseSelection(){
        return new Response<>(true, ConstVariable.getInfoSuccess("选课开关"), adminService.getCourseSelection());
    }

    //获取当前学期
    @ApiOperation(value = "获取当前学期")
    @VerifyToken()
    @GetMapping("/term")
    public Response<?> getTerm(){
        JSONObject data = new JSONObject();
        List<String> list = adminService.getCurrentTerm();
        data.put("year", list.get(0));
        data.put("term", list.get(1));
        return new Response<>(true, ConstVariable.getInfoSuccess("当前学期"), data);
    }

    //获取时间表
    @ApiOperation(value = "获取时间表")
    @VerifyToken()
    @GetMapping("/schedules")
    public Response<?> getSchedule(){
        return new Response<>(true, ConstVariable.getInfoSuccess("时间表"), courseService.getSchedule());
    }

    //获取申请类型
    @ApiOperation(value = "获取申请类型")
    @VerifyToken(identity = {ConstVariable.IDENTITY_TEACHER, ConstVariable.IDENTITY_ADMIN})
    @GetMapping("/applications/types")
    public Response<?> getApplicationType(){
        return new Response<>(true, ConstVariable.getInfoSuccess("申请类型"), courseService.getApplicationType());
    }

    //获取申请结果
    @ApiOperation(value = "获取申请结果")
    @VerifyToken(identity = {ConstVariable.IDENTITY_TEACHER, ConstVariable.IDENTITY_ADMIN})
    @GetMapping("/applications/results")
    public Response<?> getApplicationResult(){
        return new Response<>(true, ConstVariable.getInfoSuccess("申请结果"), courseService.getApplicationResult());
    }

    //获取可选专业类型
    @ApiOperation(value = "获取可选专业类型")
    @VerifyToken(identity = {ConstVariable.IDENTITY_TEACHER, ConstVariable.IDENTITY_ADMIN})
    @GetMapping("/courses/permission")
    public Response<?> getPermissionType(){
        return new Response<>(true, ConstVariable.getInfoSuccess("可选专业类型"), availableTypeMapper.findAll());
    }

//    @GetMapping("/jsondebug")
//    public Response<?> testJson(){
//        return new Response<>(true, ConstVariable.getInfoSuccess("json"), courseService.getTimetableList(courseMapper.findAll().get(0)));
//    }

    @ApiOperation(value = "coursesdebug", hidden = true)
    @GetMapping("/coursesdebug")
    public Response<?> getCoursesDebug(){
        return new Response<>(true, ConstVariable.getInfoSuccess("课程"), courseMapper.findAll());
    }

//    @PostMapping("/coursesdebug")
//    public Response<?> saveCourses(
//            @RequestParam(value = "number") String number,
//            @RequestParam(value = "name") String name,
//            @RequestParam(value = "classHour") int classHour,
//            @RequestParam(value = "credit") int credit,
//            @RequestParam(value = "capacity") int capacity,
//            @RequestParam(value = "majorId") String majorId,
//            @RequestParam(value = "teacherId") String teacherId
//    ){
//        Course course = new Course(number, name, classHour, credit, capacity, majorService.getMajorByMajorId(majorId), teacherService.getTeacherByUuid(teacherId));
//        course.setIdentifier(courseService.getIdentifier(course.getNumber()));
//        return courseService.saveCourseTest(course);
//    }
    @ApiOperation(value = "timetablesdebug", hidden = true)
    @GetMapping("/timetablesdebug")
    public Response<?> getTimetablesDebug(){
        return new Response<>(true, ConstVariable.getInfoSuccess("时间表"), timetableMapper.findAll());
    }

    @ApiOperation(value = "listDebug", hidden = true)
    @PostMapping("/listDebug")
    public Response<?> listDebug(@RequestBody() JSONObject j){
        List<TimetableRequest> timetableRequests = JSON.parseArray(JSON.toJSONString(j.get("timetableList")), TimetableRequest.class);
//                (List<TimetableRequest>) j.get("timetableList");
        return new Response<>(true, "ss", timetableRequests.get(0));
    }
}
