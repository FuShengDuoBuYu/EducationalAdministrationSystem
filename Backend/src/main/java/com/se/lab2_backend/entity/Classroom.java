package com.se.lab2_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "classroom")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Classroom {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "classroom_id", length = 100, nullable = false)
    private String classroomId;

    @Column(name = "name", length = 100 ,nullable = false)
    private String name;

    @Column(name = "capacity", length = 11, nullable = false)
    private int capacity;

    //    @Nullable
    @ManyToOne
    @JsonIgnoreProperties({"classrooms"})
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    public Classroom(String classroomName, int capacity, Building building)
    {
        this.name=classroomName;
        this.capacity=capacity;
        this.building=building;
    }

    @Override
    public String toString() {
        return building.getName()+ " " + building.getBuildingId() + " " + name + " " + classroomId;
    }
}
