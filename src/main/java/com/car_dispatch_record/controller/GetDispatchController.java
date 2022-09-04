package com.car_dispatch_record.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.car_dispatch_record.model.Car_Dispatch_RecordService;
import com.car_dispatch_record.model.Car_Dispatch_RecordVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rcar.model.RcarService;
import com.rcar.model.RcarVO;
import com.store.model.StoreService;
import com.store.model.StoreVO;
import com.tool.controller.WebSocket;

@RestController
public class GetDispatchController {

	@Autowired
	private Car_Dispatch_RecordService service;
	
	@Autowired
	private WebSocket webSocket;
	
	@Autowired
	private RcarService rcarService;
	
	@Autowired
	private StoreService stService;
	
	private Map<String, String> stVo = null;
	
	@RequestMapping("dispatch")
	public String getdispatchservlet(@RequestParam(value = "month",required = false) String monthStr,
			@RequestParam(value = "store", required = false) String storeStr,
			@RequestParam(value = "status", required = false) String statusStr,
			HttpServletRequest req) {
		
		try (ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("classpath:dispatcher.xml")) {
			stVo = stService.getAll().stream().collect(Collectors.toMap(StoreVO::getSt_no,StoreVO::getSt_name));
			Gson gson = ioc.getBean(GsonBuilder.class).serializeNulls().setDateFormat("yyyy-MM-dd HH:mm").create();
			if ("selfstore".equals(statusStr)) { // 顯示自己門市車輛 調車紀錄 配車表
				LocalDate month = LocalDate.parse(monthStr);
				List<Car_Dispatch_RecordVO> list = service.getStoreMonthRecord(month, storeStr);
				
				String json = gson.toJson(list);
				return json;
			}
			
			if ("othercar".equals(statusStr)) { // 顯示外站車輛 調車紀錄 配車表
				LocalDate month = LocalDate.parse(monthStr);
				List<Car_Dispatch_RecordVO> list = service.getOtherCarRecord(month, storeStr);

				String json = gson.toJson(list);
				return json;
			}
			
			if ("record".equals(statusStr)) { // 顯示自己門市車輛 調車紀錄
				LocalDate month = LocalDate.parse(monthStr);
				List<Car_Dispatch_RecordVO> list = service.getStoreMonthDR(month, storeStr);
				System.out.println("record=" + "月:" + monthStr + list);

				String json = gson.toJson(list);
				return json;
			}
			
			if ("update".equals(statusStr)) { // 更新 核准狀態
				String idStr = req.getParameter("id");
				String statusValStr = req.getParameter("statusVal");
				String empStr = req.getParameter("emp");
				try {
					if (service.aprroveDispatch(Integer.parseInt(idStr), Integer.parseInt(statusValStr), empStr)) {
						Car_Dispatch_RecordVO vo = service.getDispatchRecord(Integer.parseInt(idStr));
						if(Integer.parseInt(statusValStr) == 1) { //同意審核
							webSocket.sendMessage(vo.getDr_apply_st(), stVo.get(vo.getDr_approve_st()) +" 核准申請! 調度單號:"+vo.getDr_no());
						}else if(Integer.parseInt(statusValStr) == 2) { //駁回
							webSocket.sendMessage(vo.getDr_apply_st(), stVo.get(vo.getDr_approve_st()) +" 駁回申請! 調度單號:"+vo.getDr_no());
						}
						return "true";
					} else {
						return "false";
					}
				} catch (Exception e) {
					
					return "false";
				}
			}

			if ("insert".equals(statusStr)) { // 申請調度
				String applyEmpStr = req.getParameter("applyEmp");
				String applyStStr = req.getParameter("applySt");
				String approveStStr = req.getParameter("approveSt");
				String drStartTimeStr = req.getParameter("drStartTime");

				
				try {
					LocalDateTime drStartTime = LocalDateTime.of(LocalDate.parse(drStartTimeStr), LocalTime.now());
					
					if(LocalDateTime.now().compareTo(drStartTime) == 1) { //後端擋下 小於(現在時間) 的申請時間
						return "false";
					}
					
					String drRcarStr = req.getParameter("drRcar");
					Car_Dispatch_RecordVO vo = new Car_Dispatch_RecordVO();
					vo.setDr_apply_emp(applyEmpStr);
					vo.setDr_apply_st(applyStStr);
					vo.setDr_approve_st(approveStStr);
					vo.setDr_start_time(Timestamp.valueOf(drStartTime));
					vo.setDr_end_time(Timestamp.valueOf(drStartTime.plusHours(2)));
					vo.setRcar_no(drRcarStr);
					boolean dispatch = service.applyDispatch(vo);
					if (dispatch) {
						webSocket.sendMessage(approveStStr, stVo.get(applyStStr)+" 申請一筆調度");
						return "true";
					} else {
						return "false";
					}

				} catch (Exception e) {
					return "false";
				}
			}

			if ("applyrecord".equals(statusStr)) { // 查看自己門市申請調度 紀錄
				LocalDate month = LocalDate.parse(monthStr);
				List<Car_Dispatch_RecordVO> list = service.getApplyRecord(month, storeStr);
				System.out.println("applyrecord=" + "月:" + monthStr + list);
				String json = gson.toJson(list);
				return json;
			}

			if ("delete".equals(statusStr)) { // 取消調度
				String idStr = req.getParameter("id");
				
				try {
					Car_Dispatch_RecordVO dispatchRecord = service.getDispatchRecord(Integer.parseInt(idStr)); 
					if(dispatchRecord.getDr_approve() >= 3) {
//					new Exception();
						return "false";
					}
					
					boolean cancelDispatch = service.cancelDispatch(Integer.parseInt(idStr));
					if (cancelDispatch) {
						return "true";
					} else {
						return "false";
					}
				} catch (Exception e) {
					return "false";
				}
			}

			if ("outcar".equals(statusStr)) { // 出車
				String idStr = req.getParameter("id");
				String rcarNoStr = req.getParameter("rcarNo");
				String rcarLocStr = req.getParameter("rcar_loc");
				Car_Dispatch_RecordVO recordVO = new Car_Dispatch_RecordVO();
				try {
					recordVO.setRcar_no(rcarNoStr);
					recordVO.setDr_no(Integer.parseInt(idStr));
					recordVO.setDr_apply_st(rcarLocStr);
					boolean carDispatch = service.carDispatch(recordVO);
					if (carDispatch) {
						Car_Dispatch_RecordVO vo = service.getDispatchRecord(Integer.parseInt(idStr));
						webSocket.sendMessage(vo.getDr_apply_st(), stVo.get(vo.getDr_approve_st())+" 出車! 調度單號:"+vo.getDr_no()+" 出車里程:"+ vo.getMiles_before());
						return "true";
					} else {
						return "false";
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "false";
				}
			}

			if ("incar".equals(statusStr)) { // 還車
				String idStr = req.getParameter("id");
				String rcarNoStr = req.getParameter("rcarNo");
				String drMilesStr = req.getParameter("drMiles");
				Car_Dispatch_RecordVO recordVO = new Car_Dispatch_RecordVO();
				try {
					recordVO.setRcar_no(rcarNoStr);
					recordVO.setDr_no(Integer.parseInt(idStr));
					recordVO.setMiles_after(Integer.parseInt(drMilesStr));
					boolean dispatchReturn = service.dispatchReturn(recordVO);
					if (dispatchReturn) {
						Car_Dispatch_RecordVO vo = service.getDispatchRecord(Integer.parseInt(idStr));
						webSocket.sendMessage(vo.getDr_approve_st(), stVo.get(vo.getDr_apply_st())+" 還車! 調度單號:"+vo.getDr_no());
						return "true";
					} else {
						return "false";
					}
				} catch (Exception e) {
					return "false";
				}

				
			}

			if ("upstatus".equals(statusStr)) { // 修改 里程 狀態
				String jsonStr = req.getParameter("json");
				System.out.println(jsonStr);
				RcarVO rcarVO = new RcarVO();
				try {
					rcarVO = gson.fromJson(jsonStr, rcarVO.getClass());
					boolean update = rcarService.update(rcarVO);
					if(update) {
						return "true";
					}else {
						return "false";
					}
				} catch (Exception e) {
					return "false";
				}
			}
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
