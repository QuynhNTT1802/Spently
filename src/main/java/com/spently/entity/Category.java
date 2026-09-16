package com.spently.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "category_id")
    private String categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Setter(AccessLevel.NONE)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", length = 20, nullable = false)
    private String type;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private Integer status = 1;
}