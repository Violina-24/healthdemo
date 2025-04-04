package com.health.healthdemo.controller;


import com.health.healthdemo.entity.MCategory;
import com.health.healthdemo.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // Allow frontend requests
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public List<MCategory> getAllCategories() { // Fix method name
        return categoryService.getAllCategories();
    }

    @PostMapping("/create")
    public MCategory createCategory(@RequestBody MCategory category) {
        return categoryService.createCategory(category);
    }
}

