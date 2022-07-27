package com.se.lab2_backend.service;

import com.se.lab2_backend.entity.Admin;
import com.se.lab2_backend.entity.Teacher;
import com.se.lab2_backend.util.Response;

import java.util.List;

public interface TeacherService {

    public List<Teacher> getAll();
    public Teacher getTeacherByJobNum(String jobNum);
    public Teacher getTeacherByIdcardNum(String idcardNum);
    public Teacher getTeacherByUuid(String uuid);
    public Integer deleteTeacherByJobNum(String jobNum);
    Response<?> judgeLoginStatus(Teacher teacher, String pwd);
    public Response<?> saveTeacher(Teacher teacher);

    public void saveAll(List<Teacher> teachers);
}
