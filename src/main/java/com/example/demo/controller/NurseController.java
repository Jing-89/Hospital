package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.NurseRequestDto;
import com.example.demo.dto.NurseResponseDto;
import com.example.demo.entity.Nurse;
import com.example.demo.service.NurseService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/nurses")
public class NurseController {

	@Autowired
	private NurseService nurseService;

	// 查詢所有護理師
	@GetMapping
	@Operation(summary = "查詢所有護理師")
	public ResponseEntity<List<NurseResponseDto>> getAllNurses() {

		List<NurseResponseDto> nurses = nurseService.getAllNurses().stream().map(NurseResponseDto::new)
				.collect(Collectors.toList());
		return ResponseEntity.ok(nurses);
	}

	// 以id查詢護理師
	@GetMapping("/{id}")
	@Operation(summary = "以id查詢護理師")
	public ResponseEntity<NurseResponseDto> getNurseById(@PathVariable Long id) {
		Nurse nurse = nurseService.getNurseById(id);
		return ResponseEntity.ok(new NurseResponseDto(nurse));
	}

	// 新增護理師
	@PostMapping
	@Operation(summary = "新增護理師")
	public ResponseEntity<NurseResponseDto> createNurse(@RequestBody NurseRequestDto dto) {
		Nurse nurse = new Nurse();
		nurse.setName(dto.getName());
		nurse.setNumber(dto.getNumber());

		Nurse created = nurseService.createNurse(nurse);
		return ResponseEntity.ok(new NurseResponseDto(created));
	}

	// 更新護理師資料
	@PutMapping("/{id}")
	@Operation(summary = "更新護理師資料")
	public ResponseEntity<NurseResponseDto> updateNurse(@PathVariable Long id, @RequestBody NurseRequestDto dto) {
		Nurse nurseDetails = new Nurse();
		nurseDetails.setName(dto.getName());
		nurseDetails.setNumber(dto.getNumber());
		
		Nurse updated = nurseService.updateNurse(id, nurseDetails);
        return ResponseEntity.ok(new NurseResponseDto(updated));
	}

	// 刪除護理師資料
	@DeleteMapping("/{id}")
	@Operation(summary = "刪除護理師資料")
	public ResponseEntity<Map<String, String>> deleteNurse(@PathVariable Long id) {
		nurseService.deleteNurse(id);
		return ResponseEntity.ok(Map.of("message", "用戶已刪除"));
	}

}
