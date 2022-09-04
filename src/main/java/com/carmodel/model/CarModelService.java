package com.carmodel.model;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarModelService {
	
	@Autowired
	private CarModelDAO dao;
	
	public boolean insert(CarModelVO carModelVO) {
		return dao.insert(carModelVO);
	}
	
	public boolean update(CarModelVO carModelVO) {
		return dao.update(carModelVO);
	}
	
	//查詢所有
	public List<CarModelVO> getAll() {
		return dao.getAll();
	}
	
	//顯示圖片
	public byte[] getImges(String model_no) {
		return dao.getImage(model_no);
	}

}
