package com.rcarorder.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rcar.model.RcarService;
import com.rcarorder.model.RcarOrderService;

import utils.ReDay;

@RestController
public class ShowOtherCarController {
	
	@Autowired
	private RcarOrderService rcarOrderService;

	@Autowired
	private RcarService rcarService;
	
	@RequestMapping("rcarorder/showothercar")
	public String showOtherCar(@RequestParam("date") String getdate,@RequestParam("store") String getstore) {
		LocalDate now = LocalDate.parse(getdate);
		//HttpSession session = req.getSession();
		
		
		//@SuppressWarnings("unchecked")// 取得區間內 有調度 "無訂單"車輛
		//List<Car_Dispatch_RecordVO> otherCarDispatch = (List<Car_Dispatch_RecordVO>)session.getAttribute("otherCarDispatch");
		//List<ReDay> nullOrderCar = rcarService.getNullOrderCar(otherCarDispatch);
		
		
		
//		LocalDate now = LocalDate.parse("2022-06-29");
		List<ReDay> list = rcarOrderService.getOthercar(getstore, now);
		List<ReDay> otherCar = rcarService.getOtherStoreCar(getstore);
		otherCar.addAll(list); //將無訂單車輛 與 訂單車輛裝載一起
		//otherCar.addAll(nullOrderCar);
		
		/// 轉json
		Gson gson = new Gson();
		String json = gson.toJson(otherCar);
		return json;
	}
}
