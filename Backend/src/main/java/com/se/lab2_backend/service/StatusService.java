package com.se.lab2_backend.service;

import com.se.lab2_backend.entity.Status;

import java.util.ArrayList;
import java.util.List;

public interface StatusService {
    public List<Status> getAll();
    public List<Status> getAllNameByIdentity(String identity);
    public ArrayList<String> getAllStatusName();
    public Status getStatusByStatusId(String statusId);
    public Status getStatusByName(String statusName);
}
