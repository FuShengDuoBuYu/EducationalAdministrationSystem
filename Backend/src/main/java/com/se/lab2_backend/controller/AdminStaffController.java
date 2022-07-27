package com.se.lab2_backend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.se.lab2_backend.common.TimetableRequest;
import com.se.lab2_backend.common.UserVO;
import com.se.lab2_backend.common.VerifyToken;
import com.se.lab2_backend.entity.*;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.se.lab2_backend.util.ConstVariable.*;

@Api(description = "管理员操作人员、专业、教室资源接口")
@RestController
public class AdminStaffController {

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

    @Autowired
    public AdminStaffController(AdminService adminService, StudentService studentService, TeacherService teacherService, MajorService majorService, StatusService statusService, InstituteService instituteService, ClassroomService classroomService, BuildingService buildingService, CourseService courseService, CourseMapper courseMapper, TimetableMapper timetableMapper) {
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
    }

    @ApiOperation(value = "管理员获取所有用户信息")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @GetMapping("/users")//管理员获取所有用户信息
    public Response<?> getUsers(){
        ArrayList<UserVO> list = new ArrayList<>();
        for(Student s: studentService.getAll()){
            list.add(new UserVO(
                    s.getUuid(),
                    s.getJobNum(),
                    s.getUsername(),
                    s.getIdcardNum(),
                    s.getPhoneNum(),
                    s.getEmail(),
                    ConstVariable.IDENTITY_STUDENT,
                    s.getMajor().getName(),
                    s.getMajor().getMajorId(),
                    s.getMajor().getInstitute().getName(),
                    s.getMajor().getInstitute().getInstituteId(),
                    s.getStatus().getName(),
                    s.getStatus().getStatusId()));
        }
        for(Teacher t: teacherService.getAll()){
            list.add(new UserVO(
                    t.getUuid(),
                    t.getJobNum(),
                    t.getUsername(),
                    t.getIdcardNum(),
                    t.getPhoneNum(),
                    t.getEmail(),
                    ConstVariable.IDENTITY_TEACHER,
                    t.getMajor().getName(),
                    t.getMajor().getMajorId(),
                    t.getMajor().getInstitute().getName(),
                    t.getMajor().getInstitute().getInstituteId(),
                    t.getStatus().getName(),
                    t.getStatus().getStatusId()));
        }
        return new Response<>(true, ConstVariable.getInfoSuccess("用户"), list);
    }

    /**
     *         @RequestParam(value = "username") String username,
     *         @RequestParam(value = "jobNum") String jobNum,
     *         @RequestParam(value = "idcardNum") String idcardNum,
     *         @RequestParam(value = "phoneNum") String phoneNum,
     *         @RequestParam(value = "email") String email,
     *         @RequestParam(value = "identity") String identity,
     *         @RequestParam(value = "major") String majorId,
     *         @RequestParam(value = "institute") String instituteId
     * */
    @ApiOperation(value = "管理员增加用户信息")
    @VerifyToken(identity = ConstVariable.IDENTITY_ADMIN)
    @PostMapping("/users")//管理员增加用户信息
    public Response<?> saveData(@RequestBody JSONObject body){
        String username = (String) body.get("username");
        String jobNum = (String) body.get("jobNum");
        String idcardNum = (String) body.get("idcardNum");
        String phoneNum = (String) body.get("phoneNum");
        String email = (String) body.get("email");
        String identity = (String) body.get("identity");
        String majorId = (String) body.get("major");
        String instituteId = (String) body.get("institute");
        Response<?> res = null;
        if (identity.length()==0){
            res = new Response<>(false,"不能识别用户角色");
        }
        if (teacherService.getTeacherByJobNum(jobNum)!=null || studentService.getStudentByJobNum(jobNum)!=null) res = new Response<>(false,"学工号已存在");
        if (teacherService.getTeacherByIdcardNum(idcardNum)!=null || studentService.getStudentByIdcardNum(idcardNum)!=null) res = new Response<>(false,"身份证号已存在");
        Major major = majorService.getMajorByMajorId(majorId);
        Institute institute = instituteService.getInstituteByInstituteId(instituteId);
        if (!major.getInstitute().getName().equals(institute.getName())) res = new Response<>(false,"专业学院信息不匹配！");
        if (res != null) return res;

        if(identity.equals(IDENTITY_TEACHER)){
            Teacher teacher = new Teacher(jobNum,username,idcardNum,phoneNum,email, ConstVariable.INITIAL_P, major, statusService.getStatusByStatusId(INITIAL_TEACHER_STATUS));
            return teacherService.saveTeacher(teacher);
        }else {
            Student student = new Student(jobNum,username,idcardNum,phoneNum,email,ConstVariable.INITIAL_P, major, statusService.getStatusByStatusId(INITIAL_STUDENT_STATUS));
            return studentService.saveStudent(student);
        }
    }

