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
@Table(name = "course_major")
public class CourseMajor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JsonIgnore
    @Column(name = "course_id")
    private String courseId;

    @ManyToOne
    @JsonIgnoreProperties({"majorId"})
    @JoinColumn(name = "major_id")
    private Major major;

    public CourseMajor(String courseId, Major major) {
        this.courseId = courseId;
        this.major = major;
    }
}
