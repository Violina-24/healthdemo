package com.health.healthdemo.services;


import com.health.healthdemo.entity.MCategory;
import com.health.healthdemo.repository.MCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private MCategoryRepository mCategoryRepository;
    public List<MCategory> getAllCategories() {
        return mCategoryRepository.findAll();
    }
    public Optional<MCategory> getCategoryById(int Cid) {
        return mCategoryRepository.findById(Cid);
}
public MCategory saveCategory(MCategory category) {
        return mCategoryRepository.save(category);
}

public MCategory createCategory(MCategory category) {
        return mCategoryRepository.save(category);
}
public void deleteCategory(int Cid) {
       mCategoryRepository.deleteById(Cid);
}


}
