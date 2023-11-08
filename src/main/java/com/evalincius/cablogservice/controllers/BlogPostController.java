package com.evalincius.cablogservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evalincius.cablogservice.models.BlogPost;
import com.evalincius.cablogservice.services.BlogPostService;

@RestController
@RequestMapping("api/blogpost")

public class BlogPostController {

    @Autowired 
    private BlogPostService blogPostService;

    @PostMapping()
    public BlogPost createBlogPost(@RequestBody BlogPost blogPost) {
        return blogPostService.createBlogPost(blogPost);
    }

    @GetMapping("/all")
    public List<BlogPost> getAllBlogPosts() {
        return blogPostService.getAllBlogPosts();
    }
    
}
