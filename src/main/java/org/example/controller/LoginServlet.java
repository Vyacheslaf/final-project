package org.example.controller;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.User;
import org.example.exception.DaoException;
import org.example.exception.ServiceException;
import org.example.service.UserService;
import org.example.util.Messages;

/**
 * A servlet that obtains the {@code User} by login and password,
 * puts this {@code User} to the {@code HttpSession} and redirects the user to the welcome page.
 * If the {@ User} with such login or password is not found 
 * then the error message will be put to the {@code HttpSession}
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = 4832141539771106639L;
	
	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(LoginServlet.class);

	/**
	 * The key for getting a localized error message from the resource bundles if the user is not found
	 */
	private static final String ERROR_USER_NOT_FOUND = "error.user.not.found";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		try {
			User user = new UserService().login(req);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_USER,	user);
			resp.sendRedirect(req.getRequestURI());
		} catch (DaoException | ServiceException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (Objects.isNull(user)) {
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
					  					  Messages.getMessage(req, ERROR_USER_NOT_FOUND));
		}
		req.getRequestDispatcher(Constants.START_PAGE).forward(req, resp);
	}
}
