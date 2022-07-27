package com.se.lab2_backend.service;

import com.se.lab2_backend.entity.Admin;
import com.se.lab2_backend.entity.Student;
import com.se.lab2_backend.util.Response;

import java.util.List;

public interface StudentService {

    public Student getStudentByJobNum(String jobNum);

    public Student getStudentByIdcardNum(String idcardNum);

    public Student getStudentByUuid(String uuid);

    public Integer deleteStudentByJobNum(String jobNum);

    public List<Student> getAll();

    Response<?> judgeLoginStatus(Student student, String pwd);

    Response<?> saveStudent(Student student);

    public void saveAll(List<Student> students);
}
