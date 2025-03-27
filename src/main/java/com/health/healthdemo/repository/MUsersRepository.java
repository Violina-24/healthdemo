package com.health.healthdemo.repository;

import com.health.healthdemo.entity.MUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MUsersRepository extends JpaRepository<MUsers,Long> {
    Optional<MUsers> findByEmailAndPassword(String email, String password);

    Optional<MUsers> findByEmail(String email);
}
