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
import org.example.service.UserService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(LoginServlet.class);
	private static final Object ERROR_MESSAGE = "Cannot find a user";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		try {
			User user = UserService.login(req);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_USER,	user);
			resp.sendRedirect(req.getRequestURI());
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, ERROR_MESSAGE);
			resp.sendRedirect(req.getHeader("Referer"));
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (Objects.nonNull(user)) {
			req.getRequestDispatcher(Constants.START_PAGE).forward(req, resp);
			return;
		}
		req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR, ERROR_MESSAGE);
		req.getRequestDispatcher(Constants.LOGIN_PAGE).forward(req, resp);
	}
}
