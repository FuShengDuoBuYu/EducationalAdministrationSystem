package com.se.lab2_backend.service;

import com.se.lab2_backend.common.UserVO;
import com.se.lab2_backend.entity.Course;
import com.se.lab2_backend.util.Response;

import java.util.List;

public interface AcademicService {
    //获取已选课程
    public Response<?> getSelectedCourses(String studentId);
    //获取已修课程
    public Response<?> getFinishedCourses(String studentId);
    //选课
    public Response<?> saveSelectedCourses(String studentId, String courseId);
    //退课
    public Response<?> deleteCourseByStudentIdAndCourseId(String studentId, String courseId);
    //管理员获取一门课的所有学生
    public List<UserVO> getStudentsByCourseId(String courseId);
    //管理员结课
    public Response<?> finishCourse(Course course);
    //提交选课申请
    public Response<?> saveSelectCourseApplication(String studentId, String courseId, String reason);
    //学生获取所有选课申请
    public Response<?> getAllSelectCourseApplicationByStudentId(String studentId);
    //管理员获取所有选课申请
    Response<?> getAllSelectCourseApplication();
    //管理员处理选课申请
    Response<?> handleSelectionApplication(String applicationId, String applicationResult);
    //管理员修改选课轮次
    Response<?> setCourseSelectionRound(String uuid, String courseSelectionRound);
}
