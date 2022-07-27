package com.se.lab2_backend.service;

import com.se.lab2_backend.entity.Building;
import com.se.lab2_backend.entity.Building;
import com.se.lab2_backend.util.Response;

import java.util.List;

public interface BuildingService {
    public List<Building> getAll();
    public Building getBuildingByBuildingId(String buildingId);
    public Building getBuildingByName(String buildingName);
    public Integer deleteBuildingById(String buildingId);
    public Response<?> saveBuilding(Building building);

}
