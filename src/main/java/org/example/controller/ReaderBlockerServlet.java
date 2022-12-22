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
 * A servlet that blocks/unblocks the reader.
 * If currently logged user is not an admin, then the user is redirected to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/blockreader")
public class ReaderBlockerServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = 2558865726284076523L;

	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(ReaderBlockerServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.ADMIN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		try {
			new UserService().blockUser(req);
		} catch (DaoException | ServiceException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
		}
		resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
	}
}
