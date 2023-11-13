package com.evalincius.cablogservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.evalincius.cablogservice.models.Author;
import com.evalincius.cablogservice.models.BlogPost;
import com.evalincius.cablogservice.models.Category;
import com.evalincius.cablogservice.models.SearchBlogPostCriteria;
import com.evalincius.cablogservice.models.Tag;
import com.evalincius.cablogservice.models.UpdateBlogPostCriteria;
import com.evalincius.cablogservice.repositories.BlogPostRepository;
import com.evalincius.cablogservice.services.BlogPostServiceImpl;

import jakarta.persistence.EntityManager;

@SpringBootTest
public class BlogPostServiceImplTest {

    @Mock
    private BlogPostRepository mockBlogPostRepository;

    @Mock
    private EntityManager mockEntityManager;

    @InjectMocks   
    private BlogPostServiceImpl testee;

    @Test
    public void whenCallingGetAllBlogPosts_thenReturnCorrectBlogPosts() {
        List<BlogPost> blogPosts = new ArrayList<BlogPost>();
        blogPosts.add(BlogPost.builder().title("mock title 1").build());
        blogPosts.add(BlogPost.builder().title("mock title 2").build());
        
        when(mockBlogPostRepository.findAll()).thenReturn(blogPosts);

        List<BlogPost> results = testee.getAllBlogPosts();

        assertEquals(2, results.size());
        assertEquals(blogPosts, results);
    }


    @Test
    public void whenCallingFilterBlogPosts_thenReturnCorrectBlogPosts() {
        SearchBlogPostCriteria mockBlogPostCriteria = SearchBlogPostCriteria.builder()
            .title("mock title")
            .categories(Collections.singletonList("mock category"))
            .tags(Collections.singletonList("mock tag"))
            .build();

        List<BlogPost> blogPosts = new ArrayList<BlogPost>();
        blogPosts.add(BlogPost.builder().title("mock title 1").build());
        blogPosts.add(BlogPost.builder().title("mock title 2").build());
        
        when(mockBlogPostRepository.findByFilterValues(mockBlogPostCriteria.getTitle()+ "%", mockBlogPostCriteria.getCategories(), mockBlogPostCriteria.getTags())).thenReturn(blogPosts);

        List<BlogPost> results = testee.filterBlogPosts(mockBlogPostCriteria);

        assertEquals(2, results.size());
        assertEquals(blogPosts, results);
    }

    @Test
    public void givenTitleIsNull_whenCallingFilterBlogPosts_thenReturnCorrectBlogPosts() {
        SearchBlogPostCriteria mockBlogPostCriteria = SearchBlogPostCriteria.builder()
            .categories(Collections.singletonList("mock category"))
            .tags(Collections.singletonList("mock tag"))
            .build();

        List<BlogPost> blogPosts = new ArrayList<BlogPost>();
        blogPosts.add(BlogPost.builder().title("mock title 1").build());
        blogPosts.add(BlogPost.builder().title("mock title 2").build());
        
        when(mockBlogPostRepository.findByFilterValues(null, mockBlogPostCriteria.getCategories(), mockBlogPostCriteria.getTags())).thenReturn(blogPosts);

        List<BlogPost> results = testee.filterBlogPosts(mockBlogPostCriteria);

        assertEquals(2, results.size());
        assertEquals(blogPosts, results);
    }

    @Test
    public void givenCategoryTagAndAuthorNotYetExist_whenCallingCreateBlogPost_thenReturnCreatedBlogPost() {
        BlogPost mockBlogPost = BlogPost.builder()
            .title("mock title")
            .content("mock content")
            .category(Category.builder().name("mock category").build())
            .tags(Collections.singletonList(Tag.builder().name("mock tag").build()))
            .author(Author.builder().firstName("mock author").build())
            .build();

            when(mockBlogPostRepository.save(mockBlogPost)).thenReturn(mockBlogPost);

            BlogPost result = testee.createBlogPost(mockBlogPost);

            verify(mockEntityManager, times(1)).persist(mockBlogPost.getCategory());
            verify(mockEntityManager, times(1)).persist(mockBlogPost.getTags().get(0));
            verify(mockEntityManager, times(1)).persist(mockBlogPost.getAuthor());
            
            verify(mockBlogPostRepository, times(1)).save(mockBlogPost);

            assertNotNull(result.getUpdatedAt());
            assertNotNull(result.getCreatedAt());

            assertNotNull(result.getAuthor().getUpdatedAt());
            assertNotNull(result.getAuthor().getCreatedAt());

            assertNotNull(result.getCategory().getUpdatedAt());
            assertNotNull(result.getCategory().getCreatedAt());

            assertNotNull(result.getTags().get(0).getUpdatedAt());
            assertNotNull(result.getTags().get(0).getCreatedAt());
    }

