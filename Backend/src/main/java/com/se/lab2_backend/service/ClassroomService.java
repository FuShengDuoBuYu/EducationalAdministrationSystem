package com.se.lab2_backend.service;

import com.se.lab2_backend.entity.Classroom;
import com.se.lab2_backend.util.Response;

import java.util.List;

public interface ClassroomService {
    public Classroom getClassroomByClassroomId(String classroomId);
    public Classroom getClassroomByName(String classroomName);
    public List<Classroom> getAll();
    public Integer deleteClassroomByClassroomId(String classroomId);
    Response<?> saveClassroom(Classroom classroom);
}
