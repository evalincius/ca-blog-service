package com.evalincius.cablogservice.services;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evalincius.cablogservice.models.Audit;
import com.evalincius.cablogservice.models.Author;
import com.evalincius.cablogservice.models.BlogPost;
import com.evalincius.cablogservice.models.SearchBlogPostCriteria;
import com.evalincius.cablogservice.models.Category;
import com.evalincius.cablogservice.models.Tag;
import com.evalincius.cablogservice.models.UpdateBlogPostCriteria;
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
        setOrUpdateAuditData(blogPost.getId(), blogPost);
        return blogPostRepository.save(blogPost);
    }

     private void setOrUpdateAuditData(Integer id, Audit audit){
        ZonedDateTime nowInUTC = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
        if(id != null) {
            audit.setUpdatedAt(nowInUTC);
        }else{
            audit.setCreatedAt(nowInUTC);
            audit.setUpdatedAt(nowInUTC);
        }
    }

    /**
     * Persist category if not exists else return the category already persisted
     * @param currentCategory
     * @return Category
     */
    private Category persistCategoryIfNotExists(Category currentCategory){
       
        if(currentCategory != null){
            Category category = currentCategory.getId() != null ? entityManager.find(Category.class, currentCategory.getId()): null;
            setOrUpdateAuditData(currentCategory.getId(), currentCategory);
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
     * @return Author
     */
    private Author persistAuthorIfNotExists(Author currentAuthor){
        if(currentAuthor != null){
            Author author = currentAuthor.getId() != null ? entityManager.find(Author.class, currentAuthor.getId()): null;
            setOrUpdateAuditData(currentAuthor.getId(), currentAuthor);

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
     * @return List
     */
    private List<Tag> persistTagsIfNotExists(List<Tag> listOfCurrentTags){
        if(listOfCurrentTags != null){
            listOfCurrentTags = listOfCurrentTags.stream().map(currentTag ->{
                Tag tag = currentTag.getId() != null ? entityManager.find(Tag.class, currentTag.getId()): null;
                setOrUpdateAuditData(currentTag.getId(), currentTag);
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
    public List<BlogPost> filterBlogPosts(SearchBlogPostCriteria blogPostSearchCriteria) {
        String tagsSearchString = blogPostSearchCriteria.getTitle() + "%";
        return blogPostRepository.findByFilterValues(tagsSearchString, blogPostSearchCriteria.getCategories(), blogPostSearchCriteria.getTags());
    }

    @Override
    public BlogPost updateBlogPost(BlogPost blogPost) {
        return blogPostRepository.save(blogPost);
    }
    
    /**
     * First finds BlogPost by Id.
     * Then finds Category by Id and set it on BlogPost
     * Finaly updates BlogPost updatedAt time and save it.
     * @param updateBlogPostCriteria
     * @return BlogPost
     */
    @Override
    public BlogPost updateBlogPostCategory(UpdateBlogPostCriteria updateBlogPostCriteria) {
        ZonedDateTime nowInUTC = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
        BlogPost blogPost = blogPostRepository.findById(updateBlogPostCriteria.getBlogPostId()).get();
        
        Category category = null;
        if(updateBlogPostCriteria.getCategoryId() != null){
            category = entityManager.find(Category.class, updateBlogPostCriteria.getCategoryId());
        }
        
        blogPost.setCategory(category);
        blogPost.setUpdatedAt(nowInUTC);
   
        return blogPostRepository.save(blogPost);
    }

    /**
     * First finds BlogPost by Id.
     * Then adds all tags from BlogPost that maches updateBlogPostCriteria.getTagIds() into blogPostTags list.
     * Removes matching TagId from the updateBlogPostCriteria.getTagIds()
     * 
     * Then for each remaining updateBlogPostCriteria.getTagIds() finds Tag by Id and add it to blogPostTags list.
     * Adds blogPostTags list to BlogPost
     * 
     * Finaly updates BlogPost updatedAt time and save it.
     * @param updateBlogPostCriteria
     * @return BlogPost
     */
    @Override
    public BlogPost updateBlogPostTags(UpdateBlogPostCriteria updateBlogPostCriteria) {
        ZonedDateTime nowInUTC = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);

        BlogPost blogPost = blogPostRepository.findById(updateBlogPostCriteria.getBlogPostId()).get();
        
        List<Tag> blogPostTags = new ArrayList<Tag>();

        //Find Tags that we should keep.
        if(blogPost.getTags() != null) {
            blogPost.getTags().stream().forEach(post -> {
                if(updateBlogPostCriteria.getTagIds() != null && updateBlogPostCriteria.getTagIds().contains(post.getId())) {
                    blogPostTags.add(post);
                    updateBlogPostCriteria.getTagIds().remove(post.getId());
                }
            });
        }
        //Find and add new tags. 
        if(updateBlogPostCriteria.getTagIds() != null) {
            updateBlogPostCriteria.getTagIds().stream().forEach(tagId -> {
                Tag newTag = entityManager.find(Tag.class, tagId);
                blogPostTags.add(newTag);
            });
        }


        blogPost.setTags(blogPostTags);
        blogPost.setUpdatedAt(nowInUTC);
   
        return blogPostRepository.save(blogPost);
    }

    @Override
    public void deleteBlogPost(Integer blogPostId) {
        blogPostRepository.deleteById(blogPostId);
    }

}
