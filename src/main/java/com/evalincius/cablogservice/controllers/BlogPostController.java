package com.evalincius.cablogservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evalincius.cablogservice.models.BlogPost;
import com.evalincius.cablogservice.models.BlogPostSearchCriteria;
import com.evalincius.cablogservice.models.UpdateBlogPostCategoryCriteria;
import com.evalincius.cablogservice.services.BlogPostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/blogpost")
public class BlogPostController {

    @Autowired 
    private BlogPostService blogPostService;

    @PostMapping()
    public BlogPost createBlogPost(@Valid @RequestBody BlogPost blogPost) {
        return blogPostService.createBlogPost(blogPost);
    }    

    @PutMapping()
    public BlogPost updateBlogPost(@RequestBody BlogPost blogPost) {
        return blogPostService.updateBlogPost(blogPost);
    }

    @PutMapping("/category")
    public BlogPost updateBlogPostCategory(@RequestBody UpdateBlogPostCategoryCriteria updateBlogPostCategoryCriteria) {
        return blogPostService.updateBlogPostCategory(updateBlogPostCategoryCriteria);
    }


    @GetMapping("/all")
    public List<BlogPost> getAllBlogPosts() {
        return blogPostService.getAllBlogPosts();
    }

    @GetMapping()
    public List<BlogPost> filterBlogPosts(BlogPostSearchCriteria blogPostSearchCriteria) {
        return blogPostService.filterBlogPosts(blogPostSearchCriteria);
    }
    
}
