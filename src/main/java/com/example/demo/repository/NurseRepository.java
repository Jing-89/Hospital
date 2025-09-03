package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;     
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Nurse;

public interface NurseRepository extends JpaRepository<Nurse, Long> {
	
	@Modifying
	@Query("UPDATE Nurse n SET n.updatedAt = :updateTime WHERE n.id IN :nurseIds")
	void updateNursesUpdatedAt(@Param("nurseIds") List<Long> nurseIds, @Param("updateTime") LocalDateTime updateTime);
	
}
