package com.best.vet.controller;

import com.best.vet.projection.ProductProjection;
import com.best.vet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    private final WebClient webClient;

    public ProductController(WebClient.Builder webClientBuilder) {
        // You can use a standalone builder without enabling the full reactive stack.
        this.webClient = webClientBuilder.baseUrl("https://world.openfoodfacts.org").build();
    }

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


    @GetMapping("/{barcode}")
    public Mono<ResponseEntity<String>> getProductByBarcode(@PathVariable String barcode) {
        return webClient.get()
                .uri("/api/v0/product/{barcode}.json", barcode)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorResume(ex ->
                        Mono.just(ResponseEntity.badRequest().body("Error retrieving product data"))
                );
    }

}
