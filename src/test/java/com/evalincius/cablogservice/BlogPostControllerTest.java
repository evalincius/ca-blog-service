package com.evalincius.cablogservice;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.evalincius.cablogservice.config.HeaderUserFilter;
import com.evalincius.cablogservice.config.WebSecurityConfig;
import com.evalincius.cablogservice.controllers.BlogPostController;
import com.evalincius.cablogservice.models.BlogPost;
import com.evalincius.cablogservice.models.SearchBlogPostCriteria;
import com.evalincius.cablogservice.models.UpdateBlogPostCriteria;
import com.evalincius.cablogservice.services.BlogPostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.ObjectWriter; 


@WebMvcTest(BlogPostController.class)
@ContextConfiguration(classes={BlogPostController.class, WebSecurityConfig.class, HeaderUserFilter.class})

class BlogPostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BlogPostService service;

	@Test
	void whenCallingGetAllBlogPosts_thenReturnJsonArray() throws Exception {
		when(service.getAllBlogPosts()).thenReturn(Collections.emptyList());
		this.mockMvc.perform(
			get("/api/blogpost/all")
			.header("X-User", "user")
			).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void whenCallingFilterBlogPosts_thenReturnJsonArray() throws Exception {
		SearchBlogPostCriteria searchBlogPostCriteria = new SearchBlogPostCriteria();
		searchBlogPostCriteria.setTitle("Test");
		searchBlogPostCriteria.setCategories(Arrays.asList("Test1", "Test2"));
		searchBlogPostCriteria.setTags(Arrays.asList("Test1", "Test2"));

		when(service.filterBlogPosts(searchBlogPostCriteria)).thenReturn(Collections.emptyList());
		this.mockMvc.perform(
			get("/api/blogpost?title=Test&categories=Test1&categories=Test2&tags=Test1&tags=Test2")
			.header("X-User", "user")
			).andDo(print()).andExpect(status().isOk());
		
		verify(service, times(1)).filterBlogPosts(searchBlogPostCriteria);

	}

	@Test
	void whenCallingDeleteBlogPost_whenUserIsAdmin_thenReturnOk() throws Exception {
		doNothing().when(service).deleteBlogPost(1);
		this.mockMvc.perform(
			delete("/api/blogpost/1")
			.header("X-User", "admin")
		).andDo(print()).andExpect(status().isOk());
	}


	@Test
	void whenCallingDeleteBlogPost_whenUserIsUser_thenReturnForbiden() throws Exception {
		doNothing().when(service).deleteBlogPost(1);
		this.mockMvc.perform(
			delete("/api/blogpost/1")
			.header("X-User", "User")
		).andDo(print()).andExpect(status().is4xxClientError());
	}

	@Test
	void givenCorrectLengthOfContent_whenCreateBlogPostCalled_thenReturnOk() throws Exception {
		BlogPost mockBlogPost = new BlogPost();
		String mockkContent = new String(new char[35]).replace('\0', 'a');
		mockBlogPost.setContent(mockkContent);	

		when(service.createBlogPost(mockBlogPost)).thenReturn(mockBlogPost);

		RequestBuilder request = MockMvcRequestBuilders
            .post("/api/blogpost")
			.header("X-User", "user")
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(mockBlogPost))
            .contentType(MediaType.APPLICATION_JSON);

		this.mockMvc.perform(request).andExpect(status().isOk());
	}

	@Test
	void givenLengthOfContentMoreThan1024Chars_whenCreateBlogPostCalled_thenthrowException() throws Exception {
		BlogPost mockBlogPost = new BlogPost();
		String mockkContent = new String(new char[1025]).replace('\0', 'a');
		mockBlogPost.setContent(mockkContent);	

		when(service.createBlogPost(mockBlogPost)).thenReturn(mockBlogPost);

		RequestBuilder request = MockMvcRequestBuilders
            .post("/api/blogpost")
			.header("X-User", "user")
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(mockBlogPost))
            .contentType(MediaType.APPLICATION_JSON);

		this.mockMvc.perform(request)
		.andExpect(status().isBadRequest())
      	.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
	}

	@Test
	void givenCorrectLengthOfContent_whenUpdateBlogPostCalled_thenReturnOk() throws Exception {
		BlogPost mockBlogPost = new BlogPost();
		String mockkContent = new String(new char[35]).replace('\0', 'a');
		mockBlogPost.setContent(mockkContent);	

		when(service.createBlogPost(mockBlogPost)).thenReturn(mockBlogPost);

		RequestBuilder request = MockMvcRequestBuilders
            .put("/api/blogpost")
			.header("X-User", "user")
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(mockBlogPost))
            .contentType(MediaType.APPLICATION_JSON);

		this.mockMvc.perform(request).andExpect(status().isOk());
	}

	@Test
	void givenLengthOfContentMoreThan1024Chars_whenUpdateBlogPostCalled_thenthrowException() throws Exception {
		BlogPost mockBlogPost = new BlogPost();
		String mockkContent = new String(new char[1025]).replace('\0', 'a');
		mockBlogPost.setContent(mockkContent);	

		when(service.createBlogPost(mockBlogPost)).thenReturn(mockBlogPost);

		RequestBuilder request = MockMvcRequestBuilders
            .put("/api/blogpost")
			.header("X-User", "user")
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(mockBlogPost))
            .contentType(MediaType.APPLICATION_JSON);

		this.mockMvc.perform(request)
		.andExpect(status().isBadRequest())
      	.andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
	}

	@Test
	void whenUpdateBlogPostCategory_thenReturnOk() throws Exception {
		UpdateBlogPostCriteria mockUpdateBlogPostCriteria = new UpdateBlogPostCriteria();
		BlogPost mockBlogPost = new BlogPost();

		when(service.updateBlogPostCategory(mockUpdateBlogPostCriteria)).thenReturn(mockBlogPost);

		RequestBuilder request = MockMvcRequestBuilders
            .put("/api/blogpost/category")
			.header("X-User", "user")
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(mockUpdateBlogPostCriteria))
            .contentType(MediaType.APPLICATION_JSON);

		this.mockMvc.perform(request)
		.andExpect(status().isOk());
	}

	@Test
	void whenUpdateBlogPostTags_thenReturnOk() throws Exception {
		UpdateBlogPostCriteria mockUpdateBlogPostCriteria = new UpdateBlogPostCriteria();
		BlogPost mockBlogPost = new BlogPost();

		when(service.updateBlogPostCategory(mockUpdateBlogPostCriteria)).thenReturn(mockBlogPost);

		RequestBuilder request = MockMvcRequestBuilders
            .put("/api/blogpost/tags")
			.header("X-User", "user")
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(mockUpdateBlogPostCriteria))
            .contentType(MediaType.APPLICATION_JSON);

		this.mockMvc.perform(request)
		.andExpect(status().isOk());
	}

	private String toJson(Object object) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = "";
		try {
			json = ow.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}