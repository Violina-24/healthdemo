package com.health.healthdemo.repository;

import com.health.healthdemo.entity.MDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MDistrictRepository extends JpaRepository<MDistrict,Long> {
    Optional<MDistrict> findByDistrictname(String districtname);

}
