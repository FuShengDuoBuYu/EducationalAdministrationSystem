package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.CourseApplication;
import com.se.lab2_backend.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseApplicationMapper extends JpaRepository<CourseApplication, Long> {
    List<CourseApplication> findAllByTeacher(Teacher teacher);

    CourseApplication findByCourseId(String courseId);

    CourseApplication findByApplicationId(String applicationId);
}
