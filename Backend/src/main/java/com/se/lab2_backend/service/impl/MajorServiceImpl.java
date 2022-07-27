package com.se.lab2_backend.service.impl;

import com.se.lab2_backend.entity.Major;
import com.se.lab2_backend.mapper.MajorMapper;
import com.se.lab2_backend.service.MajorService;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {
    MajorMapper majorMapper;
    @Autowired
    public MajorServiceImpl(MajorMapper majorMapper) {
        this.majorMapper = majorMapper;
    }

    @Override
    public Major getMajorByMajorId(String majorId) {
        return majorMapper.findMajorByMajorId(majorId)
                .orElseThrow(() -> new RuntimeException("专业" + "id" + "不存在！"));
    }

    @Override
    public Major getMajorByName(String majorName) {
        return majorMapper.findMajorByName(majorName)
                .orElseThrow(() -> new RuntimeException("专业" + majorName + "不存在！"));
    }

    @Override
    public List<Major> getAll() {
        return majorMapper.findAll();
    }


    @Override
    @Transactional
    public Integer deleteMajorByMajorId(String majorId) { return majorMapper.deleteMajorByMajorId(majorId);}

    @Override
    public Response<?> saveMajor(Major major){
        try {
            majorMapper.save(major);
            return new Response<>(true, ConstVariable.REQUEST_SUCCESS);
        }catch (Exception exception){
            return new Response<>(false,ConstVariable.REQUEST_FAIL);
        }

    }
//    @Override
//    public Integer deleteMajorsByInstituteId(String instituteId){ return majorMapper.deleteMajorsByInstituteId(instituteId);}
}

