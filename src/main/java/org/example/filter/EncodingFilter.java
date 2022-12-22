package org.example.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The {@code EncodingFilter} class is used to override the name of the 
 * character encoding used in the body of the request
 * 
 * @author Vyacheslav Fedchenko
 */

public class EncodingFilter implements Filter {
	
	/** The name of the encoding initialization parameter */
	private static final String ENCODING_PARAMETER_NAME = "encoding";

	/** The {@code encoding} is used for character encoding storage */
	private String encoding;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		encoding = filterConfig.getInitParameter(ENCODING_PARAMETER_NAME);
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, 
						 FilterChain chain) throws IOException, ServletException {
		req.setCharacterEncoding(encoding);
		chain.doFilter(req, resp);
	}
}
