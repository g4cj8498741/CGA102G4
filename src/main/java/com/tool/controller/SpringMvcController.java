package com.tool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.carmodel.model.CarModelService;
import com.carmodel.model.CarModelVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;

@Controller
public class SpringMvcController {
	
	@Autowired
	private CarModelService carModelService;
	
	@Autowired
	private StoreService storeService;
	@GetMapping("index")
	public String index(Model model) {
		//CarModelService carModelService = new CarModelService();
		List<CarModelVO> list = carModelService.getAll();
		List<StoreVO> all = storeService.getAll();
		String url = "front_end/index/index";
		model.addAttribute("carList",list);
		model.addAttribute("dao",all);
		return url;
	}
//	front_end/Login
	@GetMapping("Login")
	public String lgin() {
		System.out.println("進入");
		return "redirect:front_end/Login";
	}
}
