package com.gms.conf;

import java.util.HashMap;
import java.util.Map;

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
		map.put("code", Constant.RET_CODE_ERROR);
		map.put("msg", "系统异常，请稍后再试！");
		e.printStackTrace();
		return map;
	}
	/**
	 * 业务异常
	 */
	@ExceptionHandler({ MyException.class })
	@ResponseBody
	public Map<String, Object> exceptionHandler(MyException e) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", Constant.RET_CODE_ERROR);
		map.put("msg", e.getMessage());
		return map;
	}
}
