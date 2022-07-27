package com.se.lab2_backend.service;

import com.se.lab2_backend.entity.Institute;
import com.se.lab2_backend.entity.Status;
import com.se.lab2_backend.util.Response;

import java.util.List;

public interface InstituteService {
    public List<Institute> getAll();
    public Institute getInstituteByInstituteId(String statusId);
    public Institute getInstituteByName(String statusName);
    public Integer deleteInstituteById(String instituteId);
    public Response<?> saveInstitute(Institute institute);
}
