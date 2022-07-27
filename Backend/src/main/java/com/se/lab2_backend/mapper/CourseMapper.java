package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.AvailableType;
import com.se.lab2_backend.entity.Course;
import com.se.lab2_backend.entity.Major;
import com.se.lab2_backend.entity.Teacher;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseMapper extends JpaRepository<Course, Long> {
    List<Course> findAllByNumber(String number);
    List<Course> findAllByMajor(Major major);
    List<Course> findAllByYearAndTermAndFinished(String year, String term, boolean finished);
    List<Course> findAllByName(String name);
    List<Course> findAllByAvailableType(AvailableType availableType);
    Course findCourseByCourseId(String courseId);
    List<Course> findAllByTeacher(Teacher teacher);
    @Transactional
    Integer deleteCourseByCourseId(String courseId);

}
