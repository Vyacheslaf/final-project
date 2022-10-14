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
import org.example.exception.DaoException;
import org.example.service.UserService;

@WebServlet("/changedata")
public class UserDataServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(UserDataServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		try {
			User user = UserService.updateUserData(req);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_USER, user);
			resp.sendRedirect(req.getHeader("Referer"));
		} catch (DaoException e) {
			LOG.error(e.getMessage());
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR, e.getMessage());
			resp.sendRedirect(Constants.ERROR_SERVLET_MAPPING);
		}
	}
}
