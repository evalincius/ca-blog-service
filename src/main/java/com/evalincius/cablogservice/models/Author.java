package com.evalincius.cablogservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String lastName;
    private String profileImage;
    private String email;
    private String bio;
}
