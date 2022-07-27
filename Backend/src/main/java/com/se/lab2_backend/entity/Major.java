package com.se.lab2_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "major")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Major {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "major_id", length = 100, nullable = false)
    private String majorId;

    //专业名
    @Column(name = "name", length = 100 ,nullable = false)
    private String name;

//    //学院id
//    @Column(name = "institute_id", length = 100 ,nullable = false)
//    private String instituteId;

//    @Nullable
    @ManyToOne
    @JsonIgnoreProperties({"majors"})
    @JoinColumn(name = "institute_id", nullable = false)
    private Institute institute;

    public Major(String majorName, Institute institute)
    {
        this.name=majorName;
        this.institute=institute;
    }
}
