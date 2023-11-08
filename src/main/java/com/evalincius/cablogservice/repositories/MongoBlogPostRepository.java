package com.evalincius.cablogservice.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.evalincius.cablogservice.models.BlogPost;

@Repository
public class MongoBlogPostRepository implements BlogPostRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public BlogPost createBlogPost(BlogPost blogPost) {
        return mongoTemplate.save(blogPost);
    }

    @Override
    public List<BlogPost> getAllBlogPosts() {
        return mongoTemplate.findAll(BlogPost.class);
    }
    
}
