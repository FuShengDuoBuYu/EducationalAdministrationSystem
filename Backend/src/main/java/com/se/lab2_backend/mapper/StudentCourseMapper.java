package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.Status;
import com.se.lab2_backend.entity.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentCourseMapper extends JpaRepository<StudentCourse,Long>{
    List<StudentCourse> findAllByCourseId(String courseId);
    List<StudentCourse> findAllByStudentId(String studentId);
    List<StudentCourse> findAllByStudentIdAndStatus(String studentId, Status status);
    List<StudentCourse> findAllByCourseIdAndStatus(String studentId, Status status);
    @Transactional
    Integer deleteAllByCourseId(String courseId);
    @Transactional
    Integer deleteByStudentIdAndCourseId(String studentId,String courseId);
    StudentCourse findStudentCourseByStudentIdAndCourseIdAndStatus(String studentId, String courseId, Status status);
}
