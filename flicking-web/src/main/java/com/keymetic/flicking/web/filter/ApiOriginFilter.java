package com.keymetic.flicking.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


public class ApiOriginFilter implements javax.servlet.Filter {
	
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {	
		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader("Access-Control-Allow-Origin", "*");
		res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT,");		
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader("Access-Control-Allow-Headers", "x-requested-with,Content-Type,X-Auth-Token");
		chain.doFilter(request, response);
		
		
	}


	public void destroy() {
	}

	
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}