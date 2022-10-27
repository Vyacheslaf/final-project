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
import org.example.service.BookService;
import org.example.util.Messages;

@WebServlet("/reader")
public class ReaderServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(ReaderServlet.class);
	private static final String REQ_ATTR_SEARCH = "search";
	private static final String REQ_ATTR_BOOKS = "books";
	private static final String REQ_ATTR_NEXT_PAGE = "nextPage";
	private static final String REQ_ATTR_PREV_PAGE = "prevPage";
	private static final String REQ_ATTR_PAGE = "page";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user != null && !user.getRole().equals(UserRole.READER)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		String text = req.getParameter(REQ_ATTR_SEARCH);
		int page = BookService.getPageNumber(req);
		int booksCount = 0;
		List<Book> books = new ArrayList<>();
		try {
			booksCount = BookService.getBooksCount(req);
			books = BookService.findBooks(req, page);
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
					  					  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(Constants.ERROR_PAGE);
			return;
		}
		req.setAttribute(REQ_ATTR_SEARCH, text);
		req.setAttribute(REQ_ATTR_BOOKS, books);
		req.setAttribute(REQ_ATTR_NEXT_PAGE, BookService.getNextPage(page, booksCount));
		req.setAttribute(REQ_ATTR_PREV_PAGE, BookService.getPrevPage(page));
		req.setAttribute(REQ_ATTR_PAGE, page);
		if (user == null) {
			req.getRequestDispatcher(Constants.GUEST_HOME_PAGE).forward(req, resp);
		} else {
			req.getRequestDispatcher(Constants.READER_HOME_PAGE).forward(req, resp);
		}
	}
}
