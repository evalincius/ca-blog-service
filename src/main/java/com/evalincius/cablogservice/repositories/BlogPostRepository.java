package com.evalincius.cablogservice.repositories;

import java.util.List;

import com.evalincius.cablogservice.models.BlogPost;

public interface BlogPostRepository {
    public BlogPost createBlogPost(BlogPost blogPost);

    public List<BlogPost> getAllBlogPosts();
}
