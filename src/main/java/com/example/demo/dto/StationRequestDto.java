package com.example.demo.dto;

import java.util.List;

public class StationRequestDto {

	private String name;
	private List<Long> nurseId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Long> getNurseId() {
		return nurseId;
	}
	public void setNurseId(List<Long> nurseId) {
		this.nurseId = nurseId;
	}
	
	
}
