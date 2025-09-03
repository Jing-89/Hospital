package com.example.demo.dto;

import java.util.List;

public class NurseRequestDto {

	private String number;
	private String name;
	private List<Long> stationIds;

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

	public List<Long> getStationIds() {
		return stationIds;
	}

	public void setStationIds(List<Long> stationIds) {
		this.stationIds = stationIds;
	}

}
