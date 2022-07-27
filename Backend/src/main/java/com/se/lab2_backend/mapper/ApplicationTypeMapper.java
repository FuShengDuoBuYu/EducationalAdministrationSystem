package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.ApplicationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationTypeMapper extends JpaRepository<ApplicationType, Long> {
    ApplicationType findByApplicationTypeId(String applicationTypeId);
}
