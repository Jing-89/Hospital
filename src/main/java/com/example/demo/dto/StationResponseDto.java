package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.entity.Station;

public class StationResponseDto {

	private Long id;
	private String name;
	private LocalDateTime updatedAt;
	private List<NurseDto> nurses;

	public static class NurseDto {
		private Long id;
		private String name;
		private String number;
		private LocalDateTime assignedAt;

		public NurseDto(Long id, String number, String name, LocalDateTime assignedAt) {
			this.id = id;
			this.number = number;
			this.name = name;
			this.assignedAt = assignedAt;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public LocalDateTime getAssignedAt() {
			return assignedAt;
		}

		public void setAssignedAt(LocalDateTime assignedAt) {
			this.assignedAt = assignedAt;
		}

	}

	public StationResponseDto(Station station) {
		this.id = station.getId();
		this.name = station.getName();
		this.updatedAt = station.getUpdatedAt();
		this.nurses = station.getNurseStations().stream()
				.map(nurseStation -> new NurseDto(nurseStation.getNurse().getId(), nurseStation.getNurse().getNumber(),
						nurseStation.getNurse().getName(), nurseStation.getAssignedAt()
				)).collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<NurseDto> getNurses() {
		return nurses;
	}

	public void setNurses(List<NurseDto> nurses) {
		this.nurses = nurses;
	}

}
