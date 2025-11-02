package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    public CategoryController(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/category/{slug}")
    public String viewBySlug(@PathVariable("slug") String slug, Model model) {
        Category category = categoryRepository.findBySlug(slug).orElse(null);
        if (category == null) {
            return "redirect:/";
        }
        model.addAttribute("category", category);
        model.addAttribute("articles", articleRepository.findByCategoryOrderByCreatedAtDesc(category));
        return "category";
    }
}
