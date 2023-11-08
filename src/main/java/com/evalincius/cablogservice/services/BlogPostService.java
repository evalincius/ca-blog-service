package com.evalincius.cablogservice.services;

import java.util.List;

import com.evalincius.cablogservice.models.BlogPost;

public interface BlogPostService {
    public BlogPost createBlogPost(BlogPost blogPost);

    public List<BlogPost> getAllBlogPosts();

    public BlogPost updateBlogPost(BlogPost blogPost);
}
