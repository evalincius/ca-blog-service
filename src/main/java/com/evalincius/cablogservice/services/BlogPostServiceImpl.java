package com.evalincius.cablogservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evalincius.cablogservice.models.Author;
import com.evalincius.cablogservice.models.BlogPost;
import com.evalincius.cablogservice.models.Category;
import com.evalincius.cablogservice.models.Tag;
import com.evalincius.cablogservice.repositories.BlogPostRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @PersistenceContext
    EntityManager entityManager;
    @Override
    @Transactional
    public BlogPost createBlogPost(BlogPost blogPost) {
        blogPost.setAuthor(persistAuthorIfNotExists(blogPost.getAuthor()));
        blogPost.setCategory(persistCategoryIfNotExists(blogPost.getCategory()));
        blogPost.setTags(persistTagsIfNotExists(blogPost.getTags()));
        return blogPostRepository.save(blogPost);
    }
    /**
     * Persist category if not exists else return the category already persisted
     * @param currentCategory
     * @return
     */
    private Category persistCategoryIfNotExists(Category currentCategory){
        if(currentCategory != null){
            Category category = currentCategory.getId() != null ? entityManager.find(Category.class, currentCategory.getId()): null;
            if(category != null ) {
                currentCategory = category;
            } else{
                entityManager.persist(currentCategory);
            }
        }
        return currentCategory;
    }
    /**
     * Persist author if not exists else return the author already persisted
     * @param currentAuthor
     * @return 
     */
    private Author persistAuthorIfNotExists(Author currentAuthor){
        if(currentAuthor != null){
            Author author = currentAuthor.getId() != null ? entityManager.find(Author.class, currentAuthor.getId()): null;
            if(author != null ) {
                currentAuthor = author;
            } else{
                entityManager.persist(currentAuthor);
            }
        }
        return currentAuthor;
    }

    /**
     * Persist tags if not exists else return the list of tags already persisted
     * @param listOfCurrentTags
     * @return
     */
    private List<Tag> persistTagsIfNotExists(List<Tag> listOfCurrentTags){
        if(listOfCurrentTags != null){
            listOfCurrentTags = listOfCurrentTags.stream().map(currentTag ->{
                Tag tag = currentTag.getId() != null ? entityManager.find(Tag.class, currentTag.getId()): null;
                if(tag != null ) {
                    currentTag = tag;
                } else{
                    entityManager.persist(currentTag);
                }
                return currentTag;
            }).toList();
        }
        return listOfCurrentTags;
    }

    @Override
    public List<BlogPost> getAllBlogPosts() {
        return (List<BlogPost>) blogPostRepository.findAll();
    }
    @Override
    public BlogPost updateBlogPost(BlogPost blogPost) {
        return blogPostRepository.save(blogPost);
    }

}
