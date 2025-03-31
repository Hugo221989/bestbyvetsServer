package com.best.vet.service;

import com.best.vet.projection.ProductProjection;
import com.best.vet.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<ProductProjection> getBySubcategoryOptionId (Long id){
        return productRepository.findBySubcategoryOptions_Id(id);
    }

    public List<ProductProjection> getBySubcategoryOptionOptionValue (String value){
        return productRepository.findBySubcategoryOptions_OptionValueIgnoreCase(value);
    }

    public Page<ProductProjection> findBySubcategoryOptions_Id(Long subcategoryOptionId, Pageable pageable){
        return productRepository.findBySubcategoryOptions_Id(subcategoryOptionId, pageable);
    }

}
