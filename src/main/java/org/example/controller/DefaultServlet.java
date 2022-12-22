package org.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.entity.User;

/**
 * A start servlet that redirects the user to the start page corresponding to user's role.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/start")
public class DefaultServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = -89857105068664164L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		req.getRequestDispatcher(getServletPath(user)).forward(req, resp);
	}
	
	/**
	 * Returns the pathname to the resource corresponding to user's role
	 * 
	 * @param user
	 * 		  A {@code User}
	 * @return the pathname to the resource corresponding to user's role
	 */
	private static String getServletPath(User user) {
		if (user == null) {
			return Constants.READER_SERVLET_MAPPING;
		}
		String path;
		switch (user.getRole()) {
		case READER:
			path = Constants.READER_SERVLET_MAPPING;
			break;
		case LIBRARIAN:
			path = Constants.LIBRARIAN_SERVLET_MAPPING;
			break;
		default:
			path = Constants.ADMIN_SERVLET_MAPPING;
			break;
		}
		return path;
	}
}
