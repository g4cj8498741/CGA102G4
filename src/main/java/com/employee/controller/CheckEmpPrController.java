package com.employee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.model.EmployeeVO;
import com.google.gson.Gson;
import com.permission.model.PermissionService;
import com.permission.model.PermissionVO;

@RestController
public class CheckEmpPrController {
	
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("emp/checkemppr")
	public List<PermissionVO> CheckEmpPr(HttpServletRequest req) {
		HttpSession session = req.getSession();
		EmployeeVO emp = (EmployeeVO)session.getAttribute("employee");
		
		List<PermissionVO> empPr = permissionService.getByEmpNo(emp.getEmp_no());
		
		return empPr;
	}
}
