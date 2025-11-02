package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.ReviewRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageController {

    private final FavoriteRepository favoriteRepository;
    private final ReviewRepository reviewRepository;

    public MypageController(FavoriteRepository favoriteRepository, ReviewRepository reviewRepository) {
        this.favoriteRepository = favoriteRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        Object u = session.getAttribute("user");
        if (u == null) {
            return "redirect:/login";
        }
        User user = (User) u;
        model.addAttribute("user", user);
        model.addAttribute("favorites", favoriteRepository.findByUser(user));
    model.addAttribute("myReviews",
        reviewRepository.findAll().stream()
            .filter(r -> r.getUser() != null && r.getUser().getId() != null && r.getUser().getId().equals(user.getId()))
            .toList());
        return "mypage";
    }
}
