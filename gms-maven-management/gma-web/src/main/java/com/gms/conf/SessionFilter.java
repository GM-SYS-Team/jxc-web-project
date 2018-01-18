package com.gms.conf;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;

import com.gms.entity.jxc.User;
import com.gms.util.StringUtil;

public class SessionFilter extends AccessControlFilter{

	@Override
	protected boolean isAccessAllowed(ServletRequest arg0,
			ServletResponse arg1, Object arg2) throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest arg0, ServletResponse arg1)
			throws Exception {
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		User currentUser = (User)request.getSession().getAttribute("currentUser");
		String requestType = request.getHeader("X-Requested-With");
		request.getRequestURL();
		if(currentUser==null && StringUtil.isValid(requestType) && requestType.equalsIgnoreCase("XMLHttpRequest")){
			java.io.PrintWriter out = response.getWriter();    
		    out.println("<html>");    
		    out.println("<script>");    
		    out.println("window.open ('/login.html','_top')");    
		    out.println("</script>");    
		    out.println("</html>"); 
			return false;
		}
		return true;
	}

}
