package com.carmodel.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

@Repository
public class CarModelDAOImpl implements CarModelDAO {

	// 連線池
	@Autowired
	private DriverManagerDataSource ds;
	
	//新增目前不含圖片
	private final String INSERT = "INSERT INTO `CGA102G4`.`CAR_MODEL` (`MODEL_NO`, `LEVEL_NO`, `MODEL_NAME`, `MODEL_CC`, `MODEL_PRICE`, `MODEL_OIL`, `MODEL_SEATS`, `MODEL_BAGGAGE`, `CAR_INFO`) "
			+ "	VALUES ( ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? );";
	
	private final String UPDATE = "UPDATE `CGA102G4`.`CAR_MODEL` "
			+ "SET "
			+ "    LEVEL_NO = ? ,"
			+ "    MODEL_NAME = ? ,"
			+ "    MODEL_CC = ? ,"
			+ "    MODEL_PRICE = ? ,"
			+ "    MODEL_OIL = ? ,"
			+ "    MODEL_SEATS = ? ,"
			+ "    MODEL_BAGGAGE = ? ,"
			+ "    CAR_INFO = ? "
			+ "WHERE "
			+ "    (MODEL_NO = ? );";
	
	private final String ALL = "SELECT MODEL_NO, LEVEL_NO, MODEL_NAME, MODEL_CC, MODEL_PRICE, MODEL_OIL, MODEL_SEATS, MODEL_BAGGAGE, CAR_INFO, CAR_PHOTO "
			+ "FROM CGA102G4.CAR_MODEL;";
	
	private final String GETIMG = "SELECT car_photo FROM car_model WHERE model_no = ?";


	@Override
	public boolean insert(CarModelVO carModelVO) {
		try (Connection ct = ds.getConnection();
				PreparedStatement ps = ct.prepareStatement(INSERT)) {
			ps.setString(1, carModelVO.getModel_no());
			ps.setString(2, carModelVO.getLevel_no());
			ps.setString(3, carModelVO.getModel_name());
			ps.setInt(4, carModelVO.getModel_cc());
			ps.setInt(5, carModelVO.getModel_price());
			ps.setString(6, carModelVO.getModel_oil());
			ps.setInt(7, carModelVO.getModel_seats());
			ps.setString(8, carModelVO.getModel_baggage());
			ps.setString(9,carModelVO.getCar_info());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(CarModelVO carModelVO) {
		try (Connection ct = ds.getConnection();
				PreparedStatement ps = ct.prepareStatement(UPDATE)) {
			ps.setString(1, carModelVO.getLevel_no());
			ps.setString(2, carModelVO.getModel_name());
			ps.setInt(3, carModelVO.getModel_cc());
			ps.setInt(4, carModelVO.getModel_price());
			ps.setString(5, carModelVO.getModel_oil());
			ps.setInt(6, carModelVO.getModel_seats());
			ps.setString(7, carModelVO.getModel_baggage());
			ps.setString(8,carModelVO.getCar_info());
			ps.setString(9, carModelVO.getModel_no());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<CarModelVO> getAll() {
		
		List<CarModelVO> list = new ArrayList<CarModelVO>();
		CarModelVO carModelVO = null;
		
		try (Connection ct = ds.getConnection();
				PreparedStatement ps = ct.prepareStatement(ALL);
				){
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				carModelVO = new CarModelVO();
				carModelVO.setModel_no(rs.getString("model_no"));
				carModelVO.setLevel_no(rs.getString("level_no"));
				carModelVO.setModel_name(rs.getString("model_name"));
				carModelVO.setModel_cc(rs.getInt("model_cc"));
				carModelVO.setModel_price(rs.getInt("model_price"));
				carModelVO.setModel_oil(rs.getString("model_oil"));
				carModelVO.setModel_seats(rs.getInt("model_seats"));
				carModelVO.setModel_baggage(rs.getString("model_baggage"));
				carModelVO.setCar_info(rs.getString("car_info"));
				carModelVO.setCar_photo(rs.getBytes("car_photo"));
				list.add(carModelVO);
			}
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured." + e.getMessage());// SQL語法錯誤
		}
		return list;
	}

	@Override
	public byte[] getImage(String model_no) {
		
		byte[] images = null;
		
		try (Connection ct = ds.getConnection();
				PreparedStatement ps = ct.prepareStatement(GETIMG);
				){
			ps.setString(1, model_no);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				images = rs.getBytes("car_photo");
			}
		} catch (SQLException e) {
			throw new RuntimeException("A database error occured." + e.getMessage());// SQL語法錯誤
		}
		return images;
	}
}
