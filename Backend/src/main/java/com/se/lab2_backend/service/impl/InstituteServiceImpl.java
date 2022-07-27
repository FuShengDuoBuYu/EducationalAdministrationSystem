package com.se.lab2_backend.service.impl;

import com.se.lab2_backend.entity.Institute;
import com.se.lab2_backend.mapper.InstituteMapper;
import com.se.lab2_backend.service.InstituteService;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InstituteServiceImpl implements InstituteService {
    InstituteMapper instituteMapper;
    @Autowired
    public InstituteServiceImpl(InstituteMapper instituteMapper) {
        this.instituteMapper = instituteMapper;
    }

    @Override
    public List<Institute> getAll() {
        return instituteMapper.findAll();
    }

    @Override
    public Institute getInstituteByInstituteId(String instituteId) {
        return instituteMapper.findInstituteByInstituteId(instituteId)
                .orElseThrow(() -> new RuntimeException("学院" + "id" + "不存在！"));
    }

    @Override
    public Institute getInstituteByName(String instituteName) {
        return instituteMapper.findInstituteByName(instituteName)
                .orElseThrow(() -> new RuntimeException("学院" + instituteName + "不存在！"));
    }

    @Override
    @Transactional
    public Integer deleteInstituteById(String instituteId){ return instituteMapper.deleteInstituteByInstituteId(instituteId);}

    @Override
    public Response<?> saveInstitute(Institute institute){
        try {
            instituteMapper.save(institute);
            return new Response<>(true, ConstVariable.REQUEST_SUCCESS);
        }catch (Exception exception){
            return new Response<>(false,ConstVariable.REQUEST_FAIL);
        }


    }
}
