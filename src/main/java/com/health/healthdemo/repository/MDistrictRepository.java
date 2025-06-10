package com.health.healthdemo.repository;

import com.health.healthdemo.entity.MDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MDistrictRepository extends JpaRepository<MDistrict,Long> {
    Optional<MDistrict> findByDistrictname(String districtname);
    // Add this custom save method if needed
    @Transactional
    @Modifying
    @Query("UPDATE MPostalAddress a SET a.district = :district WHERE a.id = :id")
    void updateDistrict(@Param("id") Long id, @Param("district") MDistrict district);

}
