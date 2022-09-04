package com.employee.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;
import com.google.gson.Gson;

@RestController
public class EmpLoginController {

	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private Gson gson;
	@RequestMapping("emp/login")
	public String login(String username,String password,HttpServletRequest req) {

		HttpSession session = req.getSession();

		// 判斷是否登入過
		if (session.getAttribute("employee") == null) {
			System.out.println(username);
			System.out.println(password);
			EmployeeVO emplogin = employeeService.emplogin(username, password);
			if (emplogin == null) {
				// 登入失敗
				System.out.println("登入失敗");
				return "false";
			} else {
				// 登入成功
				session.setAttribute("employee", emplogin);
				System.out.println("登入成功");

				String json = gson.toJson(emplogin);
				session.setAttribute("employeejson", json);
				try {
					String Location = (String) session.getAttribute("location");
					if (Location != null) {
						session.removeAttribute("location"); // 刪除這筆session資料
						return Location;
					} else {
						// 要是重不是保護的網頁登入，則導入首頁
						return req.getContextPath() + "/back_end/ReviseEmp/empUpdata.jsp";
					}
				} catch (Exception e) {

				}
			}
		} else { //登出
			session.removeAttribute("employee");
			session.removeAttribute("employeejson");
			return "redirect:"+req.getContextPath()+"/back_end/EmployeeLogin/EmployeeLogin.html";
		}
		
		return null;
	}
}
