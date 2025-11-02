package com.example.demo.repository;

import com.example.demo.entity.Article;
import com.example.demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	List<Article> findByCategoryOrderByCreatedAtDesc(Category category);
	Optional<Article> findBySlug(String slug);
	List<Article> findTop6ByOrderByCreatedAtDesc();
}
