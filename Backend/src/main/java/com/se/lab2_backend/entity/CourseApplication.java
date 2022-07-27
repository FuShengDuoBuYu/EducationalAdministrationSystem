package com.se.lab2_backend.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_application")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class CourseApplication {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "application_id", nullable = false)
    private String applicationId;

    @Column(name = "course_id", length = 36, nullable = true)
    private String courseId;

    @Column(name = "number", length = 8,nullable = false)
    private String number;

    @Column(name = "name", length = 18,nullable = false)
    private String name;

    @Column(name = "class_hour", length = 11,nullable = false)
    private int classHour;

    @Column(name = "credit", length = 11,nullable = false)
    private float credit;

    @Column(name = "capacity", length = 256,nullable = false)
    private int capacity;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JsonIgnoreProperties({"majorId"})
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JsonIgnoreProperties({"uuid", "idcardNum", "phoneNum", "email", "password", "status", "major"})
    @JoinColumn(name = "teacher_id", referencedColumnName = "uuid")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "application_type_id")
    private ApplicationType applicationType;

    @ManyToOne
    @JoinColumn(name = "application_result_id")
    private ApplicationResult applicationResult;

    @Column(name = "timetable_list", nullable = false)
    private String timetableList;

    @ManyToOne
    @JoinColumn(name = "available_type_id")
    private AvailableType availableType;

    @Column(name = "year")
    private String year;

    @Column(name = "term")
    private String term;

    @Column(name = "available_major_id_list", nullable = false)
    private String availableMajorIdList;

    @Transient
    private List<Major> availableMajors;

    public CourseApplication(String courseId, String number, String name, int classHour, float credit, int capacity, String description, Major major, Teacher teacher, ApplicationType applicationType, ApplicationResult applicationResult, String timetableList) {
        this.courseId = courseId;
        this.number = number;
        this.name = name;
        this.classHour = classHour;
        this.credit = credit;
        this.capacity = capacity;
        this.description = description;
        this.major = major;
        this.teacher = teacher;
        this.applicationType = applicationType;
        this.applicationResult = applicationResult;
        this.timetableList = timetableList;
    }

//    public CourseApplication(String number, String name, Integer classHour, Float credit, Integer capacity, String description, Major major, Teacher teacher, ApplicationType applicationType, String courseId) {
//        this.courseId = courseId;
//        this.number = number;
//        this.name = name;
//        this.classHour = classHour;
//        this.credit = credit;
//        this.capacity = capacity;
//        this.description = description;
//        this.major = major;
//        this.teacher = teacher;
//        this.applicationType = applicationType;
//    }
}
