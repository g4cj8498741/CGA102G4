package com.rcar.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.employee.model.EmployeeVO;
import com.rcar.model.RcarService;
import com.rcar.model.RcarVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;

@Controller
@RequestMapping("rcar")
public class GetStoreAllCarController  {
	
	@Autowired
	private RcarService rcarService;
	
	@Autowired
	private StoreService storeService;
	
	
	@RequestMapping("/getstoreallcar")
	public String getStoreAllCar(HttpSession session,Model model) {
		
		EmployeeVO emp = (EmployeeVO)session.getAttribute("employee");
		List<RcarVO> carAll = rcarService.getSt_noAll(emp.getSt_no());
		List<StoreVO> storeAll = storeService.getAll();
		model.addAttribute("car",carAll);
		model.addAttribute("dao",storeAll);
		return "back_end/storecarcontrol/storecarcontrol";
	}
}
