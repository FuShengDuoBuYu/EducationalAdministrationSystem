package com.se.lab2_backend.service.impl;

import com.se.lab2_backend.entity.Classroom;
import com.se.lab2_backend.mapper.ClassroomMapper;
import com.se.lab2_backend.service.ClassroomService;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    ClassroomMapper classroomMapper;
    @Autowired
    public ClassroomServiceImpl(ClassroomMapper classroomMapper) {
        this.classroomMapper = classroomMapper;
    }

    @Override
    public Classroom getClassroomByClassroomId(String classroomId) {
        return classroomMapper.findClassroomByClassroomId(classroomId)
                .orElseThrow(() -> new RuntimeException("教室" + "id" + "不存在！"));
    }

    @Override
    public Classroom getClassroomByName(String classroomName) {
        return classroomMapper.findClassroomByName(classroomName)
                .orElseThrow(() -> new RuntimeException("教室" + classroomName + "不存在！"));
    }

    @Override
    public List<Classroom> getAll() {
        return classroomMapper.findAll();
    }


    @Override
    @Transactional
    public Integer deleteClassroomByClassroomId(String classroomId) { return classroomMapper.deleteClassroomByClassroomId(classroomId);}

    @Override
    public Response<?> saveClassroom(Classroom classroom){
        try {
            classroomMapper.save(classroom);
            return new Response<>(true, ConstVariable.REQUEST_SUCCESS);
        }catch (Exception exception){
            return new Response<>(false,ConstVariable.REQUEST_FAIL);
        }
    }
    
//    @Override
//    public Integer deleteClassroomsByInstituteId(String instituteId){ return classroomMapper.deleteClassroomsByInstituteId(instituteId);}
}
