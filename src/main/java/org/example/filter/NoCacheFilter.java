package org.example.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class NoCacheFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, 
						 ServletResponse response, 
						 FilterChain chain)
								 		throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;

		//set cache-control header to disable caching (HTTP/1.1 spec)
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        //set cache-control header to disable caching  (HTTP/1.0(older) spec).. 
        //because some old clients might not support the no-cache header above 
        resp.setHeader("Pragma", "no-cache");

        //set dateHeader to prevent caching at the proxy server
        resp.setDateHeader("Expires", 0); 
		
        chain.doFilter(request, response);
	}


}
