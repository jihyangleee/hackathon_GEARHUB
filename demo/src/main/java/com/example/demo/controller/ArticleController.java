package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/article/{slug}")
    public String viewArticle(@PathVariable("slug") String slug, Model model) {
        Article article = articleRepository.findBySlug(slug).orElse(null);
        if (article == null) return "redirect:/";
        model.addAttribute("article", article);
        return "article";
    }
}
