package com.se.lab2_backend.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TimetableVO {
    private Integer week;
    private Integer day;
    private List<Integer> sessions;
    private String buildingName;
    private String buildingId;
    private String classroomName;
    private String classroomId;
}
