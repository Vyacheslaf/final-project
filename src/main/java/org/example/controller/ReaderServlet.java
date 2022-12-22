package org.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Book;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;
import org.example.exception.ServiceException;
import org.example.service.BookService;
import org.example.util.Messages;

/**
 * A servlet that obtains the list of books corresponding to the search phrase,
 * sort options and page number, puts this list to the {@code HttpServletRequest}
 * and forwards the user to the reader home page if the user's role is <code>READER</code>
 * or to the guest home page if there is no logged user.
 * If currently logged user is a librarian or an admin, then the user is redirected to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/reader")
public class ReaderServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = -7896525205412318811L;
	
	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(ReaderServlet.class);
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the search phrase to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_SEARCH = "search";
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the list of books to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_BOOKS = "books";
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the next page's number to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_NEXT_PAGE = "nextPage";
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the previous page's number to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_PREV_PAGE = "prevPage";
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the current page's number to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_PAGE = "page";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user != null) &&!user.getRole().equals(UserRole.READER)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		String text = req.getParameter(REQ_ATTR_SEARCH);
		int page = BookService.getPageNumber(req);
		int booksCount = 0;
		List<Book> books = new ArrayList<>();
		try {
			BookService bookService = new BookService();
			booksCount = bookService.getBooksCount(req);
			books = bookService.findBooks(req);
		} catch (DaoException | ServiceException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
					  					  Messages.getMessage(req, e.getMessage()));
			req.getRequestDispatcher(Constants.ERROR_PAGE).forward(req, resp);
			return;
		}
		req.setAttribute(REQ_ATTR_SEARCH, text);
		req.setAttribute(REQ_ATTR_BOOKS, books);
		req.setAttribute(REQ_ATTR_NEXT_PAGE, BookService.getNextPage(page, booksCount));
		req.setAttribute(REQ_ATTR_PREV_PAGE, BookService.getPrevPage(page));
		req.setAttribute(REQ_ATTR_PAGE, page);
		if (user == null) {
			req.getRequestDispatcher(Constants.GUEST_HOME_PAGE).forward(req, resp);
			return;
		}
		req.getRequestDispatcher(Constants.READER_HOME_PAGE).forward(req, resp);
	}
}
