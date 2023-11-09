package com.evalincius.cablogservice.services;

import java.util.List;

import com.evalincius.cablogservice.models.BlogPost;
import com.evalincius.cablogservice.models.BlogPostSearchCriteria;

public interface BlogPostService {
    public BlogPost createBlogPost(BlogPost blogPost);

    public List<BlogPost> getAllBlogPosts();

    public BlogPost updateBlogPost(BlogPost blogPost);

    public List<BlogPost> filterBlogPosts(BlogPostSearchCriteria blogPostSearchCriteria);
}
