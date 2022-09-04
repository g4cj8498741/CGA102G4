package com.rcarorder.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carmodel.model.CarModelVO;
import com.rcar.model.RcarVO;

import utils.ReDay;

@Service
public class RcarOrderService {
	
	@Autowired
	private RcarOrderDAO dao;
	
	public RcarOrderService(RcarOrderDAO dao) {
		this.dao = dao;
	}
	
	public List<ReDay> getallday(List<RcarVO> rcar_no, LocalDate date){
		return dao.getallday(rcar_no, date);
	}
	
	public boolean insert(RcarOrderVO rcarOrderVO) {
		return dao.insert(rcarOrderVO);
		
	}


	public void update(CarModelVO carModelVO) {
		
	}


	public List<RcarOrderVO> getAll() {
		return dao.getAll();
	}
	
	public List<RcarOrderVO> getAll(int month) {
		return dao.getAll(month);
	}
	
	
	public List<ReDay> getOthercar(String store, LocalDate Date) { // 查詢 非本站車輛
		List<RcarOrderVO> list = dao.getOthercar(store, Date);//取得時間內所有訂單車輛
		List<ReDay> reDay = new ArrayList<>();
		vo:
		for(RcarOrderVO vo : list) {
			ReDay day = new ReDay();
			LocalDate startTime = vo.getRcaro_ppicktime().toLocalDateTime().toLocalDate();
			LocalDate endTime = vo.getRcaro_pprettime().toLocalDateTime().toLocalDate();
			int rrEndTime = 0;
			if(vo.getRcaro_rrettime() != null) {
				rrEndTime = vo.getRcaro_rrettime().toLocalDateTime().toLocalDate().getDayOfMonth();
			}
			RcarVO getrcarvo = vo.getrcarvo();
			if(getrcarvo.getSt_no().equals(store)) { //過濾本門市車輛
				continue;
			}else if((Date.getDayOfMonth() > endTime.getDayOfMonth()) && !getrcarvo.getRcar_loc().equals(store) && endTime.getMonthValue() == Date.getMonthValue() ) {
				//要是 查歸還地 本站 且還車時間 < now 就會判斷 他目前所在地是不是 本站 不是會消失在外站表
				continue;
			}else if(vo.getRcaro_returnloc_actual() != null && !vo.getRcaro_returnloc_actual().equals(store)) { //過濾實際歸還地點不是預計還車地點車輛 //修改
				continue;
			}
			for(ReDay re : reDay) {
				if(re.getCar_no().equals(vo.getRcar_no())) {
					// 檢查一台車有多筆訂單 去除重複新增
					re.otherStoreCar(startTime.getDayOfMonth(), endTime.getDayOfMonth(), endTime.getMonthValue(), startTime.getMonthValue(), vo.getRcaro_pickuploc(), vo.getRcaro_returnloc(),vo.getRcaro_returnloc_actual(),rrEndTime);
					continue vo;
				}
			}
			day.setCar_no(vo.getRcar_no());
			day.setCar_model(vo.getModel_no());
			day.setStore(getrcarvo.getSt_no());
			day.setStatus(getrcarvo.getRcar_status());
			day.otherStoreCar(startTime.getDayOfMonth(), endTime.getDayOfMonth(), endTime.getMonthValue(), startTime.getMonthValue(), vo.getRcaro_pickuploc(), vo.getRcaro_returnloc(),vo.getRcaro_returnloc_actual(),rrEndTime);
			reDay.add(day);
		}
		return reDay;
	}
	
	public List<RcarOrderVO> getbetween_order(String rcar_no, LocalDate Date) {
		return dao.getbetween_order(rcar_no, Date);
	}
}
