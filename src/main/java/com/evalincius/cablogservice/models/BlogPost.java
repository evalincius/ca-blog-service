package com.evalincius.cablogservice.models;

import java.time.ZonedDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogPost {
    private String id;
    private String title;
    private String content;
    private Author author;
    private String image;
    private String category;
    private Set<String> tags;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
