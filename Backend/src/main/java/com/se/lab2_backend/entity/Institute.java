package com.se.lab2_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.models.auth.In;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "institute")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Institute {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "institute_id", length = 100, nullable = false)
    private String instituteId;

    //学院名
    @Column(name = "name", length = 100 ,nullable = false)
    private String name;

    @JsonIgnoreProperties({"institute"})
    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "institute_id", nullable = false, updatable = false, insertable = false)
    private List<Major> majors;
    public Institute(String name){
        this.name=name;
    }
}

