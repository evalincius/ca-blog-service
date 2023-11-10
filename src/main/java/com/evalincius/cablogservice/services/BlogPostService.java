package com.evalincius.cablogservice.services;

import java.util.List;

import com.evalincius.cablogservice.models.BlogPost;
import com.evalincius.cablogservice.models.SearchBlogPostCriteria;
import com.evalincius.cablogservice.models.UpdateBlogPostCriteria;

public interface BlogPostService {
    public BlogPost createBlogPost(BlogPost blogPost);

    public List<BlogPost> getAllBlogPosts();
       
    public List<BlogPost> filterBlogPosts(SearchBlogPostCriteria blogPostSearchCriteria);

    public BlogPost updateBlogPost(BlogPost blogPost);

    public BlogPost updateBlogPostCategory(UpdateBlogPostCriteria updateBlogPostCategoryCriteria);

    public BlogPost updateBlogPostTags(UpdateBlogPostCriteria updateBlogPostCriteria);

    public void deleteBlogPost(Integer blogPostId);
}
