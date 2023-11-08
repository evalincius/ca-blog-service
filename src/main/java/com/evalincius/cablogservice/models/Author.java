package com.evalincius.cablogservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private String firstName;
    private String lastName;
    private String profileImage;
    private String email;
    private String bio;
}
