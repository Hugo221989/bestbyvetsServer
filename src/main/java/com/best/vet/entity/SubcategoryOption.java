package com.best.vet.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "subcategory_option")
@Data
@NoArgsConstructor
public class SubcategoryOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subcategory_type_id", nullable = false)
    private SubcategoryType subcategoryType;

    // Valor de la opción (por ejemplo, 'Joven-adulto', 'alergias', etc.)
    @Column(name = "option_value", nullable = false, length = 100)
    private String optionValue;

    @Column(name = "href", length = 200)
    private String href;

    @Column(name = "icon", length = 100)
    private String icon;

    @ManyToMany(mappedBy = "subcategoryOptions")
    List<Product> products;
}
