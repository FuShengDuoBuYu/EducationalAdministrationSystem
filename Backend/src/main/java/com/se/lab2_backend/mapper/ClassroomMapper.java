package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.Classroom;
import com.se.lab2_backend.entity.Classroom;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomMapper extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findClassroomByName(String classroomName);
    Optional<Classroom> findClassroomByClassroomId(String classroomId);
    @NotNull("没有专业信息！") List<Classroom> findAll();
    @Transactional
    Integer deleteClassroomByClassroomId(String classroomId);
}

