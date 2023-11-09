package com.evalincius.cablogservice.services;

import java.util.List;

import com.evalincius.cablogservice.models.BlogPost;
import com.evalincius.cablogservice.models.BlogPostSearchCriteria;
import com.evalincius.cablogservice.models.UpdateBlogPostCategoryCriteria;

public interface BlogPostService {
    public BlogPost createBlogPost(BlogPost blogPost);

    public List<BlogPost> getAllBlogPosts();
       
    public List<BlogPost> filterBlogPosts(BlogPostSearchCriteria blogPostSearchCriteria);

    public BlogPost updateBlogPost(BlogPost blogPost);

    public BlogPost updateBlogPostCategory(UpdateBlogPostCategoryCriteria updateBlogPostCategoryCriteria);

    public void deleteBlogPost(Integer blogPostId);
}
