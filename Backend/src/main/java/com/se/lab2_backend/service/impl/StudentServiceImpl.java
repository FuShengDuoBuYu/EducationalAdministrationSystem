package com.se.lab2_backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.se.lab2_backend.entity.Student;
import com.se.lab2_backend.mapper.StudentMapper;
import com.se.lab2_backend.service.StudentService;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import com.se.lab2_backend.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static com.se.lab2_backend.util.ConstVariable.LOGIN_SUCCESS;

@Service
public class StudentServiceImpl implements StudentService {

    StudentMapper studentMapper;


    @Autowired
    public StudentServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public Student getStudentByJobNum(String jobNum) {
        return studentMapper.findStudentByJobNum(jobNum);
    }

    @Override
    public Student getStudentByUuid(String uuid) {
        return studentMapper.findStudentByUuid(uuid);
    }

    @Override
    @Transactional
    public Integer deleteStudentByJobNum(String jobNum) {
        return studentMapper.deleteStudentByJobNum(jobNum);
    }

    @Override
    public List<Student> getAll() {
        return studentMapper.findAll();
    }

    @Override
    public Student getStudentByIdcardNum(String idcardNum){
        return studentMapper.findStudentByIdcardNum(idcardNum);
    }
    @Override
    public Response<?> judgeLoginStatus(Student student, String pwd) {
        if(student==null){
            return new Response<>(false,"学工号不存在",null);
        }
        else if(!student.getPassword().equals(pwd)){
            return new Response<>(false,"密码错误",null);
        }
        JSONObject res = new JSONObject();
        String token = TokenUtil.getToken(student.getUuid(), ConstVariable.IDENTITY_STUDENT, student.getJobNum(), student.getUsername(), student.getPassword());
        res.put("token", token);
        res.put("passwordChange", !student.getPassword().equals(ConstVariable.INITIAL_P));
        return new Response<>(true,ConstVariable.LOGIN_SUCCESS,res);
    }

    @Override
    public Response<?> saveStudent(Student student) {
        try {
            studentMapper.save(student);
            return new Response<>(true,ConstVariable.REQUEST_SUCCESS,student);
        }catch (Exception exception){
            return new Response<>(false,ConstVariable.REQUEST_FAIL);
        }
    }

    @Override
    public void saveAll(List<Student> students){
        studentMapper.saveAll(students);
    }
}
