package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.NurseRequestDto;
import com.example.demo.dto.NurseResponseDto;
import com.example.demo.entity.Nurse;
import com.example.demo.entity.NurseStation;
import com.example.demo.entity.Station;
import com.example.demo.repository.NurseRepository;
import com.example.demo.repository.NurseStationRepository;
import com.example.demo.repository.StationRepository;

import jakarta.transaction.Transactional;

@Service
public class NurseService {

	@Autowired
	private NurseRepository nurseRepository;
	
	@Autowired
	private StationRepository stationRepository;
	
	@Autowired
	private NurseStationRepository nurseStationRepository;

	public List<NurseResponseDto> getAllNurses() {
		return nurseRepository.findAll().stream().map(NurseResponseDto::new).collect(Collectors.toList());
	}

	public NurseResponseDto getNurseById(Long id) {
		Nurse nurse = nurseRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到此id:" + id));
		return new NurseResponseDto(nurse);
	}

	public NurseResponseDto createNurse(NurseRequestDto dto) {
		Nurse nurse = new Nurse();
		nurse.setName(dto.getName());
		nurse.setNumber(dto.getNumber());
		Nurse saved = nurseRepository.save(nurse);
		return new NurseResponseDto(saved);
	}

	@Transactional
	public NurseResponseDto updateNurse(Long nurseId, NurseRequestDto dto) {
		Nurse nurse = nurseRepository.findById(nurseId).orElseThrow(() -> new RuntimeException("找不到護士: " + nurseId));

		nurse.setNumber(dto.getNumber());
		nurse.setName(dto.getName());

		nurseStationRepository.deleteByNurseId(nurseId);

		if (dto.getStationIds() != null && !dto.getStationIds().isEmpty()) {
			List<Station> stations = stationRepository.findAllById(dto.getStationIds());
			stations.forEach(station -> {
				NurseStation ns = new NurseStation();
				ns.setNurse(nurse);
				ns.setStation(station);
				ns.setAssignedAt(LocalDateTime.now());
				nurse.getNurseStations().add(ns);
				station.getNurseStations().add(ns);
				nurseStationRepository.save(ns);
			});
		}

		nurseRepository.save(nurse);
		return new NurseResponseDto(nurse);
	}

	public void deleteNurse(Long id) {
		if (!nurseRepository.existsById(id)) {
			throw new RuntimeException("找不到此id: " + id);
		}
		nurseRepository.deleteById(id);
	}

}
