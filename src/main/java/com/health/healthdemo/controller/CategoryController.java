package com.health.healthdemo.controller;


import com.health.healthdemo.entity.MCategory;
import com.health.healthdemo.repository.MCategoryRepository;
import com.health.healthdemo.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MCategoryRepository mCategoryRepository;

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(
            @RequestParam String domicile,
            @RequestParam String state,
            @RequestParam String institute) {

        List<MCategory> categories;

        // Only show NRI and Management for PIMC
        if ("PIMC".equalsIgnoreCase(institute)) {
            if ("nri".equalsIgnoreCase(domicile)) {
                categories = mCategoryRepository.findByCategoryname("NRI");
            } else if ("indian".equalsIgnoreCase(domicile) && "other".equalsIgnoreCase(state)) {
                categories = mCategoryRepository.findByCategoryname("Management");
            } else if ("indian".equalsIgnoreCase(domicile) && "meghalaya".equalsIgnoreCase(state)) {
                categories = mCategoryRepository.findAll();
            } else {
                categories = Collections.emptyList();
            }
        } else if ("SMC".equalsIgnoreCase(institute)) {
            // Exclude NRI and Management for SMC
            if ("indian".equalsIgnoreCase(domicile) && "meghalaya".equalsIgnoreCase(state)) {
                categories = mCategoryRepository.findAll()
                        .stream()
                        .filter(c -> !c.getCategoryname().equalsIgnoreCase("NRI") && !c.getCategoryname().equalsIgnoreCase("Management"))
                        .toList();
            } else {
                categories = Collections.emptyList();
            }
        } else {
            categories = Collections.emptyList();
        }

        return ResponseEntity.ok(categories);
    }


    @PostMapping("/create")
    public MCategory createCategory(@RequestBody MCategory category) {
        return categoryService.createCategory(category);
    }
}

