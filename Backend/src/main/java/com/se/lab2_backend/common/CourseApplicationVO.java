package com.se.lab2_backend.common;

import com.alibaba.fastjson.JSONArray;
import com.se.lab2_backend.entity.Major;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CourseApplicationVO {
    private String applicationId;
    private String courseId;
    private String number;
    private String name;
    private Integer classHour;
    private Float credit;
    private Integer capacity;
    private String description;
    private String teacherName;
    private String teacherId;
    private String major;
    private String majorId;
    private String institute;
    private String instituteId;
    private String applicationType;
    private String applicationResult;
    private List<TimetableVO> timetableList;

    private String availableType;
    private String availableTypeId;
    private String year;
    private String term;
    // 每个元素的第一个是InstituteId 第二个是MajorId
    private List<List<String>> availableMajorList;
}
