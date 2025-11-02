package com.example.demo.controller;

import com.example.demo.entity.Favorite;
import com.example.demo.entity.User;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    public FavoriteController(FavoriteRepository favoriteRepository, ProductRepository productRepository) {
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/favorite/toggle")
    public String toggle(@RequestParam("productId") Long productId, HttpSession session) {
        Object u = session.getAttribute("user");
        if (u == null) return "redirect:/login";
        User user = (User) u;
        var opt = favoriteRepository.findByUserAndProductId(user, productId);
        if (opt.isPresent()) {
            favoriteRepository.delete(opt.get());
        } else {
            var pOpt = productRepository.findById(productId);
            if (pOpt.isEmpty()) return "redirect:/";
            Favorite f = new Favorite();
            f.setUser(user);
            f.setProduct(pOpt.get());
            favoriteRepository.save(f);
        }
        return "redirect:/product/" + productId;
    }
}
