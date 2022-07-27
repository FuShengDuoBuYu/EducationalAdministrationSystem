package com.se.lab2_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "schedule")
public class Schedule {
    @Id
    @Column(name = "session", length = 100, nullable = false)
    private Integer session;

    @Column(name = "start_time", length = 100 ,nullable = false)
    private String startTime;

    @Column(name = "end_time", length = 100 ,nullable = false)
    private String endTime;
}
