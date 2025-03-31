package com.best.vet.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "product_subcategory_option",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_option_id"))
    private List<SubcategoryOption> subcategoryOptions;

    @Column(name = "option_value", nullable = false, length = 100)
    private String title;

    @Column(name = "href", length = 200)
    private String href;

    @Column(name = "icon", length = 300)
    private String icon;
}
