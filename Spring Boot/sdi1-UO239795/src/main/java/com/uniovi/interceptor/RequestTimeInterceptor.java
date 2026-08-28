package com.uniovi.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.uniovi.services.util.LogService;

@Component
public class RequestTimeInterceptor implements HandlerInterceptor {

	private final LogService log = new LogService(this);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("time", System.currentTimeMillis());
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long time = System.currentTimeMillis()
				- (long) request.getAttribute("time");
		log.info("Request URL: " + request.getRequestURL().toString()
				+ " -> Total time: " + time + " ms");
	}

}
