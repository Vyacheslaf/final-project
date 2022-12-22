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

/**
 * A servlet that removes the {@code Book} by ID from the catalog.
 * If currently logged user is not an admin, then the user is redirected to the welcome page.
 * If the book is removed, then the admin is redirected to the admin's start page,
 * otherwise the admin is redirected to the previous page with an error message.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/deletebook")
public class BookRemoverServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = -7658580627542637964L;

	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(BookRemoverServlet.class);

	/**
	 * The key for getting a localized message of successful operation from the resource bundles
	 */
	private static final String INFO_BOOK_HAS_DELETED = "info.book.has.deleted";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.ADMIN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		try {
			new BookService().deleteBook(req);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_INFO_MESSAGE, 
										  Messages.getMessage(req, INFO_BOOK_HAS_DELETED));
			resp.sendRedirect(Constants.START_PAGE);
		} catch (ServiceException | DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
		}
	}
}
