package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "journal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "journal_id")
    private Long id;

    private String content;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "purchased_date")
    private java.time.LocalDate purchasedDate;

    private Integer rating;
    private String title;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
