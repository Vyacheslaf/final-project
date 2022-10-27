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
import org.example.service.UserService;
import org.example.util.Messages;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(RegisterServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user != null && !user.getRole().equals(UserRole.ADMIN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		try {
			user = UserService.register(req);
			if (user.getRole().equals(UserRole.READER)) {
				req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_USER, user);
				resp.sendRedirect(Constants.LOGIN_SERVLET_MAPPING);
			} else {
				resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
			}
		} catch (DaoException e) {
			LOG.error(e.getMessage());
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
		}
	}
}
