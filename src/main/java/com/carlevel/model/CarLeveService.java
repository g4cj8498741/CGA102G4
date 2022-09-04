package com.carlevel.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarLeveService {
	
	@Autowired
	private CarLevelDAO dao;
	
	public boolean insert(CarLevelVO CarLevelVO) {
		return dao.insert(CarLevelVO);
	}
	
	public boolean update(CarLevelVO CarLevelVO) {
		return dao.update(CarLevelVO);
	}
	
	public List<CarLevelVO> getAll() {
		return dao.getAll();
	}
	
}
