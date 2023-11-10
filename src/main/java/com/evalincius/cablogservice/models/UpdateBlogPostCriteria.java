package com.evalincius.cablogservice.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBlogPostCriteria {
    private Integer blogPostId;
    private Integer categoryId;
    private List<Integer> tagIds;
}
