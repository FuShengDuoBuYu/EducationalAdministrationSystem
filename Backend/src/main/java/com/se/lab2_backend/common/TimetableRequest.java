package com.se.lab2_backend.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TimetableRequest {
    private List<Integer> week;
    private Integer day;
    private List<Integer> sessions;
    private String classroomId;
}
