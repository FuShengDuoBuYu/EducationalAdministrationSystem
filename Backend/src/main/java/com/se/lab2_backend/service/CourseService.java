package com.se.lab2_backend.service;

import com.se.lab2_backend.common.CourseApplicationVO;
import com.se.lab2_backend.common.CourseVO;
import com.se.lab2_backend.common.TimetableRequest;
import com.se.lab2_backend.common.TimetableVO;
import com.se.lab2_backend.entity.*;
import com.se.lab2_backend.service.impl.CourseServiceImpl;
import com.se.lab2_backend.util.Response;

import java.util.List;

public interface CourseService {


    //一些显示相关 GET请求
    public List<CourseVO> createCourseList(List<Course> courseList);
    public List<CourseVO> getCourseList();
    public List<CourseVO> getCourseListByMajorId(String majorId);
    public CourseVO getCourseVOByCourseId(String courseId);
    public List<CourseVO> getAvailableCourseListByMajorId(String majorId);
    public List<CourseVO> getCourseListByTeacherId(String teacherId);
    public List<CourseApplicationVO> createCourseApplicationList(List<CourseApplication> courseApplicationList);
    public List<CourseApplicationVO> getCourseApplicationList();
    public List<CourseApplicationVO> getCourseApplicationList(String teacherId);

    //生成标识符
    public String getIdentifier(String number);
    //课程的增改删
    public Response<?> saveCourse(Course course, List<TimetableRequest> timetableList, List<String> availableMajorIdList);
    public Response<?> updateCourse(Course course, List<TimetableRequest> timetableList, List<String> availableMajorIdList);
    public void updateCourseImpl(Course course, List<Timetable> timetableList, List<String> availableMajorIdList);
    public Response<?> deleteCourseByCourseId(String courseId);
    public Response<?> saveAll(List<Course> courses,List<List<TimetableRequest>> timetableLists);
    //课程申请的新增和处理
    public Response<?> saveCourseApplication(CourseApplication courseApplication);
    public Response<?> handleApplication(String applicationId, String applicationResultId);
    public Response<?> saveCourseTest(Course course);
    //获取选课开关
    public boolean getCourseSelection();
    //获取时间表操作
    public Response<?> saveSchedule(List<Schedule> scheduleList);
    public List<Schedule> getSchedule();
    //申请的结果和类型操作
    public List<ApplicationType> getApplicationType();
    public List<ApplicationResult> getApplicationResult();
    public ApplicationType getApplicationTypeById(String applicationTypeId);
    public ApplicationResult getApplicationResultById(String applicationResultId);

    public Course getCourseByCourseId(String courseId);
    //判断时间表是否冲突
    public boolean timeIsConflict(String courseId, List<TimetableRequest> timetableList);

    //修改课程可选专业
    public Response<?> setCoursePermission(Course course, List<String> availableMajorIdList);
    //获取当前学期的全部开课Id
    public List<String> getAvailableCourseIdList();
}
