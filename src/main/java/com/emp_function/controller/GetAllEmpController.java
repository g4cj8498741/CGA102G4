package com.emp_function.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emp_function.model.Emp_functionService;
import com.emp_function.model.Emp_functionVO;
import com.google.gson.Gson;

@RestController
public class GetAllEmpController {

	@Autowired
	private Emp_functionService emp_functionService;
	
	@Autowired
	private Gson gson;
	
	@RequestMapping("GetAllEmp")
	public List<Emp_functionVO> getAllEmp(@RequestParam("status") String statusStr) {
		//String statusStr = req.getParameter("status");
		//Emp_functionService emp_functionService = new Emp_functionService();
		if("getall".equals(statusStr)) {
			List<Emp_functionVO> all = emp_functionService.getAll();
			
			return all;
		}
		 
		return null;
	}
}
