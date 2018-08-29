package com.uniovi.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.uniovi.services.util.LogService;

@Component
public class RequestTimeInterceptor extends HandlerInterceptorAdapter {

	private LogService log = new LogService(this);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("time", System.currentTimeMillis());
		return super.preHandle(request, response, handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long time = System.currentTimeMillis()
				- (long) request.getAttribute("time");
		log.info("Request URL: " + request.getRequestURL().toString()
				+ " -> Total time: " + time + " ms");
		super.afterCompletion(request, response, handler, ex);
	}

}
