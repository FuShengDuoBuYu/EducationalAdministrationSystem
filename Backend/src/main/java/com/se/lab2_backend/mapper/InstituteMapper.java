package com.se.lab2_backend.mapper;


import com.fasterxml.jackson.core.SerializableString;
import com.se.lab2_backend.entity.Institute;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstituteMapper extends JpaRepository<Institute, Long> {
    @NotNull("没有学院信息！") List<Institute> findAll();
    Optional<Institute> findInstituteByName(String instituteName);
    Optional<Institute> findInstituteByInstituteId(String instituteId);
    @Transactional
    Integer deleteInstituteByInstituteId(String instituteId);
}
