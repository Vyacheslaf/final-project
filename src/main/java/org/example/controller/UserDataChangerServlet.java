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
 * A servlet that changes the reader's data and redirects the reader to the previous page.
 * If currently logged user is not a reader, then the user is redirected to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/changedata")
public class UserDataChangerServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = -3823577808677310017L;
	
	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(UserDataChangerServlet.class);

	/**
	 * The key for getting a localized message of successful operation from the resource bundles
	 */
	private static final String INFO_USER_DATA_HAS_CHANGED = "info.user.data.has.changed";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.READER)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		try {
			user = new UserService().updateUserData(req);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_USER, user);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_INFO_MESSAGE, 
										  Messages.getMessage(req, INFO_USER_DATA_HAS_CHANGED));
		} catch (DaoException | ServiceException e) {
			LOG.error(e.getMessage());
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
					  					  Messages.getMessage(req, e.getMessage()));
		}
		resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
	}
}
