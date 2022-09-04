package com.employee.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeDAO dao;
	
	public EmployeeService() {
	}
	
	public boolean insert(EmployeeVO EmployeeVO) {
		return dao.insert(EmployeeVO);
	}
	
	public boolean update(EmployeeVO EmployeeVO) {
		return dao.update(EmployeeVO);
	}
	
	public List<EmployeeVO> getAll() {
		return dao.getAll();
	}
	
	public EmployeeVO emp(String mail) {
		return dao.emp(mail);
	}
	
	public EmployeeVO emplogin(String username, String password) {
		return dao.emplogin(username, password);
	}
	
	public boolean NewEmployee(String EMP_NO) {
		return dao.NewEmployee(EMP_NO);
	}
	
	public int empUpdata(EmployeeVO EmployeeVO) {
		return dao.empUpdata(EmployeeVO);
	}
}
