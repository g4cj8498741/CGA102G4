package com.tool.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carmodel.model.CarModelService;

@RestController
public class SpringMvcImgController {
	
	@Autowired
	private CarModelService carModelService;
	
	@GetMapping("img")
	public void img(String model_no , HttpServletResponse response) {
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(carModelService.getImges(model_no));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
