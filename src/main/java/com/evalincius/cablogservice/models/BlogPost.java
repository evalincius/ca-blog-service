package com.evalincius.cablogservice.models;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BlogPost {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String title;
    private String content;
    @ManyToOne
    private Author author;
    private String image;
    @ManyToOne
    private Category category;
    @ManyToMany
    private List<Tag> tags;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
