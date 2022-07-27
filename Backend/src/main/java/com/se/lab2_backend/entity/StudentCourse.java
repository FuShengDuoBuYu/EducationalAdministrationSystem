package com.se.lab2_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_course")
public class StudentCourse{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "student_id")
    private String studentId;

    @Column(name = "course_id")
    private String courseId;

    //状态
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    public StudentCourse(String studentId, String courseId, Status status) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = status;
    }
}
