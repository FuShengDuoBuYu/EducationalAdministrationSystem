package com.se.lab2_backend.service;

import com.alibaba.fastjson.JSONObject;
import com.se.lab2_backend.entity.Admin;
import com.se.lab2_backend.entity.Schedule;
import com.se.lab2_backend.util.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {

    public Admin getAdminByJobNum(String jobNum);

    public Admin getAdminByUuid(String uuid);

    public Admin saveAdmin(Admin admin);

    public JSONObject getCourseSelection();

    public String getCourseSelectionRound();

    public List<String> getCurrentTerm();

    Response<?> judgeLoginStatus(Admin admin,String pwd);
}
