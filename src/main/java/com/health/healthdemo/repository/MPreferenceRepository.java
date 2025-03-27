package com.health.healthdemo.repository;

import com.health.healthdemo.entity.MCourse;
import com.health.healthdemo.entity.MPreference;
import com.health.healthdemo.entity.MUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import java.util.List;
// import java.util.Optional;

@Repository
public interface MPreferenceRepository extends JpaRepository<MPreference, Long> {
    boolean existsByMUsersAndMCourse(MUsers mUsers, MCourse mCourse);
    }

