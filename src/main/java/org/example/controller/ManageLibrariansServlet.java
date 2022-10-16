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
import org.example.service.UserService;

@WebServlet("/managelibrarians")
public class ManageLibrariansServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(ManageLibrariansServlet.class);
	private static final String REQ_ATTR_LIBRARIANS = "librarians";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user == null || !user.getRole().equals(UserRole.ADMIN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		try {
			List<User> librarians = UserService.findUsersByRole(UserRole.LIBRARIAN);
			req.setAttribute(REQ_ATTR_LIBRARIANS, librarians);
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, e.getMessage());
		}
		req.getRequestDispatcher(Constants.MANAGE_LIBRARIANS_PAGE).forward(req, resp);
	}

}