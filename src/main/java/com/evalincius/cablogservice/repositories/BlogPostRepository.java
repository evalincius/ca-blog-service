package com.evalincius.cablogservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evalincius.cablogservice.models.BlogPost;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer>  {
    
}
