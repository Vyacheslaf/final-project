package org.example.controller;

/**
 * A {@code Constants} class contains names for session attributes, servlets, pathnames to jsp pages.
 * 
 * @author Vyacheslav Fedchenko
 *
 */
public class Constants {
	
	private Constants() {}
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the currently logged {@code User} to the <code>HttpSession</code>
	 */
	public static final String SESSION_ATTRIBUTE_USER = "user";
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the sorting field's name to the <code>HttpSession</code>
	 */
	public static final String SESSION_ATTRIBUTE_SORT_BY = "sortBy";
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store {@code asc} or {@code desc} sorting type to the <code>HttpSession</code>
	 */
	public static final String SESSION_ATTRIBUTE_SORT_TYPE = "sortType";
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the error message to the <code>HttpSession</code>
	 */
	public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE = "errormessage";
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the info message to the <code>HttpSession</code>
	 */
	public static final String SESSION_ATTRIBUTE_INFO_MESSAGE = "infomessage";
	
	/**
	 * The {@code String} specifying the mapping name of the reader servlet
	 */
	public static final String READER_SERVLET_MAPPING = "reader";
	
	/**
	 * The {@code String} specifying the mapping name of the librarian servlet
	 */
	public static final String LIBRARIAN_SERVLET_MAPPING = "librarian";
	
	/**
	 * The {@code String} specifying the mapping name of the admin servlet
	 */
	public static final String ADMIN_SERVLET_MAPPING = "admin";
	
	/**
	 * The {@code String} specifying the mapping name of the login servlet
	 */
	public static final String LOGIN_SERVLET_MAPPING = "login";
	
	/**
	 * The {@code String} specifying the mapping name of the reader's orders servlet
	 */
	public static final String READER_ORDERS_SERVLET = "readerorders";
	
	/**
	 * The {@code String} specifying the mapping name of the reader details servlet
	 */
	public static final String READER_DETAILS_SERVLET_MAPPING = "readerdetails";
	
	/**
	 * The {@code String} specifying the name of the welcome page
	 */
	public static final String START_PAGE = "index.html";
	
	/**
	 * The {@code String} specifying the header name of previous page
	 */
	public static final String PREV_PAGE_HEADER_NAME = "Referer";
	
	/**
	 * The {@code String} specifying the pathname to the guest home page
	 */
	public static final String GUEST_HOME_PAGE = "/WEB-INF/jsp/guest.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the reader home page
	 */
	public static final String READER_HOME_PAGE = "/WEB-INF/jsp/reader.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the reader's orders page
	 */
	public static final String READER_ORDERS_PAGE = "/WEB-INF/jsp/reader_orders.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the reader's books page
	 */
	public static final String READER_BOOKS_PAGE = "/WEB-INF/jsp/reader_books.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the librarian home page
	 */
	public static final String LIBRARIAN_HOME_PAGE = "/WEB-INF/jsp/librarian_orders.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the admin home page
	 */
	public static final String ADMIN_HOME_PAGE = "/WEB-INF/jsp/manage_books.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the error page
	 */
	public static final String ERROR_PAGE = "/WEB-INF/jsp/error.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the order details page
	 */
	public static final String ORDER_DETAILS_PAGE = "/WEB-INF/jsp/order_details.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the page, 
	 * that displays the list of all processed orders for the librarian.
	 */
	public static final String LIBRARIAN_LIST_READERS_PAGE = "/WEB-INF/jsp/librarian_readers.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the page,
	 * that displays the list of all actual reader's orders for the librarian.
	 */
	public static final String READER_DETAILS_PAGE = "/WEB-INF/jsp/reader_details.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the book details page.
	 */
	public static final String BOOK_DETAILS_PAGE = "/WEB-INF/jsp/book_details.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the manage librarians page.
	 */
	public static final String MANAGE_LIBRARIANS_PAGE = "/WEB-INF/jsp/manage_librarians.jsp";
	
	/**
	 * The {@code String} specifying the pathname to the manage readers page.
	 */
	public static final String MANAGE_READERS_PAGE = "/WEB-INF/jsp/manage_readers.jsp";
}
