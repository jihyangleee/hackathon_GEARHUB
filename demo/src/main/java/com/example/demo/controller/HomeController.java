package com.example.demo.controller;

import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.entity.Category;
import com.example.demo.entity.Article;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    public HomeController(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping({"/", "/search"})
    public String index(@RequestParam(value = "q", required = false) String q, Model model) {
        // For now, search targets article titles
        if (q != null && !q.isBlank()) {
            // simple title search over articles
            model.addAttribute("articles", articleRepository.findAll().stream().filter(a -> a.getTitle() != null && a.getTitle().toLowerCase().contains(q.toLowerCase())).toList());
            model.addAttribute("q", q);
        } else {
            model.addAttribute("articles", articleRepository.findTop6ByOrderByCreatedAtDesc());
        }
        model.addAttribute("categories", categoryRepository.findAll());
        return "index";
    }

    @GetMapping("/category")
    public String category(@RequestParam(value = "name", required = false) String name, Model model) {
        if (name != null && !name.isBlank()) {
            return "redirect:/category/" + name;
        }
        return "redirect:/";
    }
}
