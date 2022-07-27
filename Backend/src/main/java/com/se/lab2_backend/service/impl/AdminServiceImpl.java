package com.se.lab2_backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.se.lab2_backend.entity.Admin;
import com.se.lab2_backend.entity.Schedule;
import com.se.lab2_backend.mapper.AdminMapper;
import com.se.lab2_backend.mapper.ScheduleMapper;
import com.se.lab2_backend.service.AdminService;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import com.se.lab2_backend.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.se.lab2_backend.util.ConstVariable.LOGIN_SUCCESS;

@Service
public class AdminServiceImpl implements AdminService {

    AdminMapper adminMapper;

    @Autowired
    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminMapper.save(admin);
    }

    @Override
    public JSONObject getCourseSelection() {
        Admin admin = adminMapper.findAdminByJobNum("000000");
        JSONObject res = new JSONObject();
        res.put("isCourseSelection", admin.isCourseSelection());
        res.put("courseSelectionRound", admin.getCourseSelectionRound());
        return res;
    }

    @Override
    public String getCourseSelectionRound() {
        return adminMapper.findAdminByJobNum("000000").getCourseSelectionRound();
    }

    @Override
    public List<String> getCurrentTerm() {
        ArrayList<String> term = new ArrayList<>();
        Admin admin = adminMapper.findAdminByJobNum("000000");
        term.add(admin.getCurrentYear());
        term.add(admin.getCurrentTerm());
        return term;
    }

    @Override
    public Admin getAdminByJobNum(String jobNum) {
        return adminMapper.findAdminByJobNum(jobNum);
    }

    @Override
    public Admin getAdminByUuid(String uuid) {
        return adminMapper.findAdminByUuid(uuid);
    }

    @Override
    public Response<?> judgeLoginStatus(Admin admin, String pwd) {
        if(admin==null){
            return new Response<>(false,ConstVariable.USER_NOT_EXIST,null);
        }
        else if(!admin.getPassword().equals(pwd)){
            return new Response<>(false,ConstVariable.P_INVALID,null);
        }
        JSONObject res = new JSONObject();
        String token = TokenUtil.getToken(admin.getUuid(), ConstVariable.IDENTITY_ADMIN, admin.getJobNum(), admin.getUsername(), admin.getPassword());
        res.put("token", token);
        res.put("passwordChange", !admin.getPassword().equals(ConstVariable.INITIAL_P));
        return new Response<>(true,ConstVariable.LOGIN_SUCCESS,res);
    }
}
