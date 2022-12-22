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
import org.example.service.UserService;
import org.example.util.Messages;

/**
 * A servlet that creates a new {@code User},
 * puts this {@code User} to the {@code HttpSession} and redirects the user to the login servlet 
 * if the newly created user's role is <code>READER</code>
 * or redirects the admin to the previous page if the newly created user's role is <code>LIBRARIAN</code>.
 * If currently logged user is a reader or a librarian, then the user is redirected to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/register")
public class UserCreatorServlet extends HttpServlet {
	
	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = -4667869936708974544L;
	
	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(UserCreatorServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user != null) &&!user.getRole().equals(UserRole.ADMIN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		try {
			user = new UserService().register(req);
			if (user.getRole().equals(UserRole.READER)) {
				req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_USER, user);
				resp.sendRedirect(Constants.LOGIN_SERVLET_MAPPING);
			} else {
				resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
			}
		} catch (DaoException | ServiceException e) {
			LOG.error(e.getMessage());
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
		}
	}
}
