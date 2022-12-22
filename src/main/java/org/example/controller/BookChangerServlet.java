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
 * A servlet that obtains changed book's info from {@code HttpServletRequest} and save it to the catalog.
 * If currently logged user is not an admin, then the user is redirected to the welcome page.
 * If the book cannot be saved to the catalog, then the user is redirected to the previous page.
 * 
 * @author Vyacheslav Fedchenko
 * 
 */

@WebServlet("/changebook")
public class BookChangerServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = 8183669165510331971L;
	
	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(BookChangerServlet.class);
	
	/**
	 * The key for getting a localized message of successful operation from the resource bundles
	 */
	private static final String INFO_BOOK_HAS_CHANGED = "info.book.has.changed";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.ADMIN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		try {
			new BookService().changeBook(req);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_INFO_MESSAGE, 
										  Messages.getMessage(req, INFO_BOOK_HAS_CHANGED));
			resp.sendRedirect(Constants.START_PAGE);
		} catch (ServiceException | DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
					  					  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
		}
	}
}
