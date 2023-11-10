package com.evalincius.cablogservice.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Builder
@Entity
public class BlogPost extends Audit {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String title;
    @Size(max = 1024, message = "Content cannot exceed 1024 characters")    
    @Size(max = 1024)
    private String content;
    @ManyToOne
    private Author author;
    private String image;
    @ManyToOne
    private Category category;
    @ManyToMany
    private List<Tag> tags;
}
