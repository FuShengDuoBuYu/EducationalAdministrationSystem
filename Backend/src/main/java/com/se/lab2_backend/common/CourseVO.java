package com.se.lab2_backend.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.se.lab2_backend.entity.Major;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CourseVO {
    private String courseId;
    private String number;
    private String identifier;
    private String name;
    private Integer classHour;
    private Float credit;
    private Integer capacity;
    private String description;
    private String teacherName;
    private String teacherId;
    private String major;
    private String majorId;
    private String availableType;
    private String availableTypeId;
    private String institute;
    private String instituteId;
    private String year;
    private String term;
    private List<TimetableVO> timetableList;
    // 每个元素的第一个是InstituteId 第二个是MajorId
    private List<List<String>> availableMajorList;
    private Integer selectedStudents;
    private boolean finished;

    public CourseVO(String courseId, String number, String identifier, String name, Integer classHour, Float credit, Integer capacity, String description, String teacherName, String teacherId, String major, String majorId, String availableType, String availableTypeId, String institute, String instituteId, List<TimetableVO> timetableList) {
        this.courseId = courseId;
        this.number = number;
        this.identifier = identifier;
        this.name = name;
        this.classHour = classHour;
        this.credit = credit;
        this.capacity = capacity;
        this.description = description;
        this.teacherName = teacherName;
        this.teacherId = teacherId;
        this.major = major;
        this.majorId = majorId;
        this.availableType = availableType;
        this.availableTypeId = availableTypeId;
        this.institute = institute;
        this.instituteId = instituteId;
        this.timetableList = timetableList;
    }
}