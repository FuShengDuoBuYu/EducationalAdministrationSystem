package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.ApplicationResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationResultMapper extends JpaRepository<ApplicationResult, Long> {
    ApplicationResult findByApplicationResultId(String applicationResultId);
}
