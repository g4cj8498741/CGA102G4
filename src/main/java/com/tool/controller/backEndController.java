package com.tool.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.store.model.StoreService;
import com.store.model.StoreVO;

@RequestMapping("backend")
@Controller
public class backEndController {

	@Autowired
	private StoreService storeService;
	
	private List<StoreVO> all;
	
	@RequestMapping("cardispatchrecord")
	public String disoatch(Model model) {
		model.addAttribute("dao", all);
		return "back_end/cardispatchrecord/cardispatchrecord";
	}
	
	@RequestMapping("storeAll")
	public String stroeAll(Model model) {
		model.addAttribute("dao", all);
		return "back_end/Store/storeAll";
	}
	
	@RequestMapping("back_cardistribution")
	public String backCardistribution(Model model) {
		model.addAttribute("dao", all);
		return "back_end/car_distribution/back_cardistribution";
	}
	
	@RequestMapping("othercarorder")
	public String xx(Model model) {
		model.addAttribute("dao", all);
		return "back_end/otherCarOrder/othercarorder";
	}
	
	@PostConstruct
	public void init() {
		all = storeService.getAll();
	}
}
