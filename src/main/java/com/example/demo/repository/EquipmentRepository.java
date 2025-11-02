package com.example.demo.repository;

import com.example.demo.entity.Article;
import com.example.demo.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
	List<Equipment> findByArticle(Article article);
	List<Equipment> findByCategory_NameIgnoreCase(String categoryName);
}
