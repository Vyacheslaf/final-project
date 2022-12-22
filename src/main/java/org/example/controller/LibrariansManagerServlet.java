package org.example.controller;

import java.io.IOException;
import java.util.List;

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
 * A servlet that obtains the list of all users with role <code>LIBRARIAN</code>, 
 * puts this list to the {@code HttpServletRequest} and then forwards the user to the manage librarians page.
 * If currently logged user is not an admin, then the user is redirected to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/managelibrarians")
public class LibrariansManagerServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = -2592388825593611961L;

	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(LibrariansManagerServlet.class);

	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the list of librarians to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_LIBRARIANS = "librarians";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.ADMIN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		try {
			List<User> librarians = new UserService().findUsersByRole(UserRole.LIBRARIAN);
			req.setAttribute(REQ_ATTR_LIBRARIANS, librarians);
		} catch (DaoException | ServiceException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
		}
		req.getRequestDispatcher(Constants.MANAGE_LIBRARIANS_PAGE).forward(req, resp);
	}

}
