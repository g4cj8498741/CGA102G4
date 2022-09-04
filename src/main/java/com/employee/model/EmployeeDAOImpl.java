package com.employee.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import utils.MysqlJDBC;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
	
	@Autowired
	private DriverManagerDataSource ds;

	private final String INSERT = "INSERT INTO `CGA102G4`.`EMPLOYEE` (`EMP_NO`, `EMP_NAME`, `EMP_PASS`, `ST_NO`, `EMP_GENDER`, `EMP_BIR`, `EMP_TEL`, `EMP_ADRS`, `EMP_MAIL`, `EMP_TITLE`) "
			+ " VALUES ( ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? )";

	private final String UPDATE = "UPDATE `CGA102G4`.`EMPLOYEE` " 
			+ "SET " 
			+ "    `EMP_NAME` = ? ," 
			+ "    `ST_NO` = ? ," 
			+ "    `EMP_TEL` = ? ," 
			+ "    `EMP_ADRS` = ? ," 
			+ "    `EMP_MAIL` = ? ," 
			+ "    `EMP_TITLE` = ? ,"
			+ "    `EMP_STATUS` = ?  " 
			+ "WHERE " 
			+ "    (`EMP_NO` = ? );";

	private final String ALL = "SELECT EMP_NO, EMP_NAME, EMP_PASS, ST_NO, EMP_GENDER, EMP_BIR, EMP_TEL, EMP_ADRS, EMP_MAIL, EMP_TITLE, EMP_STATUS "
			+ "FROM CGA102G4.EMPLOYEE;";

	private final String EMP = "SELECT EMP_NO, EMP_NAME, EMP_PASS, ST_NO, EMP_GENDER, EMP_BIR, EMP_TEL, EMP_ADRS, EMP_MAIL, EMP_TITLE, EMP_STATUS "
			+ "FROM CGA102G4.EMPLOYEE WHERE EMP_MAIL = ? ;";

	private final String LOGIN = "SELECT * FROM employee where EMP_NO = ? and EMP_PASS = ? ; ";
	
	private final String MAXEMPNO ="SELECT max(EMP_NO) FROM employee;";
	
	private final String EMPUP = "UPDATE `CGA102G4`.`EMPLOYEE` "
			+ "SET "
			+ "    EMP_NAME = ?,"
			+ "    EMP_GENDER = ?,"
			+ "    EMP_BIR = ?,"
			+ "    EMP_TEL = ?,"
			+ "    EMP_ADRS = ?,"
			+ "    EMP_MAIL = ?, "
			+ "	   EMP_PASS = ? "
			+ "WHERE "
			+ "    (EMP_NO = ? )";

	// insert
	@Override
	public boolean insert(EmployeeVO EmployeeVO) {
		Connection ct = null;
		PreparedStatement ps = null;
		try {
			ct = ds.getConnection();
			ps = ct.prepareStatement(INSERT);
			ps.setString(1, EmployeeVO.getEmp_no());
			ps.setString(2, EmployeeVO.getEmp_name());
			ps.setString(3, EmployeeVO.getEmp_pass());
			ps.setString(4, EmployeeVO.getSt_no());
			ps.setInt(5, EmployeeVO.getEmp_gender());
			ps.setDate(6, EmployeeVO.getEmp_bir());
			ps.setString(7, EmployeeVO.getEmp_tel());
			ps.setString(8, EmployeeVO.getEmp_adrs());
			ps.setString(9, EmployeeVO.getEmp_mail());
			ps.setString(10, EmployeeVO.getEmp_title());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ct != null) {
				try {
					ct.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// update
	@Override
	public boolean update(EmployeeVO EmployeeVO) {
		Connection ct = null;
		PreparedStatement ps = null;
		try {
			ct = ds.getConnection();
			ps = ct.prepareStatement(UPDATE);
			ps.setString(1, EmployeeVO.getEmp_name());
			ps.setString(2, EmployeeVO.getSt_no());
			ps.setString(3, EmployeeVO.getEmp_tel());
			ps.setString(4, EmployeeVO.getEmp_adrs());
			ps.setString(5, EmployeeVO.getEmp_mail());
			ps.setString(6, EmployeeVO.getEmp_title());
			ps.setInt(7, EmployeeVO.getEmp_status());
			ps.setString(8, EmployeeVO.getEmp_no());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (ps != null) {
				try {
					ps.cancel();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ct != null) {
				try {
					ct.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// getAll
	@Override
	public List<EmployeeVO> getAll() {
		List<EmployeeVO> list = new ArrayList<>();
		try (Connection ct = ds.getConnection(); PreparedStatement ps = ct.prepareStatement(ALL);) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				EmployeeVO vo = new EmployeeVO();
				vo.setEmp_no(rs.getString("EMP_NO"));
				vo.setEmp_name(rs.getString("EMP_NAME"));
				vo.setSt_no(rs.getString("ST_NO"));
				vo.setEmp_gender(rs.getInt("EMP_GENDER"));
				vo.setEmp_bir(rs.getDate("EMP_BIR"));
				vo.setEmp_tel(rs.getString("EMP_TEL"));
				vo.setEmp_adrs(rs.getString("EMP_ADRS"));
				vo.setEmp_mail(rs.getString("EMP_MAIL"));
				vo.setEmp_title(rs.getString("EMP_TITLE"));
				vo.setEmp_status(rs.getInt("EMP_STATUS"));
				list.add(vo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public EmployeeVO emp(String mail) {
		try (Connection ct = ds.getConnection(); PreparedStatement ps = ct.prepareStatement(EMP);) {
			ps.setString(1, mail);
			ResultSet rs = ps.executeQuery();
			rs.next();
			EmployeeVO vo = new EmployeeVO();
			vo.setEmp_no(rs.getString("EMP_NO"));
			vo.setEmp_name(rs.getString("EMP_NAME"));
			vo.setSt_no(rs.getString("ST_NO"));
			vo.setEmp_gender(rs.getInt("EMP_GENDER"));
			vo.setEmp_bir(rs.getDate("EMP_BIR"));
			vo.setEmp_tel(rs.getString("EMP_TEL"));
			vo.setEmp_adrs(rs.getString("EMP_ADRS"));
			vo.setEmp_mail(rs.getString("EMP_MAIL"));
			vo.setEmp_title(rs.getString("EMP_TITLE"));
			vo.setEmp_status(rs.getInt("EMP_STATUS"));
			return vo;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 員工登入
	@Override
	public EmployeeVO emplogin(String username, String password) {
		EmployeeVO employeeVO = new EmployeeVO();
		try (Connection ct = ds.getConnection(); PreparedStatement ps = ct.prepareStatement(LOGIN);) {
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				employeeVO = new EmployeeVO();
				employeeVO.setEmp_no(rs.getString("emp_no"));
				employeeVO.setEmp_name(rs.getString("emp_name"));
				employeeVO.setEmp_pass(rs.getString("emp_pass"));
				employeeVO.setSt_no(rs.getString("st_no"));
				employeeVO.setEmp_gender(rs.getInt("emp_gender"));
				employeeVO.setEmp_bir(rs.getDate("emp_bir"));
				employeeVO.setEmp_tel(rs.getString("emp_tel"));
				employeeVO.setEmp_adrs(rs.getString("emp_adrs"));
				employeeVO.setEmp_mail(rs.getString("emp_mail"));
				employeeVO.setEmp_title(rs.getString("emp_title"));
				employeeVO.setEmp_status(rs.getInt("emp_status"));
				return employeeVO;
			} else {
				return null;
			}
		} catch (SQLException e) {
			return null;
		}

	}
		//新增員工資料進資料庫做判斷
	@Override
	public boolean NewEmployee(String EMP_NO) {	
		try (Connection ct = ds.getConnection();
			PreparedStatement ps = ct.prepareStatement(EMP_NO);	){
			ps.setString(1, EMP_NO);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}	
		
	}
	//找到員工編號最大值
	@Override
	public String maxempno() {
		try (Connection ct = ds.getConnection();
			PreparedStatement ps = ct.prepareStatement(MAXEMPNO);){ //ct.prepareStatement(傳入sql語句)
			ResultSet rs = ps.executeQuery();// ps.executeQuery()  適用查詢  || ps.executeUpdate() 適用修改資料
			rs.next();
			return rs.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
		
	}

	@Override
	public int empUpdata(EmployeeVO EmployeeVO) {
		try (Connection ct = ds.getConnection();
				PreparedStatement ps = ct.prepareStatement(EMPUP);
					){
				ps.setString(1, EmployeeVO.getEmp_name());
				ps.setInt(2, EmployeeVO.getEmp_gender());
				ps.setDate(3, EmployeeVO.getEmp_bir());
				ps.setString(4, EmployeeVO.getEmp_tel());
				ps.setString(5, EmployeeVO.getEmp_adrs());
				ps.setString(6, EmployeeVO.getEmp_mail());
				ps.setString(7, EmployeeVO.getEmp_pass());
				ps.setString(8, EmployeeVO.getEmp_no());
				
				return ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0 ;
			}
	}

}
