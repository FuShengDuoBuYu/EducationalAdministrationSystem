package com.se.lab2_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "status")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Status {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "status_id", length = 100, nullable = false)
    private String statusId;

    //状态名
    @Column(name = "name", length = 100 ,nullable = false)
    private String name;

    //状态身份
    @JsonIgnore
    @Column(name = "identity", length = 100 ,nullable = false)
    private String identity;
}