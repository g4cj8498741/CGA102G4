package com.employee.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.store.model.StoreService;
import com.store.model.StoreVO;

@RequestMapping("backend/emp")
@Controller
public class SpringEmpController {

	@Autowired
	private StoreService storeService;
	
	private List<StoreVO> all;
	
	public SpringEmpController() {
		//bean初始化首先調用 建構子 但為注入依賴 所以不可寫在這
		//all = storeService.getAll();
	}
	
	@GetMapping("updateEmp")
	public String upemp(Model model) {
		
		model.addAttribute("dao",all);
		return "back_end/ReviseEmp/empUpdata";
	}
	
	@GetMapping("newEmp")
	public String newEmp(Model model) {
		//List<StoreVO> all = storeService.getAll();
		model.addAttribute("dao",all);
		return "back_end/employee/NewEmployee";
	}
	
	@GetMapping("allUpdataEmp")
	public String allupEmp(Model model) {
		//List<StoreVO> all = storeService.getAll();
		model.addAttribute("dao",all);
		return "back_end/employee/UpdateEmployee";
	}
	
	@PostConstruct
	public void initMethod() {
		System.out.println("SpringEmpController 初始化");
		all = storeService.getAll();
	}
}
