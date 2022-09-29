package org.example.controller;

public class Constants {
	
	private Constants() {}
	
	public static final String SESSION_ATTRIBUTE_USER = "user";
	public static final String SESSION_ATTRIBUTE_ERROR = "error";
	public static final String GUEST_SERVLET_MAPPING = "guest";
	public static final String READER_SERVLET_MAPPING = "reader";
	public static final String LIBRARIAN_SERVLET_MAPPING = "librarian";
	public static final String ADMIN_SERVLET_MAPPING = "admin";
	public static final String ERROR_SERVLET_MAPPING = "error";
	public static final String START_PAGE = "index.html";
	public static final String GUEST_HOME_PAGE = "/WEB-INF/jsps/guest_start_page.jsp";
	public static final String READER_HOME_PAGE = "/WEB-INF/jsps/reader_start_page.jsp";
	public static final String LIBRARIAN_HOME_PAGE = "/WEB-INF/jsps/librarian_start_page.jsp";
	public static final String ADMIN_HOME_PAGE = "/WEB-INF/jsps/admin_start_page.jsp";
	public static final String LOGIN_PAGE = "/WEB-INF/jsps/login.jsp";
	public static final String ERROR_PAGE = "/WEB-INF/jsps/error.jsp";
}
