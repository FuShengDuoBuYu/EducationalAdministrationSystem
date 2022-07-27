package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.AvailableType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableTypeMapper extends JpaRepository<AvailableType, Long> {
    AvailableType findAvailableTypeByAvailableTypeId(String availableTypeId);
}
