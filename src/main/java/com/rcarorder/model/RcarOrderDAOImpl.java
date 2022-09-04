package com.rcarorder.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.rcar.model.RcarVO;

import utils.MysqlJDBC;
import utils.ReDay;


@Repository
public class RcarOrderDAOImpl implements RcarOrderDAO {
	
	@Autowired
	private DriverManagerDataSource ds;
	
	
	
	private final String CAR_ORDER = "SELECT " //取得門市所有車輛訂單 用於配車表上
			+ "    EXTRACT(DAY FROM RCARO_PPICKTIME) AS start, "
			+ "    EXTRACT(DAY FROM RCARO_PPRETTIME) As end, "
			+ "    (MONTH(RCARO_PPICKTIME) + MONTH(RCARO_PPRETTIME)) as total, " // 開始月份 + 結束月份 來判斷 開始或結束日期不再 你要查詢的月份
			+ "    RCAR_NO,"
			+ "    RCARO_PICKUPLOC, "
			+ "    RCARO_RETURNLOC "
			+ "FROM "
			+ "    RCAR_ORDER "
			+ "WHERE "
			+ "    RCAR_NO like ? and (MONTH(RCARO_PPICKTIME) + MONTH(RCARO_PPRETTIME)) in (? , ? , ? , ? ) AND YEAR(RCARO_PPRETTIME) = ? "
			+ "    AND RCARO_STATUS != 4 "
			+ "ORDER BY start;";
	
	
	private final String OTHER_CAR_ORDER = "SELECT * "
			+ "FROM "
			+ "    RCAR_ORDER "
			+ "WHERE "
			+ "    (RCARO_RETURNLOC = ? "
			+ "     OR RCARO_PICKUPLOC = ? OR RCARO_RETURNLOC_ACTUAL = ?)" ////////////////////////////////////////////////// 修改
			+ "        AND (RCARO_PPRETTIME BETWEEN ? AND ? "
			+ "        OR RCARO_PPICKTIME BETWEEN ? AND ? ) "
			+ "        AND RCARO_STATUS != 4 ;";

	
	private final String GET_CAR_BETWEEN_ORDER = "SELECT  "
			+ "  * "
			+ "FROM "
			+ "    RCAR_ORDER "
			+ "WHERE "
			+ "    RCAR_NO = ? "
			+ "        AND (RCARO_PPRETTIME BETWEEN ? AND ? "
			+ "        OR RCARO_PPICKTIME BETWEEN ? AND ? ) "
			+ "        AND RCARO_STATUS != 4;";
	
	private final String All = "SELECT * FROM RCAR_ORDER";
	
	private final String MONTHALL = "SELECT " //取得特定月份所有訂單
			+ "    RCARO_NO,"
			+ "    MEB_NO,"
			+ "    LEVEL_NO,"
			+ "    MODEL_NO,"
			+ "    RCAR_NO,"
			+ "    RCARO_PPICKTIME,"
			+ "    RCARO_PPRETTIME,"
			+ "    RCARO_RPICKTIME,"
			+ "    RCARO_RRETTIME,"
			+ "    RCARO_PICKUPLOC,"
			+ "    RCARO_RETURNLOC,"
			+ "    RCARO_RETURNLOC_ACTUAL,"
			+ "    RCARO_TIME,"
			+ "    RCARO_PAY,"
			+ "    RCARO_EXTRA_PAY,"
			+ "    RCARO_EXTRA_PAY_STATUS,"
			+ "    CONSUME_POINT,"
			+ "    EARN_POINT,"
			+ "    EVENT_NO,"
			+ "    RCARO_STATUS,"
			+ "    RCARO_NOTE,"
			+ "    LESSEE_NAME "
			+ "FROM "
			+ "    CGA102G4.RCAR_ORDER "
			+ "WHERE "
			+ "    (MONTH(RCARO_PPICKTIME) = ? "
			+ "OR  MONTH(RCARO_PPRETTIME) = ?) "
			+ "AND RCARO_STATUS != 4 ";
	
	private final String INSERT = "INSERT INTO `CGA102G4`.`RCAR_ORDER` (`MEB_NO`, `MODEL_NO`, `RCARO_PPICKTIME`, `RCARO_PPRETTIME`, `RCARO_PICKUPLOC`, `RCARO_RETURNLOC`, `RCARO_PAY`, `CONSUME_POINT`, `EVENT_NO`, `RCARO_NOTE`, `LESSEE_NAME`) "
			+ " VALUES ( ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? );";
	

