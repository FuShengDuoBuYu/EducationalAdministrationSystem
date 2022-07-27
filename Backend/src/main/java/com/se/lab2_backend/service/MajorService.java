package com.se.lab2_backend.service;

import com.se.lab2_backend.entity.Institute;
import com.se.lab2_backend.entity.Major;
import com.se.lab2_backend.util.Response;
import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.List;

public interface MajorService {
    public Major getMajorByMajorId(String majorId);
    public Major getMajorByName(String majorName);
    public List<Major> getAll();
    public Integer deleteMajorByMajorId(String majorId);
    Response<?> saveMajor(Major major);
    //public Integer deleteMajorsByInstituteId(String instituteId);
}
