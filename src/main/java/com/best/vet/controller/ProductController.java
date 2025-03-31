package com.best.vet.controller;

import com.best.vet.projection.ProductProjection;
import com.best.vet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping(value = "getBySubcategoryOptionValue/{value}")
    public ResponseEntity<List<ProductProjection>> getBySubcategoryOptionId(@PathVariable("value") Long value) {
        List<ProductProjection> menuTree = productService.getBySubcategoryOptionId(value);
        return ResponseEntity.ok(menuTree);
    }

    @GetMapping("/getBySubcategoryOptionValuePage/{value}")
    public ResponseEntity<Page<ProductProjection>> getBySubcategoryOptionValuePage(
            @PathVariable("value") Long value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductProjection> products = productService.findBySubcategoryOptions_Id(value, pageable);
        return ResponseEntity.ok(products);
    }

}
