package com.employee.model;
import java.util.List;


public interface EmployeeDAO {
	//新增
	boolean insert(EmployeeVO EmployeeVO);
	//修改
	boolean update(EmployeeVO EmployeeVO);
	//預覽ALL
	List<EmployeeVO> getAll();
	//取得個人員工資料
	EmployeeVO emp(String mail);
	//登入判斷員工編號不可重複
	boolean NewEmployee(String EMP_NO);
	//登入驗證
	EmployeeVO emplogin(String username, String password);
	// 查詢員工編號最大值
	String maxempno();
	// 員工自行更改資料
	int empUpdata(EmployeeVO EmployeeVO);
}
