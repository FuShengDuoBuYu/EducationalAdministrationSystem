package com.se.lab2_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_selection_application")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class CourseSelectionApplication {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "application_id", nullable = false)
    private String applicationId;

    @Column(name = "course_id", nullable = false)
    private String courseId;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "reason", nullable = false)
    private String reason;

    @ManyToOne
    @JoinColumn(name = "application_result_id")
    private ApplicationResult applicationResult;

    public CourseSelectionApplication(String courseId, String studentId, String reason, ApplicationResult applicationResult) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.reason = reason;
        this.applicationResult = applicationResult;
    }
}
