package com.evalincius.cablogservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evalincius.cablogservice.models.BlogPost;
import com.evalincius.cablogservice.repositories.BlogPostRepository;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;
    @Override
    public BlogPost createBlogPost(BlogPost blogPost) {
        return blogPostRepository.createBlogPost(blogPost);
    }
    @Override
    public List<BlogPost> getAllBlogPosts() {
        return blogPostRepository.getAllBlogPosts();
    }
    @Override
    public BlogPost updateBlogPost(BlogPost blogPost) {
        return blogPostRepository.updateBlogPost(blogPost);
    }

}
