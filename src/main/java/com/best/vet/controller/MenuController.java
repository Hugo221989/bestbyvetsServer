package com.best.vet.controller;

import com.best.vet.dto.CategoryDTO;
import com.best.vet.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getMenuTree() {
        List<CategoryDTO> menuTree = menuService.getMenuTree();
        return ResponseEntity.ok(menuTree);
    }
}
