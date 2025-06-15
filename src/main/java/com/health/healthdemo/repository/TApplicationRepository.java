 package com.health.healthdemo.repository;

 import com.health.healthdemo.entity.MUsers;
 import com.health.healthdemo.entity.TApplication;

 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.data.repository.query.Param;
 import org.springframework.stereotype.Repository;

 import java.util.List;
 import java.util.Optional;


 @Repository
 public interface TApplicationRepository extends JpaRepository<TApplication, Long> {

  // 1. Find by user
  Optional<TApplication> findByUser(MUsers user);

  // 2. Find applications by course ID - using explicit JPQL
  @Query("SELECT a FROM TApplication a WHERE a.mCourse.Courseid = :courseId")
  List<TApplication> findApplicationsByCourseId(@Param("courseId") Long courseId);

  // 3. Count applications by course ID - using explicit JPQL
  @Query("SELECT COUNT(a) FROM TApplication a WHERE a.mCourse.Courseid = :courseId")
  Long countApplicationsByCourseId(@Param("courseId") Long courseId);

  // 4. Count applications by course ID and status - using explicit JPQL
  @Query("SELECT COUNT(a) FROM TApplication a WHERE a.mCourse.Courseid = :courseId AND a.status = :status")
  Long countApplicationsByCourseIdAndStatus(@Param("courseId") Long courseId, @Param("status") String status);

  // 5. Count by status - can remain as derived query
  Long countByStatus(String status);
 }