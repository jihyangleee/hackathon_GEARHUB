package com.example.demo.config;

import com.example.demo.entity.Article;
import com.example.demo.entity.Category;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.EquipmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final EquipmentRepository equipmentRepository;

    public DataSeeder(ArticleRepository articleRepository, CategoryRepository categoryRepository,
                      EquipmentRepository equipmentRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Previously the seeder created a "sample" category and a long sample article.
        // To avoid creating example/sample content in production, that behavior was removed.
        // If you need to restore sample data for demo purposes, reintroduce a controlled seeding flag.

        // Create one article per existing category (overview) based on current DB contents
        for (Category existing : categoryRepository.findAll()) {
            String overviewSlug = existing.getSlug() + "-overview";
            if (articleRepository.findBySlug(overviewSlug).isPresent()) continue;

            Article catArticle = new Article();
            catArticle.setTitle(existing.getName() + " — 카테고리 개요 및 관련 장비");
            catArticle.setSlug(overviewSlug);
            catArticle.setAuthor("Auto Seeder");
            catArticle.setCategory(existing);
            catArticle.setCreatedAt(LocalDateTime.now());
            catArticle.setUpdatedAt(LocalDateTime.now());
            StringBuilder catContent = new StringBuilder();
            catContent.append("카테고리: ").append(existing.getName()).append("\n\n");
            if (existing.getDescription() != null) catContent.append(existing.getDescription()).append("\n\n");
            catContent.append("이 카테고리와 관련된 주요 장비 목록:\n\n");
            // find equipments by category name
            try {
                for (var eq : equipmentRepository.findByCategory_NameIgnoreCase(existing.getName())) {
                    catContent.append("- ").append(eq.getName());
                    if (eq.getBrand() != null) catContent.append(" (").append(eq.getBrand()).append(")");
                    if (eq.getPrice() != null) catContent.append(" - ₩").append(eq.getPrice());
                    if (eq.getPurchaseUrl() != null && !eq.getPurchaseUrl().isBlank()) {
                        catContent.append("\n  구매: ").append(eq.getPurchaseUrl());
                    }
                    catContent.append("\n\n");
                }
            } catch (Exception ignored) {
                // If repository mapping is inconsistent, skip equipments
            }
            catContent.append("팁: 위 장비들은 사용 환경과 예산에 따라 선택하세요. 더 자세한 정보는 각 제품 설명을 확인하세요.\n");
            catArticle.setContent(catContent.toString());
            articleRepository.save(catArticle);
        }
    }
}