    @ApiOperation(value = "管理员批量增加用户信息")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PostMapping("/users/batch")
    public Response<?> batchSave(@RequestBody JSONObject body){

        List<JSONObject> users = JSON.parseArray(JSON.toJSONString(body.get("list")),JSONObject.class);
        //以下三个列表 保存了不同问题的用户类型
        List<JSONObject> jobNumDefectiveUsers = new ArrayList<>();
        List<JSONObject> idCardDefectiveUsers = new ArrayList<>();
        List<JSONObject> majorInstituteDefectiveUsers = new ArrayList<>();
        //以下两个列表 保存了格式正确的老师和学生的内容
        List<Teacher> teachers = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        //以下的遍历 把前端数据进行初步的解析，把每一行数据归档到以上五个列表
        for(JSONObject user:users){

            String username = (String) user.get("username");
            String jobNum = (String) user.get("jobNum");
            String idcardNum = (String) user.get("idcardNum");
            String phoneNum = (String) user.get("phoneNum");
            String email = (String) user.get("email");
            String identity = (String) user.get("identity");
            String majorName = (String) user.get("major");
            String instituteName = (String) user.get("institute");

            if (teacherService.getTeacherByJobNum(jobNum)!=null || studentService.getStudentByJobNum(jobNum)!=null) { jobNumDefectiveUsers.add(user); continue; }
            else if (teacherService.getTeacherByIdcardNum(idcardNum)!=null || studentService.getStudentByIdcardNum(idcardNum)!=null) { idCardDefectiveUsers.add(user); continue; }
            Major major = majorService.getMajorByName(majorName);
            Institute institute = instituteService.getInstituteByName(instituteName);
            if (!major.getInstitute().getName().equals(institute.getName())) { majorInstituteDefectiveUsers.add(user); continue; }

            if(identity.equals(IDENTITY_TEACHER)){
                Teacher teacher = new Teacher(jobNum,username,idcardNum,phoneNum,email, ConstVariable.INITIAL_P, major, statusService.getStatusByStatusId(INITIAL_TEACHER_STATUS));
                teachers.add(teacher);
            }else {
                Student student = new Student(jobNum,username,idcardNum,phoneNum,email,ConstVariable.INITIAL_P, major, statusService.getStatusByStatusId(INITIAL_STUDENT_STATUS));
                students.add(student);
            }
        }
        teacherService.saveAll(teachers);
        studentService.saveAll(students);

        if(jobNumDefectiveUsers.size()+idCardDefectiveUsers.size()+majorInstituteDefectiveUsers.size()==0) return new Response<>(true,"所有数据批量增加成功！");
        else {
            JSONObject res = new JSONObject();
            res.put("学工号重复用户", jobNumDefectiveUsers);
            res.put("身份证重复用户", idCardDefectiveUsers);
            res.put("专业院系不匹配用户", majorInstituteDefectiveUsers);
            return new Response<>(false,"部分数据有问题！请检查！",res);
        }

    }

    /**
     * @RequestParam(value = "jobNum") String jobNum,
     * @RequestParam(value = "identity") String identity
     * */
    @ApiOperation(value = "管理员删除用户信息")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @DeleteMapping("/users")//删除用户信息
    public Response<?> deleteUser(@RequestBody JSONObject body){
        String jobNum = (String) body.get("jobNum");
        String identity = (String) body.get("identity");
        Response<?> res = null;
        if (teacherService.getTeacherByJobNum(jobNum)==null && studentService.getStudentByJobNum(jobNum)==null) res = new Response<>(false,"学工号不存在");
        if (res != null) return res;
        if(identity.equals(IDENTITY_TEACHER)){
            Integer deleted = teacherService.deleteTeacherByJobNum(jobNum);
            res = new Response<>(deleted==1, deleted==1?"成功删除"+IDENTITY_TEACHER:"删除"+IDENTITY_TEACHER+"失败！");
        } else {
            Integer deleted = studentService.deleteStudentByJobNum(jobNum);
            res = new Response<>(deleted==1, deleted==1?"成功删除"+IDENTITY_STUDENT:"删除"+IDENTITY_STUDENT+"失败！");
        }
        return res;
    }

