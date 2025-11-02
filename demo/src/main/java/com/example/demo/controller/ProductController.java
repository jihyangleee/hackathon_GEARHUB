package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ProductController(ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/product/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isEmpty()) {
            return "redirect:/";
        }
        Product p = opt.get();
        model.addAttribute("product", p);
        model.addAttribute("reviews", reviewRepository.findByProduct(p));
        return "product";
    }
    
    @PostMapping("/product/{id}/review")
    public String addReview(@PathVariable Long id, @RequestParam("content") String content, @RequestParam(value = "rating", required = false) Integer rating, jakarta.servlet.http.HttpSession session) {
        var opt = productRepository.findById(id);
        if (opt.isEmpty()) return "redirect:/";
        var userObj = session.getAttribute("user");
        if (userObj == null) return "redirect:/login";
        var user = (com.example.demo.entity.User) userObj;
        com.example.demo.entity.Review r = new com.example.demo.entity.Review();
        r.setProduct(opt.get());
        r.setUser(user);
        r.setContent(content);
        r.setRating(rating == null ? 5 : rating);
        reviewRepository.save(r);
        return "redirect:/product/" + id;
    }
}
