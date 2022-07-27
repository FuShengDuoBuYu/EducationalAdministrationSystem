package com.se.lab2_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "course_info")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Course {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "course_id", length = 36, nullable = false)
    private String courseId;

    @Column(name = "number", length = 8,nullable = false)
    private String number;

    @Column(name = "identifier", length = 256 ,nullable = false)
    private String identifier;

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
    @JoinColumn(name = "available_type_id")
    private AvailableType availableType;

    @ManyToOne
    @JsonIgnoreProperties({"uuid", "idcardNum", "phoneNum", "email", "password", "status"})
    @JoinColumn(name = "teacher_id", referencedColumnName = "uuid")
    private Teacher teacher;

    @OneToMany(fetch=FetchType.EAGER)
    @JsonIgnoreProperties({"course"})
    @JoinColumn(name = "course_id", nullable = false, updatable = false, insertable = false)
    private List<Timetable> timetableList;

    @Transient
    private List<Major> availableMajors;

    @Column(name = "year")
    private String year;

    @Column(name = "term")
    private String term;

    @Column(name = "finished")
    private boolean finished;

    public Course(String number, String name, int classHour, Float credit, int capacity, String description, Major major, Teacher teacher) {
        this.number = number;
        this.name = name;
        this.classHour = classHour;
        this.credit = credit;
        this.capacity = capacity;
        this.description = description;
        this.major = major;
        this.teacher = teacher;
    }

    public Course(String number, String name, int classHour, float credit, int capacity, String description, Major major, Teacher teacher, AvailableType availableType, String year, String term) {
        this.number = number;
        this.name = name;
        this.classHour = classHour;
        this.credit = credit;
        this.capacity = capacity;
        this.description = description;
        this.major = major;
        this.teacher = teacher;
        this.availableType = availableType;
        this.year = year;
        this.term = term;
    }
//    public Course(String courseId, String number, String name, int classHour, Float credit, int capacity, String description, Major major, Teacher teacher) {
//        this.courseId = courseId;
//        this.number = number;
//        this.name = name;
//        this.classHour = classHour;
//        this.credit = credit;
//        this.capacity = capacity;
//        this.description = description;
//        this.major = major;
//        this.teacher = teacher;
//    }
}