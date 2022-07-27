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
import java.io.Serializable;

@Getter
@Setter
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course_timetable")
public class Timetable implements Serializable {
    @EmbeddedId
    private TimetableKey timetableId;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false, updatable = false, insertable = false)
    private Classroom classroom;

    @ManyToOne
    @JsonIgnoreProperties({"courseId", "classHour", "credit", "capacity", "major", "teacher", "timetableList", "year", "term"})
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @JsonIgnore
    public int getWeek(){
        return timetableId.getWeek();
    }
    @JsonIgnore
    public int getDay(){
        return timetableId.getDay();
    }
    @JsonIgnore
    public int getSession(){
        return timetableId.getSession();
    }
    @JsonIgnore
    public String getYear(){
        return timetableId.getYear();
    }
    @JsonIgnore
    public String getTerm(){
        return timetableId.getTerm();
    }
    @JsonIgnore
    public void setTerm(String year, String term) {
        this.timetableId.setYear(year);
        this.timetableId.setTerm(term);
    }
}
