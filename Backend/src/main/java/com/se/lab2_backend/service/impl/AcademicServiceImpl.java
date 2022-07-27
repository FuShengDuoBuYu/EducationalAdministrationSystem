package com.se.lab2_backend.service.impl;

import com.se.lab2_backend.common.CourseSelectionApplicationVO;
import com.se.lab2_backend.common.CourseVO;
import com.se.lab2_backend.common.UserVO;
import com.se.lab2_backend.entity.*;
import com.se.lab2_backend.mapper.*;
import com.se.lab2_backend.service.*;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AcademicServiceImpl implements AcademicService {
    AdminService adminService;
    StudentService studentService;
    CourseMapper courseMapper;
    ApplicationResultMapper applicationResultMapper;
    StudentCourseMapper studentCourseMapper;
    StatusService statusService;
    CourseService courseService;
    CourseSelectionApplicationMapper courseSelectionApplicationMapper;

    @Autowired
    public AcademicServiceImpl(AdminService adminService, StudentService studentService, CourseMapper courseMapper, ApplicationResultMapper applicationResultMapper, StudentCourseMapper studentCourseMapper, StatusService statusService, CourseService courseService, CourseSelectionApplicationMapper courseSelectionApplicationMapper) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.courseMapper = courseMapper;
        this.applicationResultMapper = applicationResultMapper;
        this.studentCourseMapper = studentCourseMapper;
        this.statusService = statusService;
        this.courseService = courseService;
        this.courseSelectionApplicationMapper = courseSelectionApplicationMapper;
    }

    private List<CourseVO> generateCourseVOList(List<StudentCourse> studentCourseList){
        List<CourseVO> courseVOList = new ArrayList<>();
        for(StudentCourse s : studentCourseList){
            Course c = courseMapper.findCourseByCourseId(s.getCourseId());
            courseVOList.add(new CourseVO(
                    c.getCourseId(),
                    c.getNumber(),
                    c.getIdentifier(),
                    c.getName(),
                    c.getClassHour(),
                    c.getCredit(),
                    c.getCapacity(),
                    c.getDescription(),
                    c.getTeacher().getUsername(),
                    c.getTeacher().getUuid(),
                    c.getMajor().getName(),
                    c.getMajor().getMajorId(),
                    c.getAvailableType().getName(),
                    c.getAvailableType().getAvailableTypeId(),
                    c.getMajor().getInstitute().getName(),
                    c.getMajor().getInstitute().getInstituteId(),
                    c.getYear(),
                    c.getTerm(),
                    CourseServiceImpl.getTimetableList(c),
                    CourseServiceImpl.getAvailableMajorList(c),
                    studentCourseMapper.findAllByCourseId(c.getCourseId()).size(),
                    c.isFinished()
            ));
        }
        return courseVOList;
    }

    @Override
    public Response<?> getSelectedCourses(String studentId){
        List<StudentCourse> selected = studentCourseMapper.findAllByStudentIdAndStatus(studentId, getSelectedStatus());
        return new Response<>(true, "查询成功", generateCourseVOList(selected));
    }

    @Override
    public Response<?> getFinishedCourses(String studentId){
        List<StudentCourse> finished = studentCourseMapper.findAllByStudentIdAndStatus(studentId, getFinishedStatus());
        return new Response<>(true, "查询成功", generateCourseVOList(finished));
    }

    @Override
    public Response<?> saveSelectedCourses(String studentId, String courseId){
        Response<?> res = null;
        Course course = courseService.getCourseByCourseId(courseId);
        //TODO: 同类课程（课程代码和课程名称相同的课程）同一个学生只能选一门
        if(isCourseSelected(studentId, course.getNumber())){
            res = new Response<>(false, "课程已选");
        } else if(isCourseFinished(studentId, course.getNumber())) {
            res = new Response<>(false, "课程已修");
        } else if(isCourseFull(courseId)){
            res = new Response<>(false, "课程容量已达上限");
        } else if(isTimeConflict(studentId, course)){
            res = new Response<>(false, "课程时间冲突！");
        }
        if(res != null) return res;
        StudentCourse selectCourse = new StudentCourse(studentId,courseId,statusService.getStatusByStatusId(ConstVariable.SELECTED_COURSE_STATUS));
        studentCourseMapper.save(selectCourse);
        return new Response<>(true, "选课成功");
    }

    private boolean isTimeConflict(String studentId, Course course) {
        for(StudentCourse studentCourse: studentCourseMapper.findAllByStudentIdAndStatus(studentId, statusService.getStatusByStatusId(ConstVariable.SELECTED_COURSE_STATUS))){
            Course courseTemp = courseService.getCourseByCourseId(studentCourse.getCourseId());
            if(isCourseTimeConflict(course, courseTemp)) return true;
        }
        return false;
    }

    private boolean isCourseTimeConflict(Course course, Course courseTemp) {
        List<Timetable> timetable = course.getTimetableList();
        List<Timetable> timetableTemp = courseTemp.getTimetableList();
        for(Timetable t: timetable){
            if(timetableTemp.stream().anyMatch(timetable1 -> {
                return timetable1.getSession() == t.getSession() && timetable1.getDay() == t.getDay();
            })) return true;
        }
        return false;
    }

    private boolean isCourseSelected(String studentId, String number){
        List<Course> courseList = courseMapper.findAllByNumber(number);
        boolean res = false;
        for(Course course: courseList)
            if(studentCourseMapper.findStudentCourseByStudentIdAndCourseIdAndStatus(studentId, course.getCourseId(), getSelectedStatus()) != null){
                res = true;
                break;
            }
        return res;
    }
    private boolean isCourseFinished(String studentId, String number){
        List<Course> courseList = courseMapper.findAllByNumber(number);
        boolean res = false;
        for(Course course: courseList)
            if(studentCourseMapper.findStudentCourseByStudentIdAndCourseIdAndStatus(studentId, course.getCourseId(), getFinishedStatus()) != null){
                res = true;
                break;
            }
        return res;
    }
    private boolean isCourseFull(String courseId){
        if(adminService.getCourseSelection().get("courseSelectionRound").equals("1")) return false;
        List<StudentCourse> studentCourseList = studentCourseMapper.findAllByCourseId(courseId);
        Course course = courseMapper.findCourseByCourseId(courseId);
        return studentCourseList.size()>=course.getCapacity();
    }
    private Status getSelectedStatus(){
        return statusService.getStatusByStatusId(ConstVariable.SELECTED_COURSE_STATUS);
    }
    private Status getFinishedStatus(){
        return statusService.getStatusByStatusId(ConstVariable.FINISHED_COURSE_STATUS);
    }

    @Override
    @Transactional
    public Response<?> deleteCourseByStudentIdAndCourseId(String studentId, String courseId){
        Integer del = studentCourseMapper.deleteByStudentIdAndCourseId(studentId,courseId);
        return new Response<>(del==1,del==1?"退课成功":"表中无数据");
    }

    UserVO generateStudentVO(String studentId){
        Student s = studentService.getStudentByUuid(studentId);
        return new UserVO(
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
                s.getStatus().getStatusId());
    }

    @Override
    public List<UserVO> getStudentsByCourseId(String courseId){
        List<StudentCourse> selectedCourses = studentCourseMapper.findAllByCourseId(courseId);
        List<UserVO> students = new ArrayList<>();
        for(StudentCourse studentCourse : selectedCourses){
            students.add(generateStudentVO(studentCourse.getStudentId()));
        }
//        return new Response<>(true,students.size()==0?"该课程无学生":"成功获取",students);
        return students;
    }

    @Override
    public Response<?> finishCourse(Course course) {
        course.setFinished(true);
        List<StudentCourse> studentCourseList = studentCourseMapper.findAllByCourseId(course.getCourseId());
        for(StudentCourse s: studentCourseList)
            s.setStatus(statusService.getStatusByStatusId(ConstVariable.FINISHED_COURSE_STATUS));
        studentCourseMapper.saveAll(studentCourseList);
        courseMapper.save(course);
        return new Response<>(true, "结课成功！");
    }

    @Override
    public Response<?> saveSelectCourseApplication(String studentId, String courseId, String reason) {
        ApplicationResult initialApplicationResult = applicationResultMapper.findByApplicationResultId(ConstVariable.INITIAL_APPLICATION_RESULT);
        if(courseSelectionApplicationMapper.findCourseSelectionApplicationByStudentIdAndCourseIdAndApplicationResult(studentId, courseId, initialApplicationResult)!=null) return new Response<>(false, "存在未处理的选课申请，请等待管理员处理！");
        CourseSelectionApplication courseSelectionApplication = new CourseSelectionApplication(courseId, studentId, reason, initialApplicationResult);
        courseSelectionApplication = courseSelectionApplicationMapper.save(courseSelectionApplication);
        return new Response<>(true, "选课申请提交成功！", courseSelectionApplication);
    }

    List<CourseSelectionApplicationVO> generateCourseSelectionApplicationVOList(List<CourseSelectionApplication> courseSelectionApplications){
        List<CourseSelectionApplicationVO> res = new ArrayList<>();
        for(CourseSelectionApplication courseSelectionApplication: courseSelectionApplications){
            CourseSelectionApplicationVO tempVO = new CourseSelectionApplicationVO();
            tempVO.setApplicationId(courseSelectionApplication.getApplicationId());
            tempVO.setCourse(courseService.getCourseVOByCourseId(courseSelectionApplication.getCourseId()));
            tempVO.setStudent(generateStudentVO(courseSelectionApplication.getStudentId()));
            tempVO.setReason(courseSelectionApplication.getReason());
            tempVO.setApplicationResult(courseSelectionApplication.getApplicationResult().getName());
            res.add(tempVO);
        }
        return res;
    }

    @Override
    public Response<?> getAllSelectCourseApplicationByStudentId(String studentId) {
        List<CourseSelectionApplication> courseSelectionApplications = courseSelectionApplicationMapper.findAllByStudentId(studentId);
        return new Response<>(true, ConstVariable.getInfoSuccess("学生选课申请"), generateCourseSelectionApplicationVOList(courseSelectionApplications));
    }

    @Override
    public Response<?> getAllSelectCourseApplication() {
        return new Response<>(true, ConstVariable.getInfoSuccess("全部选课申请"), generateCourseSelectionApplicationVOList(courseSelectionApplicationMapper.findAll()));
    }

    @Override
    public Response<?> handleSelectionApplication(String applicationId, String applicationResultId) {
        CourseSelectionApplication courseSelectionApplication = courseSelectionApplicationMapper.findByApplicationId(applicationId);
        if(!courseSelectionApplication.getApplicationResult().getApplicationResultId().equals(ConstVariable.INITIAL_APPLICATION_RESULT)) return new Response<>(false, "该申请已经处理！");
        Response<?> res = new Response<>(false, "操作失败！");
        switch (applicationResultId){
            case ConstVariable.REFUSE_APPLICATION_RESULT:
                res = refuseSelectionApplication(courseSelectionApplication);
                break;
            case ConstVariable.ACCEPT_APPLICATION_RESULT:
                res = acceptSelectionApplication(courseSelectionApplication);
                break;
        }
        return res;
    }

    @Override
    @Transactional
    public Response<?> setCourseSelectionRound(String uuid, String courseSelectionRound) {
        Admin admin = adminService.getAdminByUuid(uuid);
        admin.setCourseSelectionRound(courseSelectionRound);
        if(courseSelectionRound.equals("2")){
            List<String> courseIdList = courseService.getAvailableCourseIdList();
//            for(String s: courseIdList) System.out.println(s);
            for(String courseId: courseIdList)
                filterStudent(courseId);
        }
        adminService.saveAdmin(admin);
        return new Response<>(true, ConstVariable.REQUEST_SUCCESS);
    }

    //根据课程id进行筛人
    private void filterStudent(String courseId) {
        List<StudentCourse> studentCourseList = studentCourseMapper.findAllByCourseId(courseId);
        Course course = courseMapper.findCourseByCourseId(courseId);
//System.out.println(course.getCapacity() + " " + studentCourseList.size());
        List<Student> students = new ArrayList<>();
        List<Student> filteredStudents = new ArrayList<>();
        if(course.getCapacity()<studentCourseList.size()){
//System.out.println(course.getCourseId() + " " + course.getName());
            for(StudentCourse studentCourse: studentCourseList)
                students.add(studentService.getStudentByUuid(studentCourse.getStudentId()));
            //首先进行分组
            Map<String, List<Student>> groupedStudents = students.stream().collect(Collectors.groupingBy(item -> item.getJobNum().substring(0, 2)));
            //再对key值进行排序(也就是学号前两位)
            groupedStudents = groupedStudents.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            int sum = 0;
            String exceedYear = null;
            // 人数筛选
            for(Map.Entry<String, List<Student>> entry: groupedStudents.entrySet()){
                if(sum + entry.getValue().size() >= course.getCapacity()){
                    exceedYear = entry.getKey();
                    break;
                } else {
                    filteredStudents.addAll(entry.getValue());
                    sum += entry.getValue().size();
                }
            }
            //随机踢人
            Collections.shuffle(groupedStudents.get(exceedYear));
            filteredStudents.addAll(groupedStudents.get(exceedYear).subList(0, course.getCapacity()-sum));
            studentCourseMapper.deleteAllByCourseId(courseId);
            // 保存数据
            for(Student s: filteredStudents){
                StudentCourse selectCourse = new StudentCourse(s.getUuid(),courseId,statusService.getStatusByStatusId(ConstVariable.SELECTED_COURSE_STATUS));
                studentCourseMapper.save(selectCourse);
            }
        }
    }

    private Response<?> acceptSelectionApplication(CourseSelectionApplication courseSelectionApplication) {
        String studentId = courseSelectionApplication.getStudentId();
        String courseId = courseSelectionApplication.getCourseId();
        if(canCourseExpand(courseSelectionApplication.getCourseId())) return new Response<>(false, "教室剩余容量不足，无法通过申请！");
        Course course = courseMapper.findCourseByCourseId(courseSelectionApplication.getCourseId());
        course.setCapacity(course.getCapacity()+1);
        StudentCourse selectCourse = new StudentCourse(studentId,courseId,statusService.getStatusByStatusId(ConstVariable.SELECTED_COURSE_STATUS));
        studentCourseMapper.save(selectCourse);
        courseSelectionApplication.setApplicationResult(applicationResultMapper.findByApplicationResultId(ConstVariable.ACCEPT_APPLICATION_RESULT));
        courseSelectionApplicationMapper.save(courseSelectionApplication);
        return new Response<>(true, "同意申请成功！课程已自动扩容");
    }

    private boolean canCourseExpand(String courseId) {
//        List<StudentCourse> studentCourseList = studentCourseMapper.findAllByCourseId(courseId);
        Course course = courseMapper.findCourseByCourseId(courseId);
        int classroomCapacity = course.getTimetableList().get(0).getClassroom().getCapacity();
        return course.getCapacity()>=classroomCapacity;
    }

    private Response<?> refuseSelectionApplication(CourseSelectionApplication courseSelectionApplication) {
        courseSelectionApplication.setApplicationResult(applicationResultMapper.findByApplicationResultId(ConstVariable.REFUSE_APPLICATION_RESULT));
        courseSelectionApplicationMapper.save(courseSelectionApplication);
        return new Response<>(true, "拒绝申请成功！");
    }
}
