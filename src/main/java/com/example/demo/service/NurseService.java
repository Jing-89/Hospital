package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Nurse;
import com.example.demo.repository.NurseRepository;

@Service
public class NurseService {

	@Autowired
	private NurseRepository nurseRepository;

	public List<Nurse> getAllNurses() {
		return nurseRepository.findAll();
	}

	public Nurse getNurseById(Long id) {
		return nurseRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到此id:" + id));
	}

	public Nurse createNurse(Nurse nurse) {
		return nurseRepository.save(nurse);
	}

	public Nurse updateNurse(Long id, Nurse nurseDetails) {
		Nurse nurse = nurseRepository.findById(id).orElseThrow(() -> new RuntimeException("找不到此id:" + id));
		nurse.setName(nurseDetails.getName());
		nurse.setNumber(nurseDetails.getNumber());
		return nurseRepository.save(nurse);
	}

	public void deleteNurse(Long id) {
		if (!nurseRepository.existsById(id)) {
			throw new RuntimeException("找不到此id: " + id);
		}
		nurseRepository.deleteById(id);
	}

}
