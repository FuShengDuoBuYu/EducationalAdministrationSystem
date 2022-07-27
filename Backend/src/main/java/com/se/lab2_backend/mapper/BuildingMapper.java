package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.Building;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildingMapper extends JpaRepository<Building, Long> {
    @NotNull("没有教学楼信息！") List<Building> findAll();
    Optional<Building> findBuildingByName(String buildingName);
    Optional<Building> findBuildingByBuildingId(String buildingId);
    @Transactional
    Integer deleteBuildingByBuildingId(String buildingId);
}
