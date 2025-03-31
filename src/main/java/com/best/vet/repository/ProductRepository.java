package com.best.vet.repository;

import com.best.vet.entity.Product;
import com.best.vet.projection.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<ProductProjection> findBySubcategoryOptions_Id(Long subcategoryOptionId);
    List<ProductProjection> findBySubcategoryOptions_OptionValueIgnoreCase(String subcategoryOptionValue);
    Page<ProductProjection> findBySubcategoryOptions_Id(Long subcategoryOptionId, Pageable pageable);

}
