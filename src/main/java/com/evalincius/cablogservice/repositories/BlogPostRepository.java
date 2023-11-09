package com.evalincius.cablogservice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.evalincius.cablogservice.models.BlogPost;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer>  {
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
}


