package com.evalincius.cablogservice.models;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBlogPostCriteria {
    private String title;
    private List<String> categories;
    private List<String> tags;
}