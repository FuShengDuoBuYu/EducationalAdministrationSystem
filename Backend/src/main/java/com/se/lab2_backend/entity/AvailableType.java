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
@Table(name = "available_type")
public class AvailableType{
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "available_type_id", length = 100, nullable = false)
    private String availableTypeId;

    @Column(name = "name", length = 100 ,nullable = false)
    private String name;
}
