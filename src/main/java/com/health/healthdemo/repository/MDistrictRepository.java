package com.health.healthdemo.repository;

import com.health.healthdemo.entity.MDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MDistrictRepository extends JpaRepository<MDistrict,Long> {
}
