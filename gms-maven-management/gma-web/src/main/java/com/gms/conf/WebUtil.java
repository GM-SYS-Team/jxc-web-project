package com.gms.conf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gms.util.StringUtil;


public class WebUtil {
	static Logger log = LoggerFactory.getLogger(WebUtil.class);
	
	/**
	 * 是否AJAX请求
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		return  (request.getHeader("X-Requested-With") != null  && "XMLHttpRequest".equals( request.getHeader("X-Requested-With").toString()));
	}
	
	/**
	 * 获取WEB根目录
	 * @return
	 */
	public static String getWebRoot(HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath("/");
	}
	
	/**
	 * 获取完整的访问url
	 * @param in
	 * @return
	 */
	public static String getFullUrl(HttpServletRequest request){
		StringBuffer requstUrl = request.getRequestURL();
		if (request.getQueryString() != null) {
			requstUrl.append('?');
			requstUrl.append(request.getQueryString());
		}
		return requstUrl.toString();
	}
	
	/*** 
     * 服务器是否是本机 
     * @param request 
     * @return 
     */  
	public static boolean isLocalIp(HttpServletRequest request){  
        String ip= getClientIpAddr(request);  
        return ip.equals("127.0.0.1")||ip.equals("localhost")||ip.equals("0:0:0:0:0:0:0:1");  
    }  
	
	/*** 
     * 获取客户端ip地址(可以穿透代理) 
     * @param request 
     * @return 
     */  
    public static String getClientIpAddr(HttpServletRequest request) {    
        String ip = request.getHeader("X-Forwarded-For");    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("Proxy-Client-IP");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("WL-Proxy-Client-IP");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_X_FORWARDED");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_CLIENT_IP");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_FORWARDED_FOR");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_FORWARDED");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("HTTP_VIA");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getHeader("REMOTE_ADDR");    
        }    
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
            ip = request.getRemoteAddr();    
        }    
        return ip;    
    }  
    public static String getIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Real-IP");  
        if (null != ip && !"".equals(ip.trim())  
                && !"unknown".equalsIgnoreCase(ip)) {  
            return ip;  
        }  
        ip = request.getHeader("X-Forwarded-For");  
        if (null != ip && !"".equals(ip.trim())  
                && !"unknown".equalsIgnoreCase(ip)) {  
            // get first ip from proxy ip  
            int index = ip.indexOf(',');  
            if (index != -1) {  
                return ip.substring(0, index);  
            } else {  
                return ip;  
            }  
        }  
        return request.getRemoteAddr();  
    }  
	
	//从输入流读取字符串  
    public static String readStreamParameter(InputStream in){  
        StringBuilder buffer = new StringBuilder();  
        BufferedReader reader=null;  
        try{  
            reader = new BufferedReader(new InputStreamReader(in));  
            String line=null;  
            while((line = reader.readLine())!=null){  
            	//log.debug("..............line = "+line);
                buffer.append(line);  
            }  
        }catch(Exception e){  
        	log.error("读取输入流异常:{}",e);  
        }finally{  
            if(null!=reader){  
                try {  
                    reader.close();  
                } catch (IOException e) {  
                	log.error("关闭输入流异常:{}",e); 
                }  
            }  
        }  
        return buffer.toString();  
    } 
    
    public static void setSession( HttpServletRequest request ,Map pramaMap){
    	if(pramaMap == null){
    		return;
    	}
    	HttpSession session = request.getSession();
    	Set keySet = pramaMap.keySet();
    	Iterator<String> itr = keySet.iterator();
    	while(itr.hasNext()){
    		String key = itr.next();
    		session.setAttribute(key, pramaMap.get(key));
    	}

    }
    
    public static Object getSessionValue(HttpServletRequest request ,String key){
    	return request.getSession().getAttribute(key);
    	
    }
}
