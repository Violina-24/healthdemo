package com.health.healthdemo.repository;

import com.health.healthdemo.entity.MCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MCategoryRepository extends JpaRepository <MCategory,Integer>{
    List<MCategory> findByCategoryname(String categoryname);
    List<MCategory> findByCid(Integer cid);


}
