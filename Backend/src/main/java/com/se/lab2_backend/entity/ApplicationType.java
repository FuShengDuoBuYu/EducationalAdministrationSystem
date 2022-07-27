package com.se.lab2_backend.entity;

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
@Table(name = "application_type")
public class ApplicationType {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "application_type_id", length = 100, nullable = false)
    private String applicationTypeId;

    @Column(name = "name", length = 100 ,nullable = false)
    private String name;
}
