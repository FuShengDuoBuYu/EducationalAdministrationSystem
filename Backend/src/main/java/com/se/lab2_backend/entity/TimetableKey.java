package com.se.lab2_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class TimetableKey implements Serializable {

    @Column(name = "week")
    private int week;

    @Column(name = "day")
    private int day;

    @Column(name = "session")
    private int session;

    @JsonIgnore
    @Column(name = "classroom_id")
    private String classroomId;

    @Column(name = "year")
    private String year;

    @Column(name = "term")
    private String term;
}
