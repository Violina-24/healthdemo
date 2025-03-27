package com.health.healthdemo.repository;

import com.health.healthdemo.entity.MPostalAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MPostalAddressRepository extends JpaRepository<MPostalAddress, Long> {
}
