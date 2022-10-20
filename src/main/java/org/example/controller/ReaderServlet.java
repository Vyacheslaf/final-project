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

@WebServlet("/reader")
public class ReaderServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(ReaderServlet.class);
	private static final String ERROR_MESSAGE = "Cannot find books";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user != null && !user.getRole().equals(UserRole.READER)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		String text = req.getParameter("search");
		int page = BookService.getPageNumber(req);
		int booksCount = 0;
		List<Book> books = new ArrayList<>();
		try {
			booksCount = BookService.getBooksCount(req);
			books = BookService.findBooks(req, page);
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, ERROR_MESSAGE);
//			resp.sendRedirect(req.getHeader("Referer"));
			resp.sendRedirect(Constants.ERROR_PAGE);
			return;
		}
		req.setAttribute("search", text);
		req.setAttribute("books", books);
		req.setAttribute("nextPage", BookService.getNextPage(page, booksCount));
		req.setAttribute("prevPage", BookService.getPrevPage(page));
		req.setAttribute("page", page);
		if (user == null) {
			req.getRequestDispatcher(Constants.GUEST_HOME_PAGE).forward(req, resp);
		} else {
			req.getRequestDispatcher(Constants.READER_HOME_PAGE).forward(req, resp);
		}
	}
}
