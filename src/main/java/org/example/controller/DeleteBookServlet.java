package org.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;
import org.example.exception.ServiceException;
import org.example.service.BookService;
import org.example.util.Messages;

@WebServlet("/deletebook")
public class DeleteBookServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(AddBookServlet.class);
	private static final String INFO_BOOK_WAS_DELETED = "info.book.was.deleted";
	private static final String REQ_PARAM_BOOK_ID = "bookid";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user == null || !user.getRole().equals(UserRole.ADMIN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		String bookId = req.getParameter(REQ_PARAM_BOOK_ID);
		try {
			BookService.deleteBook(bookId);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_INFO_MESSAGE, 
										  Messages.getMessage(req, INFO_BOOK_WAS_DELETED));
			resp.sendRedirect(Constants.START_PAGE);
		} catch (ServiceException | DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
		}
	}
}
