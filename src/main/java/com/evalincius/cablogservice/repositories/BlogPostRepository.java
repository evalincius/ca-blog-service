package com.evalincius.cablogservice.repositories;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.evalincius.cablogservice.models.BlogPost;

import jakarta.transaction.Transactional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer>  {
    
    /*
    * This SQL query retrieves data from the 'blog_post' table (aliased as 'bp') based on specified conditions:
    * 
    * 1. It performs a RIGHT JOIN with the 'category' table (aliased as 'c') on the 'category_id' column,
    *    and filters results based on the conditions that either the provided category list is NULL or
    *    the category name is in the provided list of categories (:categories).
    * 
    * 2. It then performs another RIGHT JOIN with a subquery ('t') that selects 'blog_post_id' from the
    *    'blog_post_tags' table (aliased as 'bpt'), joining with the 'tag' table on 'tags_id', and filtering
    *    results based on the conditions that either the provided tag list is NULL or the tag name is in the
    *    provided list of tags (:tags). The subquery is grouped by 'blog_post_id'.
    * 
    * 3. The main query joins the 'blog_post' table with the subquery ('t') on the 'id' column.
    * 
    * 4. The WHERE clause filters results based on the condition that either the provided title is NULL or
    *    the 'title' column in the 'blog_post' table matches the provided title (:title).
    * 
    * Overall, this query is designed to retrieve blog posts based on specified categories, tags, and title criteria.
    */
    @Query(value = """
        SELECT bp.* 
        FROM blog_post bp 
            RIGHT JOIN category c ON bp.category_id = c.id AND (COALESCE(:categories) IS NULL OR c.name IN (:categories))

                RIGHT JOIN (
                    SELECT bpt.blog_post_id FROM blog_post_tags bpt
                        RIGHT JOIN tag ON bpt.tags_id = tag.id AND (COALESCE(:tags) IS NULL OR tag.name IN (:tags))
                    GROUP BY bpt.blog_post_id
                ) t ON bp.id = t.blog_post_id
            
        WHERE ( :title IS NULL OR bp.title = :title )
        """, nativeQuery=true)
    public List<BlogPost> findByFilterValues(@Param("title") String title, @Param("categories") List<String> categories, @Param("tags") List<String> tags);

    @Modifying
    @Transactional
    @Query(value = "UPDATE blog_post bp SET bp.category_id = :category_id WHERE bp.id = :id", nativeQuery=true)
    public void updateCategory(@Param(value = "id") int id, @Param(value = "category_id") int categoryId);
}


