package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.Major;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MajorMapper extends JpaRepository<Major, Long> {
    Optional<Major> findMajorByName(String majorName);
    Optional<Major> findMajorByMajorId(String majorId);
    @NotNull("没有专业信息！") List<Major> findAll();
    @Transactional
    Integer deleteMajorByMajorId(String majorId);
    //Integer deleteMajorsByInstituteId(String instituteId);
}
