package com.gms.conf;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.gms.exception.GmsNotLoginException;
import com.gms.exception.GmsRuntimeException;
import com.gms.util.Constant;


public class MyExceptionHandler implements HandlerExceptionResolver{
	private static Logger log = LogManager.getLogger(MyExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2,
			Exception exception) {
		
		Map<String, Object> model = new HashMap<String, Object>();  
		model.put("exception", exception);
		log.error("异常:", exception);
		
		if(exception instanceof GmsNotLoginException){	//未登录的异常
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", Constant.RET_CODE_NOT_LOGIN_EXCEPTION);
			resultJson.put("msg", Constant.RET_CODE_NOT_LOGIN_EXCEPTION_MSG);
			request.setAttribute("JSON_RESULT", resultJson.toString());
			request.setAttribute("url", "../SMHome/login");
			return new ModelAndView("pages/Common/forbid", model);
		}else if(exception instanceof GmsRuntimeException){ //业务异常
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", ((GmsRuntimeException) exception).getResult().getCode());
			resultJson.put("msg", ((GmsRuntimeException) exception).getResult().getMsg());
			request.setAttribute("JSON_RESULT", resultJson.toString());
			if(WebUtil.isAjaxRequest(request)){
				return new ModelAndView("pages/Common/json", model);
			}else{
				return new ModelAndView("pages/Common/exception", model);
			}
		}else{ //其他异常
			JSONObject resultJson = new JSONObject();
			resultJson.put("code", Constant.RET_CODE_ERRO_SYS_EXCEPTION);
			//resultJson.put("msg", ExceptionConst.RET_CODE_ERRO_SYS_EXCEPTION_MSG);
			resultJson.put("msg", exception.getMessage());
			request.setAttribute("JSON_RESULT", resultJson.toString());
			if(WebUtil.isAjaxRequest(request)){
				return new ModelAndView("pages/Common/json", model);
			}else{
				return new ModelAndView("pages/Common/exception", model);
			}
		}
	}

}
