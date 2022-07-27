package com.se.lab2_backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.se.lab2_backend.entity.Teacher;
import com.se.lab2_backend.mapper.TeacherMapper;
import com.se.lab2_backend.service.TeacherService;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import com.se.lab2_backend.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.JobName;

import java.util.List;

import static com.se.lab2_backend.util.ConstVariable.LOGIN_SUCCESS;

@Service
public class TeacherServiceImpl implements TeacherService {
    TeacherMapper teacherMapper;

    @Autowired
    public TeacherServiceImpl(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @Override
    public List<Teacher> getAll() {
        return teacherMapper.findAll();
    }

    @Override
    public Teacher getTeacherByJobNum(String jobNum) {
        return teacherMapper.findTeacherByJobNum(jobNum);
    }

    @Override
    public Teacher getTeacherByUuid(String uuid) {
        return teacherMapper.findTeacherByUuid(uuid);
    }

    @Override
    @Transactional
    public Integer deleteTeacherByJobNum(String jobNum) {
        return teacherMapper.deleteTeacherByJobNum(jobNum);
    }

    @Override
    public Teacher getTeacherByIdcardNum(String idcardNum){
        return teacherMapper.findTeacherByIdcardNum(idcardNum);
    }
    @Override
    public Response<?> judgeLoginStatus(Teacher teacher, String pwd) {
        if(teacher==null){
            return new Response<>(false,"学工号不存在",null);
        }
        else if(!teacher.getPassword().equals(pwd)){
            return new Response<>(false,"密码错误",null);
        }
        JSONObject res = new JSONObject();
        String token = TokenUtil.getToken(teacher.getUuid(), ConstVariable.IDENTITY_TEACHER, teacher.getJobNum(), teacher.getUsername(), teacher.getPassword());
        res.put("token", token);
        res.put("passwordChange", !teacher.getPassword().equals(ConstVariable.INITIAL_P));
        return new Response<>(true,ConstVariable.LOGIN_SUCCESS,res);
    }

    @Override
    public Response<?> saveTeacher(Teacher teacher) {
        try {
            teacherMapper.save(teacher);
            return new Response<>(true,ConstVariable.REQUEST_SUCCESS,teacher);
        }catch (Exception exception){
            return new Response<>(false,ConstVariable.REQUEST_FAIL);
        }
    }

    @Override
    public void saveAll(List<Teacher> teachers){
        teacherMapper.saveAll(teachers);
    }
}
