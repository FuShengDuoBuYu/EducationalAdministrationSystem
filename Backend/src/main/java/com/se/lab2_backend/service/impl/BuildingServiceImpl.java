package com.se.lab2_backend.service.impl;

import com.se.lab2_backend.entity.Building;
import com.se.lab2_backend.entity.Building;
import com.se.lab2_backend.mapper.BuildingMapper;
import com.se.lab2_backend.service.BuildingService;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {
    BuildingMapper buildingMapper;
    @Autowired
    public BuildingServiceImpl(BuildingMapper buildingMapper) {
        this.buildingMapper = buildingMapper;
    }

    @Override
    public List<Building> getAll() {
        List<Building> buildings = buildingMapper.findAll();
//        for(Building b: buildings)
//            b.setClassroomNum(b.getClassrooms().size());
        return buildings;
    }

    @Override
    public Building getBuildingByBuildingId(String buildingId) {
        return buildingMapper.findBuildingByBuildingId(buildingId)
                .orElseThrow(() -> new RuntimeException("教学楼" + "id" + "不存在！"));
    }

    @Override
    public Building getBuildingByName(String buildingName) {
        return buildingMapper.findBuildingByName(buildingName)
                .orElseThrow(() -> new RuntimeException("教学楼" + buildingName + "不存在！"));
    }

    @Override
    @Transactional
    public Integer deleteBuildingById(String buildingId){ return buildingMapper.deleteBuildingByBuildingId(buildingId);}

    @Override
    public Response<?> saveBuilding(Building building){
        try {
            buildingMapper.save(building);
            return new Response<>(true, ConstVariable.REQUEST_SUCCESS);
        }catch (Exception exception){
            return new Response<>(false,ConstVariable.REQUEST_FAIL);
        }
    }
}