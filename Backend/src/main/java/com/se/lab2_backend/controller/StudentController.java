package com.se.lab2_backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.se.lab2_backend.common.VerifyToken;
import com.se.lab2_backend.mapper.CourseMapper;
import com.se.lab2_backend.mapper.TimetableMapper;
import com.se.lab2_backend.service.*;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "学生操作相关接口")
@RestController
public class StudentController {
    TeacherService teacherService;
    StudentService studentService;
    MajorService majorService;
    CourseService courseService;
    AcademicService academicService;
    CourseMapper courseMapper;
    TimetableMapper timetableMapper;

    @Autowired
    public StudentController(TeacherService teacherService, StudentService studentService, MajorService majorService, CourseService courseService, AcademicService academicService, CourseMapper courseMapper, TimetableMapper timetableMapper) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.majorService = majorService;
        this.courseService = courseService;
        this.academicService = academicService;
        this.courseMapper = courseMapper;
        this.timetableMapper = timetableMapper;
    }

    //查看学生可选课程
    @ApiOperation(value = "查看学生可选课程")
    @VerifyToken(identity = ConstVariable.IDENTITY_STUDENT)
    @GetMapping("/courses/student/{majorId}")
    public Response<?> getCourseAvailableByMajorId(@PathVariable(name = "majorId") String majorId){
        if(courseService.getCourseSelection()) return new Response<>(true, ConstVariable.getInfoSuccess("课程"), courseService.getAvailableCourseListByMajorId(majorId));
        else return new Response<>(false, ConstVariable.COURSE_SELECTION_CLOSE);
    }

    //查看学生已选课程
    @ApiOperation(value = "查看学生已选课程")
    @VerifyToken(identity = ConstVariable.IDENTITY_STUDENT)
    @GetMapping("courses/student/selected/{studentId}")
    public Response<?> getSelectedCourses(@PathVariable String studentId){
        return academicService.getSelectedCourses(studentId);
    }

    //查看学生已修课程
    @ApiOperation(value = "查看学生已修课程")
    @VerifyToken(identity = ConstVariable.IDENTITY_STUDENT)
    @GetMapping("courses/student/finished/{studentId}")
    public Response<?> getFinishedCourses(@PathVariable String studentId){
        return academicService.getFinishedCourses(studentId);
    }

    //学生新增选课
    @ApiOperation(value = "学生新增选课")
    @VerifyToken(identity = ConstVariable.IDENTITY_STUDENT)
    @PostMapping("/courses/student/selected")
    public  Response<?> saveSelectedCourse(@RequestBody JSONObject body){
        return academicService.saveSelectedCourses((String) body.get("studentId"), (String) body.get("courseId"));
    }

    //学生退课
    @ApiOperation(value = "学生退课")
    @VerifyToken(identity = ConstVariable.IDENTITY_STUDENT)
    @DeleteMapping("/courses/student/selected")
    public Response<?> deleteSelectedCourse(@RequestBody JSONObject body){
        String studentId = (String) body.get("studentId");
        String courseId = (String) body.get("courseId");
        return academicService.deleteCourseByStudentIdAndCourseId(studentId, courseId);
    }

    //学生获取自己的选课申请
    @ApiOperation(value = "学生获取自己的选课申请")
    @VerifyToken(identity = ConstVariable.IDENTITY_STUDENT)
    @GetMapping("/courses/student/applications/{studentId}")
    public  Response<?> getStudentSelectCourseApplication(@PathVariable String studentId){
        return academicService.getAllSelectCourseApplicationByStudentId(studentId);
    }

    //学生提交选课申请
    @ApiOperation(value = "学生提交选课申请")
    @VerifyToken(identity = ConstVariable.IDENTITY_STUDENT)
    @PostMapping("/courses/student/applications")
    public  Response<?> saveSelectCourseApplication(@RequestBody JSONObject body){
        return academicService.saveSelectCourseApplication((String) body.get("studentId"), (String) body.get("courseId"), (String) body.get("reason"));
    }

}
