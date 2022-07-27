package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.CourseMajor;
import com.se.lab2_backend.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CourseMajorMapper extends JpaRepository<CourseMajor, Long> {
    List<CourseMajor> findAllByCourseId(String courseId);
    @Transactional
    void deleteAllByCourseId(String courseId);
    List<CourseMajor> findAllByMajor(Major major);
}
