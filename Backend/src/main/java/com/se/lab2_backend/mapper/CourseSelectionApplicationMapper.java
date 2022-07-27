package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.ApplicationResult;
import com.se.lab2_backend.entity.CourseSelectionApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CourseSelectionApplicationMapper extends JpaRepository<CourseSelectionApplication, Long> {
    CourseSelectionApplication findCourseSelectionApplicationByStudentIdAndCourseIdAndApplicationResult(String studentId, String courseId, ApplicationResult applicationResult);
    List<CourseSelectionApplication> findAllByStudentId(String studentId);
    CourseSelectionApplication findByApplicationId(String applicationId);
    @Transactional
    Integer deleteByApplicationId(String applicationId);
}
