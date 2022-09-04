package com.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.store.model.StoreService;
import com.store.model.StoreVO;

@RestController
@RequestMapping("store")
public class GetStoreController {

	@Autowired
	private StoreService storeService;

	@Autowired
	private Gson gson;

	@RequestMapping("/getstoreall")
	public String getStroeAll() {
		List<StoreVO> all = storeService.getAll(); // 第三部 取得Getall方法

		String json = gson.toJson(all);
		return json;
	}

	@RequestMapping("/storeaccess")
	public String storeAccess(String status,String json) {

		if ("updata".equals(status)) {
			StoreVO vo = new StoreVO();
			vo = gson.fromJson(json, vo.getClass());

			boolean update = storeService.update(vo);
			System.out.println(update);
			if (update) {
				return "true";
			} else {
				return "false";
			}
		}

		if ("insert".equals(status)) {
			StoreVO vo = new StoreVO();
			vo = gson.fromJson(json, vo.getClass());

			try {
				boolean update = storeService.insert(vo);
				System.out.println(update);
				if (update) {
					return "true";
				} else {
					return "false";
				}
			} catch (Exception e) {
				return "false";
			}

		}
		return null;
	}
}
