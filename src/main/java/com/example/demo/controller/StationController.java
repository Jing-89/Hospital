package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AssignRequestDto;
import com.example.demo.dto.StationRequestDto;
import com.example.demo.dto.StationResponseDto;
import com.example.demo.entity.Station;
import com.example.demo.service.StationService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("api/stations")
public class StationController {

	@Autowired
	private StationService stationService;

	// 查詢所有站點
	@GetMapping
	@Operation(summary = "查詢所有站點")
	public ResponseEntity<List<StationResponseDto>> getAllStations() {
		return ResponseEntity.ok(stationService.getAllStations());
	}

	// 查詢單一站點
	@GetMapping("/{id}")
	@Operation(summary = "查詢單一站點")
	public ResponseEntity<StationResponseDto> getStationById(@PathVariable Long id) {
		return ResponseEntity.ok(stationService.getStationById(id));
	}

	// 新增站點
	@PostMapping
	@Operation(summary = "新增站點")
	public ResponseEntity<StationResponseDto> createStation(@RequestBody StationRequestDto dto) {
		return ResponseEntity.ok(stationService.createStation(dto));
	}

	// 更新站點
	@PutMapping("/{id}")
	@Operation(summary = "更新站點")
	public ResponseEntity<StationResponseDto> updateStation(@PathVariable Long id, @RequestBody StationRequestDto dto) {
		return ResponseEntity.ok(stationService.updateStation(id, dto));
	}

	// 刪除站點
	@DeleteMapping("/{id}")
	@Operation(summary = "刪除站點")
	public ResponseEntity<Map<String, String>> deleteStation(@PathVariable Long id) {
		stationService.deleteStation(id);
		return ResponseEntity.ok(Map.of("message", "站點已刪除"));
	}

	// 指派護士到指定站點
	@PutMapping("/{id}/assign")
	@Operation(summary = "指派護士到指定站點")
	public ResponseEntity<StationResponseDto> assignNurses(@PathVariable Long id, @RequestBody AssignRequestDto dto) {
		Station assigned = stationService.assignNursesToStation(id, dto.getNurseIds());
		return ResponseEntity.ok(new StationResponseDto(assigned));

	}

	// 移除站點上的護士
	@PutMapping("/{id}/unassign")
	@Operation(summary = "移除站點上的護士")
	public ResponseEntity<StationResponseDto> unassignNurses(@PathVariable Long id, @RequestBody AssignRequestDto dto) {
		Station unassigned = stationService.unassignNursesFromStation(id, dto.getNurseIds());
		return ResponseEntity.ok(new StationResponseDto(unassigned));
	}

}
