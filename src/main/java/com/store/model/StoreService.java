package com.store.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {
	
	@Autowired
	private StoreDAO dao;
	
//	public StoreService() {
//		dao = new StoreDAOImpl();
//	}
	
	public List<StoreVO> getAll(){
		return dao.getAll();
	}
	
	public boolean update(StoreVO StoreVO) {
		try {
			dao.update(StoreVO);
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean insert(StoreVO StoreVO) {
			return dao.insert(StoreVO);
		
	}
}
