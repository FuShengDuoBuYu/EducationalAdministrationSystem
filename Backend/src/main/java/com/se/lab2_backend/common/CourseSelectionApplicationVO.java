package com.se.lab2_backend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseSelectionApplicationVO {
    private String applicationId;
    private CourseVO course;
    private UserVO student;
    private String reason;
    private String applicationResult;
}