    @Test
    public void givenCategoryTagAndAuthorAlreadyExist_whenCallingCreateBlogPost_thenReturnCreatedBlogPost() {
        BlogPost mockBlogPost = BlogPost.builder()
            .title("mock title")
            .content("mock content")
            .category(Category.builder().id(1).name("mock category").build())
            .tags(Collections.singletonList(Tag.builder().id(1).name("mock tag").build()))
            .author(Author.builder().id(1).firstName("mock author").build())
            .build();
            

            when(mockBlogPostRepository.save(mockBlogPost)).thenReturn(mockBlogPost);
            when(mockEntityManager.find(Author.class, mockBlogPost.getAuthor().getId())).thenReturn(mockBlogPost.getAuthor().toBuilder().build());
            when(mockEntityManager.find(Category.class, mockBlogPost.getCategory().getId())).thenReturn(mockBlogPost.getCategory().toBuilder().build());
            when(mockEntityManager.find(Tag.class, mockBlogPost.getTags().get(0).getId())).thenReturn(mockBlogPost.getTags().get(0).toBuilder().build());

            BlogPost result = testee.createBlogPost(mockBlogPost);

            verify(mockEntityManager, times(0)).persist(mockBlogPost.getCategory());
            verify(mockEntityManager, times(0)).persist(mockBlogPost.getTags().get(0));
            verify(mockEntityManager, times(0)).persist(mockBlogPost.getAuthor());
            
            verify(mockBlogPostRepository, times(1)).save(mockBlogPost);

            assertNotNull(result.getUpdatedAt());
            assertNotNull(result.getCreatedAt());

            assertNull(result.getAuthor().getUpdatedAt());
            assertNull(result.getAuthor().getCreatedAt());

            assertNull(result.getCategory().getUpdatedAt());
            assertNull(result.getCategory().getCreatedAt());

            assertNull(result.getTags().get(0).getUpdatedAt());
            assertNull(result.getTags().get(0).getCreatedAt());
    }

    @Test
    public void whenCallingupdateBlogPost_thenReturnUpdatedBlogPost() {
        BlogPost mockBlogPost = BlogPost.builder()
            .id(1)
            .title("mock title")
            .content("mock content")
            .build();

        when(mockBlogPostRepository.save(mockBlogPost)).thenReturn(mockBlogPost);

        BlogPost result = testee.updateBlogPost(mockBlogPost);

        verify(mockBlogPostRepository, times(1)).save(mockBlogPost);

        assertNotNull(result.getUpdatedAt());
    }

    @Test
    public void givenAddingNewTag_whenCallingupdateBlogPostTags_thenReturnUpdatedBlogPost() {
        ArrayList<Integer> listOfTagIds = new ArrayList<Integer>();
        listOfTagIds.add(1);
        listOfTagIds.add(2);

        UpdateBlogPostCriteria mockUpdateBlogPostCriteria = UpdateBlogPostCriteria.builder()
        .blogPostId(1)
        .tagIds(listOfTagIds)
        .build();

        Tag tagToAdd = Tag.builder().id(2).name("mock tag 2").build();

        BlogPost mockBlogPost = BlogPost.builder()
            .id(1)
            .tags(Collections.singletonList(Tag.builder().id(1).name("mock tag").build()))
            .build();

        when(mockBlogPostRepository.findById(mockBlogPost.getId())).thenReturn(Optional.of(mockBlogPost));
        when(mockBlogPostRepository.save(mockBlogPost)).thenReturn(mockBlogPost);
        when(mockEntityManager.find(Tag.class, tagToAdd.getId())).thenReturn(tagToAdd);

        BlogPost result = testee.updateBlogPostTags(mockUpdateBlogPostCriteria);

        assertEquals(2, result.getTags().size());
        assertTrue(result.getTags().contains(tagToAdd));
   }

    @Test
    public void givenRemovingTag_whenCallingupdateBlogPostTags_thenReturnUpdatedBlogPost() {
        ArrayList<Integer> listOfTagIds = new ArrayList<Integer>();
        listOfTagIds.add(1);

        UpdateBlogPostCriteria mockUpdateBlogPostCriteria = UpdateBlogPostCriteria.builder()
        .blogPostId(1)
        .tagIds(listOfTagIds)
        .build();

        BlogPost mockBlogPost = BlogPost.builder()
            .id(1)
            .tags(Arrays.asList(Tag.builder().id(1).name("mock tag").build(), Tag.builder().id(2).name("mock tag 2").build()))
            .build();

        when(mockBlogPostRepository.findById(mockBlogPost.getId())).thenReturn(Optional.of(mockBlogPost));
        when(mockBlogPostRepository.save(mockBlogPost)).thenReturn(mockBlogPost);

        BlogPost result = testee.updateBlogPostTags(mockUpdateBlogPostCriteria);

        assertEquals(1, result.getTags().size());
       }
    
    @Test
    public void whenCallingDeleteBlogPost_thenReturnOk() {
        testee.deleteBlogPost(1);
        verify(mockBlogPostRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenCallingUpdateBlogPostCategory_thenReturnUpdatedBlogPost() {
        UpdateBlogPostCriteria mockUpdateBlogPostCriteria = UpdateBlogPostCriteria.builder()
        .blogPostId(1)
        .categoryId(2)
        .build();
        
        Category categoryToUpdate = Category.builder().id(2).name("mock category 2").build();

        BlogPost mockBlogPost = BlogPost.builder()
            .id(1)
            .category(Category.builder().id(1).name("mock category").build())
            .build();

        when(mockEntityManager.find(Category.class, mockUpdateBlogPostCriteria.getCategoryId())).thenReturn(categoryToUpdate);    

        when(mockBlogPostRepository.findById(mockUpdateBlogPostCriteria.getBlogPostId())).thenReturn(Optional.of(mockBlogPost));
        when(mockBlogPostRepository.save(mockBlogPost)).thenReturn(mockBlogPost);

        BlogPost result = testee.updateBlogPostCategory(mockUpdateBlogPostCriteria);

        assertEquals(2, result.getCategory().getId());
        assertEquals("mock category 2", result.getCategory().getName());
    }
}