    /**
     * @RequestParam(value = "jobNum") String jobNum,
     * @RequestParam(value = "majorId") String majorId,
     * @RequestParam(value = "instituteId") String instituteId,
     * @RequestParam(value = "username") String username,
     * @RequestParam(value = "idcardNum") String idcardNum,
     * @RequestParam(value = "phoneNum") String phoneNum,
     * @RequestParam(value = "email") String email,
     * @RequestParam(value = "statusId") String statusId
     * */
    @ApiOperation(value = "管理员修改用户信息")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PutMapping("/users")//更改用户信息
    public Response<?> alterUser(@RequestBody JSONObject body){
        String jobNum = (String) body.get("jobNum");
        String majorId = (String) body.get("majorId");
        String instituteId = (String) body.get("instituteId");
        String username = (String) body.get("username");
        String idcardNum = (String) body.get("idcardNum");
        String phoneNum = (String) body.get("phoneNum");
        String email = (String) body.get("email");
        String statusId = (String) body.get("statusId");

//        Teacher teacherByIdcardNum = teacherService.getTeacherByIdcardNum(idcardNum);
//        Student studentByIdcardNum = studentService.getStudentByIdcardNum(idcardNum);
//        if (!Objects.equals(jobNum, teacherByIdcardNum.getJobNum()) && !Objects.equals(jobNum, studentByIdcardNum.getJobNum()))
//            return new Response<>(false,"身份证号已存在,请重新修改");
//        Response<?> res = null;
        //根据jobNum从表里找到对应的教师或者学生信息
        Student student = studentService.getStudentByJobNum(jobNum);
        Teacher teacher = teacherService.getTeacherByJobNum(jobNum);
        if(student!=null){
            student.setUsername(username);
            student.setIdcardNum(idcardNum);
            student.setPhoneNum(phoneNum);
            student.setEmail(email);
            student.setMajor(majorService.getMajorByMajorId(majorId));
            student.setStatus(statusService.getStatusByStatusId(statusId));
            return studentService.saveStudent(student);
        }else {
            teacher.setUsername(username);
            teacher.setIdcardNum(idcardNum);
            teacher.setPhoneNum(phoneNum);
            teacher.setEmail(email);
            teacher.setMajor(majorService.getMajorByMajorId(majorId));
            teacher.setStatus(statusService.getStatusByStatusId(statusId));
            return teacherService.saveTeacher(teacher);
        }
    }

    @ApiOperation(value = "管理员获取所有状态信息")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @GetMapping("/statuses")//获取所有状态信息
    public Response<?> getStatuses(){
        JSONObject res = new JSONObject();
        ArrayList<String> identities = new ArrayList<>(Arrays.asList(IDENTITY_STUDENT, IDENTITY_TEACHER));
        for(String i: identities){
            res.put(i, statusService.getAllNameByIdentity(i));
        }
        return new Response<>(true, ConstVariable.getInfoSuccess("状态"), res);
    }

    /**
     * @RequestParam(value = "instituteId") String instituteId
     * */
    //删除院系
    @ApiOperation(value = "删除院系")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @DeleteMapping("/institutes")
    public Response<?> deleteInstitute(@RequestBody JSONObject body){
        String instituteId = (String) body.get("instituteId");
        Integer delete=instituteService.deleteInstituteById(instituteId);
        return new Response<>(delete==1,delete==1?"删除院系成功":"删除院系失败，请检查是否存在该院系");
    }

    //增加院系
    @ApiOperation(value = "增加院系")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PostMapping("/institutes")
    public Response<?> addInstitute(@RequestBody JSONObject body){
        String instituteName = (String) body.get("instituteName");
        Institute institute=new Institute(instituteName);
        return instituteService.saveInstitute(institute);
    }

    /**
     * @RequestParam(value = "instituteId") String instituteId,
     * @RequestParam(value = "newInstituteName") String newInstituteName
     * */
    //修改院系信息
    @ApiOperation(value = "修改院系信息")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PutMapping("/institutes")
    public Response<?> alterInstitute(@RequestBody JSONObject body){
        String instituteId = (String) body.get("instituteId");
        String newInstituteName = (String) body.get("newInstituteName");

        Institute institute = instituteService.getInstituteByInstituteId(instituteId);
        institute.setName(newInstituteName);
        return instituteService.saveInstitute(institute);
    }

    //删除专业
    @ApiOperation(value = "删除专业")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @DeleteMapping("/majors")
    public Response<?> deleteMajor(@RequestBody JSONObject body){
        String majorId = (String) body.get("majorId");
        Integer delete=majorService.deleteMajorByMajorId(majorId);
        return new Response<>(delete==1,delete==1?"删除成功":"删除失败，请检查是否存在该专业");
    }

