package com.example.demo.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.entity.Nurse;

public class NurseResponseDto {
	
	private Long id;
	private String number;
	private String name;
	private List<String> stations;

	public NurseResponseDto(Nurse nurse) {
		this.id = nurse.getId();
		this.number = nurse.getNumber();
		this.name = nurse.getName();
		this.stations = nurse.getStations().stream().map(station -> station.getName()).collect(Collectors.toList());
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

	public List<String> getStations() {
		return stations;
	}

	public void setStations(List<String> stations) {
		this.stations = stations;
	}

}
