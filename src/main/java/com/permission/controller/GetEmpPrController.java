package com.permission.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.permission.model.PermissionService;
import com.permission.model.PermissionVO;

@RestController
public class GetEmpPrController {

	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private Gson gson;
	
	@RequestMapping("getemppr")
	public String getEmpPr(HttpServletRequest req) {
		
		String statusStr = req.getParameter("status");
		if("getemppr".equals(statusStr)) { //取得會員權限
			String empNo = req.getParameter("emp_no");
			List<PermissionVO> list = permissionService.getByEmpNo(empNo);
			
			String json = gson.toJson(list);
			System.out.println(list);
			return json;
		}
		
		if("addorrom".equals(statusStr)) { //新增與刪除 權限
			String rom = req.getParameter("json");
			String add = req.getParameter("addjson");
			
			System.out.println(rom);
			System.out.println(add);
			Type userListType = new TypeToken<ArrayList<PermissionVO>>(){}.getType();  //設定gson格式
			
			ArrayList<PermissionVO> romlist = gson.fromJson(rom, userListType);  
			ArrayList<PermissionVO> addlist = gson.fromJson(add, userListType);  
			
			System.out.println("add"+addlist);
			System.out.println("rom"+romlist);
			
			boolean romBoolean = false;
			boolean addBoolean = false;
			if(romlist.size() != 0) { //權限刪除
				int delete = permissionService.delete(romlist);      //回傳刪除幾筆資料
				if(delete == romlist.size()) {			//判斷是不是跟前端回傳的數量一樣
					System.out.println("rom"+true);
					romBoolean = true;
				}else {
					System.out.println("rom"+false);
				}
			}
			
			if(addlist.size() != 0) { //權限新增
				int insert = permissionService.insert(addlist);  //前端回傳0的話，回傳新增幾筆資料
				if(insert == addlist.size()) { 		//判斷新增跟傳到前端的數量是否一致
					System.out.println("add"+true);
					addBoolean = true;				//正確回傳true
				}else {
					System.out.println("add"+false);
				}
			}
			
			if(addlist.size() != 0 && romlist.size() != 0) { //同時新增 刪除
				if(romBoolean && addBoolean) {
					return "true";
				}else {
					return "false";
				}
			}else if(addlist.size() != 0) { //只有新增
				if(addBoolean) {
					return "true";
				}else {
					return "false";
				}
			}else if(romlist.size() != 0) { //只有刪除
				if(romBoolean) {
					return "true";
				}else {
					return "false";
				}
			}
		}
		return null;
	}
}
