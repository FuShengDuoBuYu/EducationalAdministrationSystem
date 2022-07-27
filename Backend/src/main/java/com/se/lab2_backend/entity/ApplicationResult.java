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
@Table(name = "application_result")
public class ApplicationResult {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "application_result_id", length = 100, nullable = false)
    private String applicationResultId;

    @Column(name = "name", length = 100 ,nullable = false)
    private String name;
}