	@Override
	public boolean insert(RcarOrderVO rcarOrderVO) { ///未加LEVEL_NO
		try (Connection ct = ds.getConnection();
			PreparedStatement ps = ct.prepareStatement(INSERT);){ 
			ps.setString(1,rcarOrderVO.getMeb_no());
			ps.setString(2,rcarOrderVO.getModel_no());
			ps.setTimestamp(3, rcarOrderVO.getRcaro_ppicktime());
			ps.setTimestamp(4, rcarOrderVO.getRcaro_pprettime());
			ps.setString(5,rcarOrderVO.getRcaro_pickuploc());
			ps.setString(6, rcarOrderVO.getRcaro_returnloc());
//			ps.setTimestamp(7, rcarOrderVO.getRcaro_date()); //新版本自動更新
			ps.setInt(7,rcarOrderVO.getRcaro_pay()); //無使用給0
			ps.setInt(8,rcarOrderVO.getConsume_point());
			ps.setInt(9, rcarOrderVO.getEvent_no()); //無活動也要給值
			ps.setString(10, rcarOrderVO.getRcaro_note());
			ps.setString(11, rcarOrderVO.getLessee_name());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean update(RcarOrderVO rcarOrderVO) {
		return true;
	}


	@Override
	public List<RcarOrderVO> getAll() { //取得所有訂單
		List<RcarOrderVO> list = new ArrayList<>();
		try (Connection ct = ds.getConnection();
			PreparedStatement ps = ct.prepareStatement(All);){
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				RcarOrderVO orderVO = new RcarOrderVO();
				orderVO.setRcaro_no(rs.getInt("RCARO_NO"));
				orderVO.setMeb_no(rs.getString("MEB_NO"));
				orderVO.setLevel_no(rs.getString("LEVEL_NO"));
				orderVO.setModel_no(rs.getString("MODEL_NO"));
				orderVO.setRcar_no(rs.getString("RCAR_NO"));
				orderVO.setRcaro_ppicktime(rs.getTimestamp("RCARO_PPICKTIME"));
				orderVO.setRcaro_pprettime(rs.getTimestamp("RCARO_PPRETTIME"));
				orderVO.setRcaro_rpicktime(rs.getTimestamp("RCARO_RPICKTIME"));
				orderVO.setRcaro_rrettime(rs.getTimestamp("RCARO_RRETTIME"));
				orderVO.setRcaro_pickuploc(rs.getString("RCARO_PICKUPLOC"));
				orderVO.setRcaro_returnloc(rs.getString("RCARO_RETURNLOC"));
				orderVO.setRcaro_returnloc_actual(rs.getString("RCARO_RETURNLOC_ACTUAL"));
				orderVO.setRcaro_time(rs.getTimestamp("RCARO_TIME"));
				orderVO.setRcaro_pay(rs.getInt("RCARO_PAY"));
				orderVO.setRcaro_extra_pay(rs.getInt("RCARO_EXTRA_PAY"));
				orderVO.setRcaro_extra_pay_status(rs.getInt("RCARO_EXTRA_PAY_STATUS"));
				orderVO.setConsume_point(rs.getInt("CONSUME_POINT"));
				orderVO.setEarn_point(rs.getInt("EARN_POINT"));
				orderVO.setEvent_no(rs.getInt("EVENT_NO"));
				orderVO.setRcaro_status(rs.getInt("RCARO_STATUS"));
				orderVO.setRcaro_note(rs.getString("RCARO_NOTE"));
				orderVO.setLessee_name(rs.getString("LESSEE_NAME"));
				list.add(orderVO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<RcarOrderVO> getAll(int month) { //取得特定月份所有訂單
		List<RcarOrderVO> list = new ArrayList<>();
		try (Connection ct = ds.getConnection();
			PreparedStatement ps = ct.prepareStatement(MONTHALL);){
			ps.setInt(1, month);
			ps.setInt(2, month);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				RcarOrderVO orderVO = new RcarOrderVO();
				orderVO.setRcaro_no(rs.getInt("RCARO_NO"));
				orderVO.setMeb_no(rs.getString("MEB_NO"));
				orderVO.setLevel_no(rs.getString("LEVEL_NO"));
				orderVO.setModel_no(rs.getString("MODEL_NO"));
				orderVO.setRcar_no(rs.getString("RCAR_NO"));
				orderVO.setRcaro_ppicktime(rs.getTimestamp("RCARO_PPICKTIME"));
				orderVO.setRcaro_pprettime(rs.getTimestamp("RCARO_PPRETTIME"));
				orderVO.setRcaro_rpicktime(rs.getTimestamp("RCARO_RPICKTIME"));
				orderVO.setRcaro_rrettime(rs.getTimestamp("RCARO_RRETTIME"));
				orderVO.setRcaro_pickuploc(rs.getString("RCARO_PICKUPLOC"));
				orderVO.setRcaro_returnloc(rs.getString("RCARO_RETURNLOC"));
				orderVO.setRcaro_returnloc_actual(rs.getString("RCARO_RETURNLOC_ACTUAL"));
				orderVO.setRcaro_time(rs.getTimestamp("RCARO_TIME"));
				orderVO.setRcaro_pay(rs.getInt("RCARO_PAY"));
				orderVO.setRcaro_extra_pay(rs.getInt("RCARO_EXTRA_PAY"));
				orderVO.setRcaro_extra_pay_status(rs.getInt("RCARO_EXTRA_PAY_STATUS"));
				orderVO.setConsume_point(rs.getInt("CONSUME_POINT"));
				orderVO.setEarn_point(rs.getInt("EARN_POINT"));
				orderVO.setEvent_no(rs.getInt("EVENT_NO"));
				orderVO.setRcaro_status(rs.getInt("RCARO_STATUS"));
				orderVO.setRcaro_note(rs.getString("RCARO_NOTE"));
				orderVO.setLessee_name(rs.getString("LESSEE_NAME"));
				list.add(orderVO);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public List<ReDay> getallday(List<RcarVO> st_rcar_no, LocalDate date) { //取得門市當月訂單 用於配車表用

		ArrayList<ReDay> arrayList = new ArrayList<>();
		int month = date.getMonthValue();
		int summonth = date.getMonthValue() * 2; // 將月份 * 2 
		for (RcarVO no : st_rcar_no) { //將每台車 new 一個ReDay 存放出租日期
			ReDay re = new ReDay();
			re.setCar_no(no.getRcar_no());
			re.setCar_model(no.getModel_no());
			re.setStore(no.getSt_no());
			re.setStatus(no.getRcar_status());
			arrayList.add(re);
		}
		String plate = st_rcar_no.get(0).getRcar_no().substring(0, 3);//取得車牌 前面3個字做模糊查詢
		try (Connection ct = ds.getConnection(); 
				PreparedStatement ps = ct.prepareStatement(CAR_ORDER);) {
			ps.setString(1, "%"+plate+"%");
			ps.setInt(2, summonth-2);
			ps.setInt(3, summonth-1);
			ps.setInt(4, summonth);
			ps.setInt(5, summonth+1);
			ps.setInt(6, date.getYear());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String car_no = rs.getString("RCAR_NO"); //取得查詢到的車牌
				for (ReDay no : arrayList) { // 將所有車輛進行比對
					if(car_no.equals(no.getCar_no())) {
						no.day(rs.getInt(1), rs.getInt(2),rs.getInt(3),month,rs.getString("RCARO_PICKUPLOC"),rs.getString("RCARO_RETURNLOC"));
						break;
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayList;
	}
	
	@Override
	public List<RcarOrderVO> getOthercar(String store, LocalDate Date) {//取得還車點 是本門市訂單 區間~+7天車輛 /////////////////////////// 修改
		ArrayList<RcarOrderVO> list = new ArrayList<>();
		try (Connection ct = ds.getConnection(); 
				PreparedStatement ps = ct.prepareStatement(OTHER_CAR_ORDER);) {
			ps.setString(1, store);
			ps.setString(2, store);
			ps.setString(3, store);
			ps.setObject(4, LocalDateTime.of(Date, LocalTime.MIN));
			ps.setObject(5, LocalDateTime.of(Date, LocalTime.MAX).plusDays(7));
			ps.setObject(6, LocalDateTime.of(Date, LocalTime.MIN));
			ps.setObject(7, LocalDateTime.of(Date, LocalTime.MAX).plusDays(7));
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				RcarOrderVO orderVO = new RcarOrderVO();
				orderVO.setRcaro_no(rs.getInt("RCARO_NO"));
				orderVO.setMeb_no(rs.getString("MEB_NO"));
				orderVO.setLevel_no(rs.getString("LEVEL_NO"));
				orderVO.setModel_no(rs.getString("MODEL_NO"));
				orderVO.setRcar_no(rs.getString("RCAR_NO"));
				orderVO.setRcaro_ppicktime(rs.getTimestamp("RCARO_PPICKTIME"));
				orderVO.setRcaro_pprettime(rs.getTimestamp("RCARO_PPRETTIME"));
				orderVO.setRcaro_rpicktime(rs.getTimestamp("RCARO_RPICKTIME"));
				orderVO.setRcaro_rrettime(rs.getTimestamp("RCARO_RRETTIME"));
				orderVO.setRcaro_pickuploc(rs.getString("RCARO_PICKUPLOC"));
				orderVO.setRcaro_returnloc(rs.getString("RCARO_RETURNLOC"));
				orderVO.setRcaro_returnloc_actual(rs.getString("RCARO_RETURNLOC_ACTUAL"));
				orderVO.setRcaro_time(rs.getTimestamp("RCARO_TIME"));
				orderVO.setRcaro_pay(rs.getInt("RCARO_PAY"));
				orderVO.setRcaro_extra_pay(rs.getInt("RCARO_EXTRA_PAY"));
				orderVO.setRcaro_extra_pay_status(rs.getInt("RCARO_EXTRA_PAY_STATUS"));
				orderVO.setConsume_point(rs.getInt("CONSUME_POINT"));
				orderVO.setEarn_point(rs.getInt("EARN_POINT"));
				orderVO.setEvent_no(rs.getInt("EVENT_NO"));
				orderVO.setRcaro_status(rs.getInt("RCARO_STATUS"));
				orderVO.setRcaro_note(rs.getString("RCARO_NOTE"));
				orderVO.setLessee_name(rs.getString("LESSEE_NAME"));
				list.add(orderVO);
			}
			return list;
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<RcarOrderVO> getbetween_order(String rcar_no, LocalDate Date) { //取得車輛 區間內訂單
		List<RcarOrderVO> list = new ArrayList<>();
		try (Connection ct = ds.getConnection();
			PreparedStatement ps = ct.prepareStatement(GET_CAR_BETWEEN_ORDER);) {
			ps.setString(1, rcar_no);
			ps.setObject(2, LocalDateTime.of(Date, LocalTime.MIN));
			ps.setObject(3, LocalDateTime.of(Date, LocalTime.MAX).plusDays(7));
			ps.setObject(4, LocalDateTime.of(Date, LocalTime.MIN));
			ps.setObject(5, LocalDateTime.of(Date, LocalTime.MAX).plusDays(7));
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				RcarOrderVO orderVO = new RcarOrderVO();
				orderVO.setRcaro_no(rs.getInt("RCARO_NO"));
				orderVO.setMeb_no(rs.getString("MEB_NO"));
				orderVO.setLevel_no(rs.getString("LEVEL_NO"));
				orderVO.setModel_no(rs.getString("MODEL_NO"));
				orderVO.setRcar_no(rs.getString("RCAR_NO"));
				orderVO.setRcaro_ppicktime(rs.getTimestamp("RCARO_PPICKTIME"));
				orderVO.setRcaro_pprettime(rs.getTimestamp("RCARO_PPRETTIME"));
				orderVO.setRcaro_rpicktime(rs.getTimestamp("RCARO_RPICKTIME"));
				orderVO.setRcaro_rrettime(rs.getTimestamp("RCARO_RRETTIME"));
				orderVO.setRcaro_pickuploc(rs.getString("RCARO_PICKUPLOC"));
				orderVO.setRcaro_returnloc(rs.getString("RCARO_RETURNLOC"));
				orderVO.setRcaro_returnloc_actual(rs.getString("RCARO_RETURNLOC_ACTUAL"));
				orderVO.setRcaro_time(rs.getTimestamp("RCARO_TIME"));
				orderVO.setRcaro_pay(rs.getInt("RCARO_PAY"));
				orderVO.setRcaro_extra_pay(rs.getInt("RCARO_EXTRA_PAY"));
				orderVO.setRcaro_extra_pay_status(rs.getInt("RCARO_EXTRA_PAY_STATUS"));
				orderVO.setConsume_point(rs.getInt("CONSUME_POINT"));
				orderVO.setEarn_point(rs.getInt("EARN_POINT"));
				orderVO.setEvent_no(rs.getInt("EVENT_NO"));
				orderVO.setRcaro_status(rs.getInt("RCARO_STATUS"));
				orderVO.setRcaro_note(rs.getString("RCARO_NOTE"));
				orderVO.setLessee_name(rs.getString("LESSEE_NAME"));
				list.add(orderVO);
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
