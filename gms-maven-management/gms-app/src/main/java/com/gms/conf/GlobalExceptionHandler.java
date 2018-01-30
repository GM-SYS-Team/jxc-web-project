package com.gms.conf;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gms.exception.MyException;
import com.gms.util.Constant;

@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * 系统异常
	 */
	@ExceptionHandler({ Exception.class })
	@ResponseBody
	public Map<String, Object> exceptionHandler(Exception e) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (e.getClass().equals(MyException.class) || e.getClass().equals(IllegalArgumentException.class) || e.getClass().equals(NullPointerException.class)) {
			map.put("code", Constant.RET_CODE_ERROR);
			if (StringUtils.isBlank(e.getMessage())) {
				map.put("msg", "系统异常，请稍后再试");
			}
			else {
				map.put("msg", e.getMessage());
			}
		}
		else {
			map.put("code", Constant.RET_CODE_ERROR);
			map.put("msg", "系统异常，请稍后再试");
		}
		e.printStackTrace();
		return map;
	}
}
