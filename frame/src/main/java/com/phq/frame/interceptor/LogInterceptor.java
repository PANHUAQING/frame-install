package com.phq.frame.interceptor;

import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.phq.frame.domain.master.TbLog;
import com.phq.frame.service.master.LogService;
import com.phq.frame.util.IpUtil;
import com.phq.frame.util.UUIDUtils;

/**
 * 
* @ClassName: LogInterceptor
* @Description: 
*    日志拦截器 获取用户的操作日志
* @author panhuaqing
* @date 2018年10月11日
*
 */
public class LogInterceptor implements HandlerInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
	private LogService logService;
	
	private  String uuid ="";
	private TbLog log = new TbLog();
	
	public  LogInterceptor(LogService logService) {
		this.logService = logService;
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//日志拦截器 实现对页面请求的控制
		String sessionId = request.getRequestedSessionId(); //请求sessionid
		String uri =  request.getRequestURI();//url
		Enumeration<String> parameter = request.getParameterNames();
		String clientIp =  IpUtil.getUserIp(request);
	
		try {
			String logLoginName = "";
			String logLoginUserId = "";
		    HttpSession session = request.getSession();
			if(session !=null && session.getAttribute("userName")!=null) {
				logLoginName =  session.getAttribute("userName").toString();
				logLoginUserId =  session.getAttribute("userId").toString();
			}
		    uuid = UUIDUtils.getUUID();
			//日志处理
			log.setLogId(uuid);
			log.setLogCreateTime(new Date());
			log.setLogRequestAttributes(JSONArray.toJSONString(parameter));
			log.setLogRequestClientip(clientIp);
			log.setLogRequestSessionid(sessionId);
			log.setLogRequestStarttime(new Date());
			log.setLogRequestUrl(uri);
			log.setLogLoginName(logLoginName);
			log.setLogLoginUserid(logLoginUserId);
			
		}catch (Exception e) {
			log.setLogException(e.getMessage().substring(0, 100));
		}finally {
			logService.saveLogData(log);
		}
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//日志处理
		log.setLogId(uuid);
		//更新结束时间
		log.setLogRequestEndtime(new Date());
		logService.updateEndTime(log);
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

	
    
}
