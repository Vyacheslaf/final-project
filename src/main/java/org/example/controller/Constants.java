package org.example.controller;

public class Constants {
	
	private Constants() {}
	
	public static final String SESSION_ATTRIBUTE_USER = "user";
	public static final String SESSION_ATTRIBUTE_ERROR = "error";
	public static final String SESSION_ATTRIBUTE_SORT_BY = "sortBy";
	public static final String SESSION_ATTRIBUTE_SORT_TYPE = "sortType";
	public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE = "errormessage";
	public static final String SESSION_ATTRIBUTE_INFO_MESSAGE = "infomessage";
	public static final String GUEST_SERVLET_MAPPING = "guest";
	public static final String READER_SERVLET_MAPPING = "reader";
	public static final String READER_ORDERS_SERVLET = "readerorders";
	public static final String LIBRARIAN_SERVLET_MAPPING = "librarian";
	public static final String ADMIN_SERVLET_MAPPING = "admin";
	public static final String READER_DETAILS_SERVLET_MAPPING = "readerdetails";
//	public static final String ERROR_SERVLET_MAPPING = "error";
	public static final String START_PAGE = "index.html";
	public static final String GUEST_HOME_PAGE = "/WEB-INF/jsp/guest.jsp";
	public static final String READER_HOME_PAGE = "/WEB-INF/jsp/reader.jsp";
	public static final String READER_ORDERS_PAGE = "/WEB-INF/jsp/reader_orders.jsp";
	public static final String READER_BOOKS_PAGE = "/WEB-INF/jsp/reader_books.jsp";
	public static final String LIBRARIAN_HOME_PAGE = "/WEB-INF/jsp/librarian_orders.jsp";
	public static final String ADMIN_HOME_PAGE = "/WEB-INF/jsp/manage_books.jsp";
//	public static final String LOGIN_PAGE = "/WEB-INF/jsp/login.jsp";
	public static final String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
	public static final String ORDER_DETAILS_PAGE = "/WEB-INF/jsp/order_details.jsp";
	public static final String LIBRARIAN_LIST_READERS_PAGE = "/WEB-INF/jsp/librarian_readers.jsp";
	public static final String READER_DETAILS_PAGE = "/WEB-INF/jsp/reader_details.jsp";
	public static final String BOOK_DETAILS_PAGE = "/WEB-INF/jsp/book_details.jsp";
	public static final String MANAGE_LIBRARIANS_PAGE = "/WEB-INF/jsp/manage_librarians.jsp";
	public static final String MANAGE_READERS_PAGE = "/WEB-INF/jsp/manage_readers.jsp";
}
