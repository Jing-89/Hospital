package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.entity.Nurse;
import com.example.demo.entity.Station;

public class NurseResponseDto {

	private Long id;
	private String number;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<StationSimpleDto> stations;

	public NurseResponseDto(Nurse nurse) {
		this.id = nurse.getId();
		this.number = nurse.getNumber();
		this.name = nurse.getName();
		this.createdAt = nurse.getCreatedAt();
		this.updatedAt = nurse.getUpdatedAt();
		this.stations = nurse.getNurseStations().stream().map(ns -> new StationSimpleDto(ns.getStation()))
				.collect(Collectors.toList());

	}

	public static class StationSimpleDto {
		private Long id;
		private String name;

		public StationSimpleDto(Station station) {
			this.id = station.getId();
			this.name = station.getName();
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

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<StationSimpleDto> getStations() {
		return stations;
	}

	public void setStations(List<StationSimpleDto> stations) {
		this.stations = stations;
	}

}
