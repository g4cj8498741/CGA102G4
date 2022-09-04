package com.rcarorder.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rcar.model.RcarService;
import com.rcar.model.RcarVO;
import com.rcarorder.model.RcarOrderService;
import com.store.model.StoreService;
import com.store.model.StoreVO;

import utils.ReDay;

@RestController
public class StoreShowController {


	@Autowired
	private RcarService rcarService;
	
	@Autowired
	private RcarOrderService rcarOrderService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private Gson gson;
	@SuppressWarnings("unchecked")
	@RequestMapping("storeshow")
	public String storeshow(
			@RequestParam(value = "click",required = false) String getclick,
			@RequestParam(value = "month",required = false) String getmonth,
			@RequestParam(value = "store",required = false) String getstore,
			@RequestParam(value = "store",required = false) String city,
			@RequestParam(value = "changemonth",required = false) String month,
			HttpServletRequest req) {
		
		HttpSession session = req.getSession();
		//RcarService rcarService = new RcarService();
		//RcarOrderService orderService = new RcarOrderService();

		//////////////
		if (getmonth != null) { // 初始會經過
			LocalDate localDate = LocalDate.parse(getmonth);
			;
			List<RcarVO> list2 = rcarService.getSt_noAll(getstore);
			List<ReDay> getallday = rcarOrderService.getallday(list2, localDate);
			/// 轉json
			String json = gson.toJson(getallday);
			return json;
		}

		if (getclick == null) {
			List<StoreVO> all;
			// 重新整理會經過 取得所有門市資訊
			if (session.getAttribute("store") == null) {
				all = storeService.getAll();
				session.setAttribute("store", all);
			} else {
				all = (List<StoreVO>) session.getAttribute("store");
			}
			String json = gson.toJson(all);
			return json;
			
			// 先將所有門市車輛訊息存入session 以便切換調用 可以不用一直呼叫連線池 //目前有BUG 車輛更動 無法及時更新
//			if (session.getAttribute("TPE") == null) {
//				for (StoreVO store : all) {
//					//RcarDAOImpl rcarDAOImpl = new RcarDAOImpl();
//					List<RcarVO> list = rcarService.getSt_noAll(store.getSt_no());
//					session.setAttribute(store.getSt_no(), list);
//				}
//			}

		} else {
			// 切換門市,秀出該門市所有車輛
			//String city = req.getParameter("store");
			//String month = req.getParameter("changemonth");
			LocalDate date = LocalDate.parse(month);
			System.out.println(city);
			// 取出相對應的session 可以不用調用連線池 //目前有BUG 車輛更動 無法及時更新
//			@SuppressWarnings("unchecked")
//			List<RcarVO> list = (List<RcarVO>) session.getAttribute(city);

			List<RcarVO> list = rcarService.getSt_noAll(city);
			if(list.size()!=0) {				
				List<ReDay> getallday = rcarOrderService.getallday(list, date);
				
				/// 轉json
				String json = gson.toJson(getallday);
				return json;
			}
			
		}
		
		return null;
	}
	
}
