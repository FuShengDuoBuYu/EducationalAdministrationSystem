package com.se.lab2_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "building")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Building {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "building_id", length = 100, nullable = false)
    private String buildingId;

    @Column(name = "name", length = 100 ,nullable = false)
    private String name;

    @JsonIgnoreProperties({"building"})
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.REFRESH})
    @JoinColumn(name = "building_id", nullable = false, updatable = false, insertable = false)
    private List<Classroom> classrooms;

//    @Transient
//    private Integer classroomNum;

    public Building(String name){
        this.name=name;
    }
}
