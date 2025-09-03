package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.StationRequestDto;
import com.example.demo.dto.StationResponseDto;
import com.example.demo.entity.Nurse;
import com.example.demo.entity.NurseStation;
import com.example.demo.entity.Station;
import com.example.demo.repository.NurseRepository;
import com.example.demo.repository.NurseStationRepository;
import com.example.demo.repository.StationRepository;

import jakarta.transaction.Transactional;

@Service
public class StationService {

	@Autowired
	private StationRepository stationRepository;

	@Autowired
	private NurseRepository nurseRepository;

	@Autowired
	private NurseStationRepository nurseStationRepository;

	public List<StationResponseDto> getAllStations() {
		return stationRepository.findAll().stream().map(StationResponseDto::new).collect(Collectors.toList());
	}

	public StationResponseDto getStationById(Long id) {
		Station station = stationRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到此id:" + id));
		return new StationResponseDto(station);
	}

	public StationResponseDto createStation(StationRequestDto dto) {
		Station station = new Station();
		station.setName(dto.getName());

		if (dto.getNurseId() != null && !dto.getNurseId().isEmpty()) {
			List<Nurse> nurses = nurseRepository.findAllById(dto.getNurseId());
			LocalDateTime now = LocalDateTime.now();

			nurses.forEach(nurse -> {
				NurseStation nurseStation = new NurseStation();
				nurseStation.setNurse(nurse);
				nurseStation.setStation(station);
				nurseStation.setAssignedAt(now);

				nurse.getNurseStations().add(nurseStation);
				station.getNurseStations().add(nurseStation);
			});

			Station saved = stationRepository.save(station);
			nurseRepository.updateNursesUpdatedAt(dto.getNurseId(), now);

			return new StationResponseDto(saved);
		}

		Station saved = stationRepository.save(station);
		return new StationResponseDto(saved);
	}

	@Transactional
	public StationResponseDto updateStation(Long id, StationRequestDto dto) {
		Station station = stationRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到此id:" + id));
		station.setName(dto.getName());

		Station updated = stationRepository.save(station);
		return new StationResponseDto(updated);
	}

	public void deleteStation(Long id) {
		if (!stationRepository.existsById(id)) {
			throw new RuntimeException("找不到此id: " + id);
		}
		stationRepository.deleteById(id);
	}

	@Transactional
	public Station assignNursesToStation(Long stationId, List<Long> nurseIds) {
		Station station = stationRepository.findById(stationId)
				.orElseThrow(() -> new RuntimeException("找不到站點 id: " + stationId));

		List<Nurse> nurses = nurseRepository.findAllById(nurseIds);
		LocalDateTime now = LocalDateTime.now();

		for (Nurse nurse : nurses) {
			if (!nurseStationRepository.existsByNurseIdAndStationId(nurse.getId(), stationId)) {
				NurseStation ns = new NurseStation();
				ns.setNurse(nurse);
				ns.setStation(station);
				ns.setAssignedAt(now);
				nurseStationRepository.save(ns);
			}
		}

		nurseRepository.updateNursesUpdatedAt(nurseIds, now);

		return stationRepository.save(station);
	}

	@Transactional
	public Station unassignNursesFromStation(Long stationId, List<Long> nurseIds) {
		List<NurseStation> nurseStationsToRemove = nurseStationRepository.findByStationIdAndNurseIdIn(stationId,
				nurseIds);

		LocalDateTime now = LocalDateTime.now();

		nurseStationRepository.deleteAll(nurseStationsToRemove);

		nurseRepository.updateNursesUpdatedAt(nurseIds, now);

		return stationRepository.findById(stationId).orElseThrow(() -> new RuntimeException("找不到站點 id: " + stationId));
	}

}