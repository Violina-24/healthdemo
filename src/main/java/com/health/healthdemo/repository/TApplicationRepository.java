 package com.health.healthdemo.repository;

 import com.health.healthdemo.entity.MUsers;
 import com.health.healthdemo.entity.TApplication;

 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;

 import java.util.Optional;


 @Repository
 public interface TApplicationRepository extends JpaRepository<TApplication, Long> {
  Optional<TApplication> findByUser(MUsers user);
 }
