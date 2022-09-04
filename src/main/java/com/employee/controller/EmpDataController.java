package com.employee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class EmpDataController {

	@Autowired
	private EmployeeService employeeService;
	
	
	@RequestMapping("emp/empdata")
	public String EmpData(HttpServletRequest req,String status,String json,@RequestParam(value="pass") String passStr) {
		HttpSession session = req.getSession();
		//EmployeeService service = new EmployeeService();
		if("updata".equals(status)) { //會員自行更動資料
			try {
				EmployeeVO loginemp = (EmployeeVO)session.getAttribute("employee");
				EmployeeVO employeeVO = new EmployeeVO();
				Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
				employeeVO = gson.fromJson(json, employeeVO.getClass());
				employeeVO.setEmp_pass(loginemp.getEmp_pass());
				System.out.println(employeeVO);
				int update = employeeService.empUpdata(employeeVO);
				if(update == 1) {
					loginemp.setEmp_adrs(employeeVO.getEmp_adrs());
					loginemp.setEmp_name(employeeVO.getEmp_name());
					loginemp.setEmp_gender(employeeVO.getEmp_gender());
					loginemp.setEmp_bir(employeeVO.getEmp_bir());;
					loginemp.setEmp_tel(employeeVO.getEmp_tel());
					loginemp.setEmp_mail(employeeVO.getEmp_mail());
					session.setAttribute("employee",loginemp);
					return "true";
				}else {
					return "false";
				}
			} catch (Exception e) {
				return "false";
			}
			
		}
		
		if("updatapass".equals(status)) { //更新密碼
			EmployeeVO loginemp = (EmployeeVO)session.getAttribute("employee");
			loginemp.setEmp_pass(passStr);
			int update = employeeService.empUpdata(loginemp);
			if(update == 1) {
//				session.setAttribute("employee",loginemp);
				session.removeAttribute("employee");
				return "true";
			}else {
				return "false";
			}
		}
		
		if("checkpass".equals(status)) { //確認舊密碼
			EmployeeVO loginemp = (EmployeeVO)session.getAttribute("employee");
			
			if(loginemp.getEmp_pass().equals(passStr)) {
				return "true";
			}else {
				return "false";
			}
		}
		
		return null;
	}
}
