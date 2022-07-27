package com.se.lab2_backend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.se.lab2_backend.common.TimetableRequest;
import com.se.lab2_backend.common.VerifyToken;
import com.se.lab2_backend.entity.Admin;
import com.se.lab2_backend.entity.Course;
import com.se.lab2_backend.entity.Schedule;
import com.se.lab2_backend.mapper.AvailableTypeMapper;
import com.se.lab2_backend.mapper.CourseMapper;
import com.se.lab2_backend.mapper.TimetableMapper;
import com.se.lab2_backend.service.*;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.se.lab2_backend.util.ConstVariable.IDENTITY_ADMIN;
import static com.se.lab2_backend.util.ConstVariable.REQUEST_FAIL;

@Api(description = "管理员操作课程资源接口")
@RestController
public class AdminCourseController {
    private AdminService adminService;
    private StudentService studentService;
    private TeacherService teacherService;
    private MajorService majorService;
    private StatusService statusService;
    private InstituteService instituteService;
    private ClassroomService classroomService;
    private BuildingService buildingService;
    private CourseService courseService;
    private CourseMapper courseMapper;
    private TimetableMapper timetableMapper;
    private AcademicService academicService;
    private AvailableTypeMapper availableTypeMapper;

    @Autowired
    public AdminCourseController(AdminService adminService, StudentService studentService, TeacherService teacherService, MajorService majorService, StatusService statusService, InstituteService instituteService, ClassroomService classroomService, BuildingService buildingService, CourseService courseService, CourseMapper courseMapper, TimetableMapper timetableMapper, AcademicService academicService, AvailableTypeMapper availableTypeMapper) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.majorService = majorService;
        this.statusService = statusService;
        this.instituteService = instituteService;
        this.classroomService = classroomService;
        this.buildingService = buildingService;
        this.courseService = courseService;
        this.courseMapper = courseMapper;
        this.timetableMapper = timetableMapper;
        this.academicService = academicService;
        this.availableTypeMapper = availableTypeMapper;
    }

    /**
     * "scheduleList": List<Schedule>
     * */
    //管理员修改时间安排
    @ApiOperation(value = "管理员修改时间安排")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @PostMapping("/schedules")
    public Response<?> setSchedule(@RequestBody JSONObject body){
        List<Schedule> scheduleList = JSON.parseArray(JSON.toJSONString(body.get("scheduleList")), Schedule.class);
        return courseService.saveSchedule(scheduleList);
    }

    /**
     * "adminId": String
     * "courseSelection": bool
     * */
    //修改选课开关
    @ApiOperation(value = "修改选课开关")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PutMapping("/admin/course")
    public Response<?> setCourseSelection(@RequestBody JSONObject body){
        String uuid = (String) body.get("adminId");
        boolean courseSelection = (boolean) body.get("courseSelection");
        Admin admin = adminService.getAdminByUuid(uuid);
        admin.setCourseSelection(courseSelection);
        if(adminService.saveAdmin(admin)!=null) return new Response<>(true, ConstVariable.REQUEST_SUCCESS);
        else return new Response<>(false, REQUEST_FAIL);
    }

    /**
     * "adminId": String
     * "courseSelectionRound": String
     * */
    //修改选课轮次
    @ApiOperation(value = "修改选课轮次")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PutMapping("/admin/course/round")
    public Response<?> setCourseSelectionRound(@RequestBody JSONObject body){
        String uuid = (String) body.get("adminId");
        String courseSelectionRound = (String) body.get("courseSelectionRound");
        return academicService.setCourseSelectionRound(uuid, courseSelectionRound);
    }

    /**
     * "adminId": String
     * "currentYear": String
     * "currentTerm": String
     * */
    //设置学期
    @ApiOperation(value = "设置学期")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PutMapping("/admin/term")
    public Response<?> setCurrentTerm(@RequestBody JSONObject body){
        String uuid = (String) body.get("adminId");
        String currentYear = (String) body.get("currentYear");
        String currentTerm = (String) body.get("currentTerm");
        Admin admin = adminService.getAdminByUuid(uuid);
        admin.setCurrentYear(currentYear);
        admin.setCurrentTerm(currentTerm);
        if(adminService.saveAdmin(admin)!=null) return new Response<>(true, ConstVariable.REQUEST_SUCCESS);
        else return new Response<>(false, REQUEST_FAIL);
    }

    //管理员获取全部课程信息
    @ApiOperation(value = "管理员获取全部课程信息")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @GetMapping("/courses")
    public Response<?> getCourses(){
        return new Response<>(true, ConstVariable.getInfoSuccess("课程"), courseService.getCourseList());
    }

    /**
     * "courseId": String
     * */
    //查看某门课选课名单
    @ApiOperation(value = "查看某门课选课名单")
    @VerifyToken(identity = {ConstVariable.IDENTITY_ADMIN, ConstVariable.IDENTITY_TEACHER})
    @GetMapping("/courses/students/{courseId}")
    public Response<?> getCourseAndStudentsByCourseId(@PathVariable String courseId){
        JSONObject data = new JSONObject();
        data.put("students", academicService.getStudentsByCourseId(courseId));
        data.put("course", courseService.getCourseVOByCourseId(courseId));
        return new Response<>(true, ConstVariable.getInfoSuccess("课程选课信息"), data);
    }

    /**
     * "courseId": String
     * */
    //查看某门课选课名单
    @ApiOperation(value = "根据课程id某门课详细信息")
    @VerifyToken()
    @GetMapping("/courses/{courseId}")
    public Response<?> getCourseByCourseId(@PathVariable String courseId){
//        JSONObject data = new JSONObject();
//        data.put("course", courseService.getCourseVOByCourseId(courseId));
        return new Response<>(true, ConstVariable.getInfoSuccess("课程选课信息"), courseService.getCourseVOByCourseId(courseId));
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
     *             "timetableList" : List<TimetableRequest> timetableList
     *
     *             "availableTypeId" : String availableTypeId,
     *             "availableMajorIdList" : List<String> availableMajorIdList
     *             "year" : String year,
     *             "term" : String term
     * */
    //管理员添加课程
    @ApiOperation(value = "管理员添加课程")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @PostMapping("/courses")
    public Response<?> saveCourses(@RequestBody JSONObject body){
        Course course = new Course(
                (String) body.get("number"),
                (String) body.get("name"),
                (Integer) body.get("classHour"),
                Float.parseFloat((String) body.get("credit")),
                (Integer) body.get("capacity"),
                (String) body.get("description"),
                majorService.getMajorByMajorId((String) body.get("majorId")),
                teacherService.getTeacherByUuid((String) body.get("teacherId")),
                availableTypeMapper.findAvailableTypeByAvailableTypeId((String) body.get("availableTypeId")),
                (String) body.get("year"),
                (String) body.get("term")
        );
        course.setFinished(false);
        List<TimetableRequest> timetableList = JSON.parseArray(JSON.toJSONString(body.get("timetableList")), TimetableRequest.class);
        course.setIdentifier(courseService.getIdentifier(course.getNumber()));
        List<String> availableMajorIdList = JSON.parseArray(JSON.toJSONString(body.get("availableMajorIdList")), String.class);
        return courseService.saveCourse(course, timetableList, availableMajorIdList);
    }

    //管理员批量增加课程
    // TODO: 检查lab4新增的字段
    @ApiOperation(value = "管理员批量增加课程")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @PostMapping("/courses/batch")
    public Response<?> batchSaveCourses(@RequestBody JSONObject body){
        List<JSONObject> courseList=JSON.parseArray(JSON.toJSONString(body.get("list")),JSONObject.class);
        List<Course> validCourses=new ArrayList<>();
        List<Course> invalidCourses=new ArrayList<>();
        List<List<TimetableRequest>> timetableLists=new ArrayList<>();
        for(JSONObject rawCourse:courseList) {
            Course course = new Course(
                    (String) rawCourse.get("number"),
                    (String) rawCourse.get("name"),
                    (Integer) rawCourse.get("classHour"),
                    Float.valueOf((String) rawCourse.get("credit")),
                    (Integer) rawCourse.get("capacity"),
                    (String) rawCourse.get("description"),
                    majorService.getMajorByMajorId((String) rawCourse.get("majorId")),
                    teacherService.getTeacherByUuid((String) rawCourse.get("teacherId"))
            );

            List<TimetableRequest> timetableList = JSON.parseArray(JSON.toJSONString(rawCourse.get("timetableList")), TimetableRequest.class);
            if(courseService.timeIsConflict("",timetableList)){
                course.setIdentifier(courseService.getIdentifier(course.getNumber()));

                invalidCourses.add(course);

            }
            else{
                course.setIdentifier(courseService.getIdentifier(course.getNumber()));
                validCourses.add(course);
                timetableLists.add(timetableList);

            }
        }
        courseService.saveAll(validCourses,timetableLists);
        if(invalidCourses.size()==0){return new Response<>(true,"所有课程添加成功");}
        else{
            JSONObject res=new JSONObject();
            res.put("时间冲突的课程",invalidCourses);
            return new Response<>(false,"部分课程时间产生冲突",res);
        }
    }

    /**
     *             "courseId" : String courseId,
     *             "number" : String number,
     *             "name" : String name,
     *             "classHour" : int classHour,
     *             "credit" : float credit,
     *             "capacity" : int capacity,
     *             "description" : String description,
     *             "majorId" : String majorId,
     *             "teacherId" : String teacherId,
     *             "timetableList" : List<TimetableRequest> timetableList
     *
     *             "availableTypeId" : String availableTypeId,
     *             "availableMajorIdList" : List<String> availableMajorIdList
     *             "year" : String year,
     *             "term" : String term
     * */
    //管理员修改课程信息
    @ApiOperation(value = "管理员修改课程信息")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @PutMapping("/courses")
    public Response<?> alterCourses(@RequestBody JSONObject body){
        Course course = courseService.getCourseByCourseId((String) body.get("courseId"));
        course.setNumber((String) body.get("number"));
        course.setName((String) body.get("name"));
        course.setClassHour((Integer) body.get("classHour"));
        course.setCredit(Float.parseFloat((String) body.get("credit")));
        course.setCapacity((Integer) body.get("capacity"));
        course.setDescription((String) body.get("description"));
        course.setMajor(majorService.getMajorByMajorId((String) body.get("majorId")));
        course.setTeacher(teacherService.getTeacherByUuid((String) body.get("teacherId")));
        course.setAvailableType(availableTypeMapper.findAvailableTypeByAvailableTypeId((String) body.get("availableTypeId")));
        course.setYear((String) body.get("year"));
        course.setTerm((String) body.get("term"));

        List<TimetableRequest> timetableList = JSON.parseArray(JSON.toJSONString(body.get("timetableList")), TimetableRequest.class);
        List<String> availableMajorIdList = JSON.parseArray(JSON.toJSONString(body.get("availableMajorIdList")), String.class);
        return courseService.updateCourse(course, timetableList, availableMajorIdList);
//        return new Response<>(true, "11");
    }


    //管理员结课
    @ApiOperation(value = "管理员结课")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @PutMapping("/courses/finish/{courseId}")
    public Response<?> finishCourses(@PathVariable String courseId){
        Course course = courseService.getCourseByCourseId(courseId);
        return academicService.finishCourse(course);
    }

    /**
     * "courseId": String
     * */
    //管理员删除课程
    @ApiOperation(value = "管理员删除课程")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @DeleteMapping("/courses")
    public Response<?> deleteCourses(@RequestBody JSONObject body){
        String courseId = (String) body.get("courseId");
        return courseService.deleteCourseByCourseId(courseId);
//        Integer delete=courseService.deleteCourseByCourseId(courseId);
//        return new Response<>(delete==1,delete==1?"删除课程成功":"删除课程失败");
    }

    //管理员获取课程申请信息
    @ApiOperation(value = "管理员获取课程申请信息")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @GetMapping("/courses/applications")
    public Response<?> getCourseApplications(){
        return new Response<>(true, ConstVariable.getInfoSuccess("课程申请"), courseService.getCourseApplicationList());
    }

    /**
     * @RequestParam(value = "applicationId") String applicationId,
     * */
    //管理员同意课程申请
    @ApiOperation(value = "管理员同意课程申请")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @PutMapping("/courses/applications/accept")
    public Response<?> acceptCourseApplication(@RequestBody JSONObject body){
        String applicationId = (String) body.get("applicationId");
        return courseService.handleApplication(applicationId, ConstVariable.ACCEPT_APPLICATION_RESULT);
    }

    /**
     * @RequestParam(value = "applicationId") String applicationId,
     * */
    //管理员拒绝课程申请
    @ApiOperation(value = "管理员拒绝课程申请")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @PutMapping("/courses/applications/refuse")
    public Response<?> refuseCourseApplication(@RequestBody JSONObject body){
        String applicationId = (String) body.get("applicationId");
        return courseService.handleApplication(applicationId, ConstVariable.REFUSE_APPLICATION_RESULT);
    }

    //获取全部选课申请
    @ApiOperation(value = "获取全部选课申请")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @GetMapping("/courses/student/applications")
    public  Response<?> getSelectCourseApplication(){
        return academicService.getAllSelectCourseApplication();
    }

    /**
     * @RequestParam(value = "applicationId") String applicationId,
     * */
    //管理员同意选课申请
    @ApiOperation(value = "管理员同意选课申请")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @PutMapping("/courses/student/applications/accept")
    public Response<?> acceptCourseSelectionApplication(@RequestBody JSONObject body){
        String applicationId = (String) body.get("applicationId");
//        return courseService.handleApplication(applicationId, ConstVariable.ACCEPT_APPLICATION_RESULT);
        return academicService.handleSelectionApplication(applicationId, ConstVariable.ACCEPT_APPLICATION_RESULT);
    }

    /**
     * @RequestParam(value = "applicationId") String applicationId,
     * */
    //管理员拒绝选课申请
    @ApiOperation(value = "管理员拒绝选课申请")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @PutMapping("/courses/student/applications/refuse")
    public Response<?> refuseCourseSelectionApplication(@RequestBody JSONObject body){
        String applicationId = (String) body.get("applicationId");
//        return courseService.handleApplication(applicationId, ConstVariable.REFUSE_APPLICATION_RESULT);
        return academicService.handleSelectionApplication(applicationId, ConstVariable.REFUSE_APPLICATION_RESULT);
    }
}