    /**
     * @RequestParam(value = "majorId") String majorId,
     * @RequestParam(value = "newMajorName") String newMajorName,
     * @RequestParam(value = "newInstituteId") String newInstituteId
     * */
    //修改专业，默认传回的专业id和学院id都是有效的。
    @ApiOperation(value = "修改专业")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PutMapping("/majors")
    public Response<?> alterMajor(@RequestBody JSONObject body){
        String majorId = (String) body.get("majorId");
        String newMajorName = (String) body.get("newMajorName");
        String newInstituteId = (String) body.get("newInstituteId");

        Major major=majorService.getMajorByMajorId(majorId);
        major.setName(newMajorName);
        major.setInstitute(instituteService.getInstituteByInstituteId(newInstituteId));
        return majorService.saveMajor(major);
    }

    /**
     * @RequestParam(value = "majorName") String majorName,
     * @RequestParam(value = "instituteId") String instituteId
     * */
    //增加专业,默认新的专业名和学院名都是有效的
    @ApiOperation(value = "增加专业")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PostMapping("/majors")
    public Response<?> addMajor(@RequestBody JSONObject body){
        String majorName = (String) body.get("majorName");
        String instituteId = (String) body.get("instituteId");

        Institute institute=instituteService.getInstituteByInstituteId(instituteId);
        Major major = new Major(majorName,institute);
        return majorService.saveMajor(major);
    }

    //删除教学楼
    @ApiOperation(value = "删除教学楼")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @DeleteMapping("/buildings")
    public Response<?> deleteBuilding(@RequestBody JSONObject body){
        String buildingId = (String) body.get("buildingId");

        Integer delete=buildingService.deleteBuildingById(buildingId);
        return new Response<>(delete==1,delete==1?"删除教学楼成功":"删除教学楼失败，请检查是否存在该教学楼");
    }
    //增加教学楼
    @ApiOperation(value = "增加教学楼")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PostMapping("/buildings")
    public Response<?> addBuilding(@RequestBody JSONObject body){
        String buildingName = (String) body.get("buildingName");


        Building building = new Building(buildingName);
        return buildingService.saveBuilding(building);
    }

    /***
     * @RequestParam(value = "buildingId") String buildingId,
     * @RequestParam(value = "newBuilding") String newBuildingName
     */
    //修改教学楼信息
    @ApiOperation(value = "修改教学楼信息")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PutMapping("/buildings")
    public Response<?> alterBuilding(@RequestBody JSONObject body){
        String buildingId = (String) body.get("buildingId");
        String newBuildingName = (String) body.get("newBuilding");

        Building building = buildingService.getBuildingByBuildingId(buildingId);
        building.setName(newBuildingName);
        return buildingService.saveBuilding(building);
    }

    //删除教室
    @ApiOperation(value = "删除教室")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @DeleteMapping("/classrooms")
    public Response<?> deleteClassroom(@RequestBody JSONObject body){
        String classroomId = (String) body.get("classroomId");

        Integer delete=classroomService.deleteClassroomByClassroomId(classroomId);
        return new Response<>(delete==1,delete==1?"删除成功":"删除失败，请检查是否存在该教室");
    }

    /**
     * @RequestParam(value = "classroomId") String classroomId,
     * @RequestParam(value = "newClassroomName") String newClassroomName,
     * @RequestParam(value = "newClassroomCapacity") int newClassroomCapacity,
     * @RequestParam(value = "newBuildingId") String newBuildingId
     */
    //修改教室，默认传回的教室id和学院id都是有效的。
    @ApiOperation(value = "修改教室")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PutMapping("/classrooms")
    public Response<?> alterClassroom(@RequestBody JSONObject body){
        String classroomId = (String) body.get("classroomId");
        String newClassroomName = (String) body.get("newClassroomName");
        int newClassroomCapacity = (int) body.get("newClassroomCapacity");
        String newBuildingId = (String) body.get("newBuildingId");

        Classroom classroom=classroomService.getClassroomByClassroomId(classroomId);
        classroom.setName(newClassroomName);
        classroom.setCapacity(newClassroomCapacity);
        classroom.setBuilding(buildingService.getBuildingByBuildingId(newBuildingId));
        return classroomService.saveClassroom(classroom);
    }

    /**
     * @RequestParam(value = "classroomName") String classroomName,
     * @RequestParam(value = "capacity") int capacity,
     * @RequestParam(value = "buildingId") String buildingId
     * */
    //增加教室,默认新的教室名和学院名都是有效的
    @ApiOperation(value = "增加教室")
    @VerifyToken(identity = IDENTITY_ADMIN)
    @PostMapping("/classrooms")
    public Response<?> addClassroom(@RequestBody JSONObject body){
        String classroomName = (String) body.get("classroomName");
        int capacity = (int) body.get("capacity");
        String buildingId = (String) body.get("buildingId");

        Building building=buildingService.getBuildingByBuildingId(buildingId);
        Classroom classroom = new Classroom(classroomName,capacity,building);
        return classroomService.saveClassroom(classroom);
    }
}
