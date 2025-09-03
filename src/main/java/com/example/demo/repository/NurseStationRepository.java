package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.NurseStation;

import jakarta.transaction.Transactional;

public interface NurseStationRepository extends JpaRepository<NurseStation, Long> {

	List<NurseStation> findByStationIdAndNurseIdIn(Long stationId, List<Long> nurseIds);

	boolean existsByNurseIdAndStationId(Long nurseId, Long stationId);

	@Transactional
    void deleteByNurseId(Long nurseId);
}
