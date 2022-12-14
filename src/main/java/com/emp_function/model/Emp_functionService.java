package com.emp_function.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Emp_functionService {
	
	@Autowired
	private Emp_functionDAO dao;
	
	public Emp_functionService() {
		dao = new Emp_functionDAOImpl();
	}
	
	public void insert(Emp_functionVO emp_functionVO) {
		 dao.insert(emp_functionVO);
	}
	
	public void update(Emp_functionVO emp_functionVO) {
		dao.update(emp_functionVO);
	}
	
	public List<Emp_functionVO> getAll() {
		return dao.getAll();
	}
}
