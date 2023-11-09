package com.evalincius.cablogservice.models;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
public class Tag extends Audit{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String name;
}
