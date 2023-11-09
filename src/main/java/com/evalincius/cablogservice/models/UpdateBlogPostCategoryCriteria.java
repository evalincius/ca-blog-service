package com.evalincius.cablogservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBlogPostCategoryCriteria {
    private int blogPostId;
    private int categoryId;
}
