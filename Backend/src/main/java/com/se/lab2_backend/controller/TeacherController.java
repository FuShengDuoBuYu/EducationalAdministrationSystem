package com.se.lab2_backend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.se.lab2_backend.common.VerifyToken;
import com.se.lab2_backend.entity.CourseApplication;
import com.se.lab2_backend.mapper.ApplicationTypeMapper;
import com.se.lab2_backend.mapper.AvailableTypeMapper;
import com.se.lab2_backend.mapper.CourseMapper;
import com.se.lab2_backend.mapper.TimetableMapper;
import com.se.lab2_backend.service.CourseService;
import com.se.lab2_backend.service.MajorService;
import com.se.lab2_backend.service.StudentService;
import com.se.lab2_backend.service.TeacherService;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "教师操作相关接口")
@RestController
public class TeacherController {
    TeacherService teacherService;
    StudentService studentService;
    MajorService majorService;
    CourseService courseService;
    CourseMapper courseMapper;
    TimetableMapper timetableMapper;
    AvailableTypeMapper availableTypeMapper;

    @Autowired
    public TeacherController(TeacherService teacherService, StudentService studentService, MajorService majorService, CourseService courseService, CourseMapper courseMapper, TimetableMapper timetableMapper, AvailableTypeMapper availableTypeMapper) {
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.majorService = majorService;
        this.courseService = courseService;
        this.courseMapper = courseMapper;
        this.timetableMapper = timetableMapper;
        this.availableTypeMapper = availableTypeMapper;
    }

    //教师获取所开课程
    @ApiOperation(value = "教师获取所开课程")
    @VerifyToken(identity = ConstVariable.IDENTITY_TEACHER)
    @GetMapping("/courses/teacher/{teacherId}")
    public Response<?> getCourseByTeacherId(@PathVariable(name = "teacherId") String teacherId){
        return new Response<>(true, ConstVariable.getInfoSuccess("课程"), courseService.getCourseListByTeacherId(teacherId));
    }

    //教师获取自己的课程申请
    @ApiOperation(value = "教师获取自己的课程申请")
    @VerifyToken(identity = ConstVariable.IDENTITY_TEACHER)
    @GetMapping("/courses/applications/{teacherId}")
    public Response<?> getCourseApplicationByTeacherId(@PathVariable(name = "teacherId") String teacherId){
        return new Response<>(true, ConstVariable.getInfoSuccess("课程申请"), courseService.getCourseApplicationList(teacherId));
    }

    /**
     *             "number" : String number,
     *             "name" : String name,
     *             "classHour" : int classHour,
     *             "credit" : float credit,
     *             "capacity" : int capacity,
     *             "description" : String description,
     *             "majorId" : String majorId,
     *             "teacherId" : String teacherId,
     *             "timetableList" : List<TimetableRequest> timetableList,
     *             "applicationTypeId" : String applicationTypeId
     *             "courseId" : String courseId
     *
     *             "availableTypeId" : String availableTypeId,
     *             "availableMajorIdList" : List<String> availableMajorIdList
     *             "year" : String year,
     *             "term" : String term
     * */
    //教师提交课程申请
    @ApiOperation(value = "教师提交课程申请")
    @VerifyToken(identity = ConstVariable.IDENTITY_TEACHER)
    @PostMapping("/courses/applications")
    public Response<?> saveCourseApplications(@RequestBody JSONObject body){
        CourseApplication courseApplication = new CourseApplication(
                (String) body.get("courseId"),
                (String) body.get("number"),
                (String) body.get("name"),
                (Integer) body.get("classHour"),
                Float.parseFloat((String) body.get("credit")),
                (Integer) body.get("capacity"),
                (String) body.get("description"),
                majorService.getMajorByMajorId((String) body.get("majorId")),
                teacherService.getTeacherByUuid((String) body.get("teacherId")),
                courseService.getApplicationTypeById((String) body.get("applicationTypeId")),
                courseService.getApplicationResultById(ConstVariable.INITIAL_APPLICATION_RESULT),
                JSON.toJSONString(body.get("timetableList"))
        );
        courseApplication.setAvailableType(availableTypeMapper.findAvailableTypeByAvailableTypeId((String) body.get("availableTypeId")));
        courseApplication.setAvailableMajorIdList(JSON.toJSONString(body.get("availableMajorIdList")));
        courseApplication.setYear((String) body.get("year"));
        courseApplication.setTerm((String) body.get("term"));
        return courseService.saveCourseApplication(courseApplication);
//        return new Response<>(true, "test", courseApplication);
    }
}
