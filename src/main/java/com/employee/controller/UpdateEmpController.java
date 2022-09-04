package com.employee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class UpdateEmpController {

	
	@Autowired
	private EmployeeService employeeService;
	
	
	
	@RequestMapping("emp/updateorgetall")
	public String updateOrGetAll(String status,HttpServletRequest req) {
		if ("getall".equals(status)) {
			List<EmployeeVO> all = employeeService.getAll(); // 第三步驟
			Gson create = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();// 轉gson格式
			String json = create.toJson(all);
			return json;
		}
		
		if("update".equals(status)) {
			String name = req.getParameter("name");           //用update方法修改各欄位的值
			String storeno = req.getParameter("storeno");
			String phone = req.getParameter("phone");
			String adrs = req.getParameter("adrs");
			String email = req.getParameter("email");
			String title = req.getParameter("title");
			String parameter = req.getParameter("statue");
			String id = req.getParameter("empno");
			try {
				EmployeeVO employeeVO = new EmployeeVO();   //呼叫EmployeeVO
				 employeeVO.setEmp_name(name);               //用EmployeeVO把值傳回到資料庫
				 employeeVO.setSt_no(storeno);
				 employeeVO.setEmp_tel(phone);
				 employeeVO.setEmp_adrs(adrs);
				 employeeVO.setEmp_mail(email);
				 employeeVO.setEmp_title(title);
				 employeeVO.setEmp_no(id);
				 employeeVO.setEmp_status(Integer.parseInt(parameter));
				
				 boolean update = employeeService.update(employeeVO);
				 if(update) {
					 return "true";
				 }else {
					 return "false";
				 }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "false";
			}
			 
		}
		return null;
	}

}
