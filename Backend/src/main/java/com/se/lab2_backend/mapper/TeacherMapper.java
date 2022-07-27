package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.Teacher;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherMapper extends JpaRepository<Teacher,Long> {

    @NotNull("没有教师信息！") List<Teacher> findAll();
    Teacher findTeacherByJobNum(String jobNum);
    Teacher findTeacherByIdcardNum(String idcardNum);
    Teacher findTeacherByUuid(String uuid);
    Integer deleteTeacherByJobNum(String jobNum);
}
