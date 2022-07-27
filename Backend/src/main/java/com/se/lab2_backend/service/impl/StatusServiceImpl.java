package com.se.lab2_backend.service.impl;

import com.se.lab2_backend.entity.Status;
import com.se.lab2_backend.mapper.StatusMapper;
import com.se.lab2_backend.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {
    StatusMapper statusMapper;
    @Autowired
    public StatusServiceImpl(StatusMapper statusMapper) {
        this.statusMapper = statusMapper;
    }

    @Override
    public List<Status> getAll() {
        return statusMapper.findAll();
    }

    @Override
    public List<Status> getAllNameByIdentity(String identity) {
        return statusMapper.findAllByIdentity(identity);
    }


    @Override
    public ArrayList<String> getAllStatusName() {
        ArrayList<String> statuses = new ArrayList<String>();
        for(Status s: statusMapper.findAll()){
            statuses.add(s.getName());
        }
        return statuses;
    }

    @Override
    public Status getStatusByStatusId(String statusId) {
        return statusMapper.findStatusByStatusId(statusId)
                .orElseThrow(() -> new RuntimeException("状态" + "id" + "不存在！"));
    }

    @Override
    public Status getStatusByName(String statusName) {
        return statusMapper.findStatusByName(statusName)
                .orElseThrow(() -> new RuntimeException("状态" + statusName + "不存在！"));
    }
}
