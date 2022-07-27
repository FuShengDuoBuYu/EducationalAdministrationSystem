package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.Student;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMapper extends JpaRepository<Student,Long> {
    @NotNull("没有学生信息！") List<Student> findAll();
    Student findStudentByJobNum(String jobNum);
    Student findStudentByIdcardNum(String idcardNum);
    Student findStudentByUuid(String uuid);
    Integer deleteStudentByJobNum(String jobNum);
}
