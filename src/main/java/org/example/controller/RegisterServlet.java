package org.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Config;
import org.example.dao.DaoFactory;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(GuestServlet.class);
	private static final String REQ_PARAM_EMAIL = "email";
	private static final String REQ_PARAM_PASSWORD = "password";
	private static final String REQ_PARAM_FIRST_NAME = "firstname";
	private static final String REQ_PARAM_LAST_NAME = "lastname";
	private static final String REQ_PARAM_PHONE_NUMBER = "phone";
	private static final String REQ_PARAM_PASSPORT_NUMBER = "passport";
	private static final String REQ_PARAM_ROLE = "role";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {
		User user = new User();
		user.setEmail(req.getParameter(REQ_PARAM_EMAIL));
		user.setPassword(req.getParameter(REQ_PARAM_PASSWORD));
		user.setFirstName(req.getParameter(REQ_PARAM_FIRST_NAME));
		user.setLastName(req.getParameter(REQ_PARAM_LAST_NAME));
		user.setPhoneNumber(req.getParameter(REQ_PARAM_PHONE_NUMBER));
		user.setPassportNumber(req.getParameter(REQ_PARAM_PASSPORT_NUMBER));
		user.setRole(UserRole.valueOf(req.getParameter(REQ_PARAM_ROLE).toUpperCase()));
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		UserDao userDao = daoFactory.getUserDao();
		try {
			user = userDao.create(user);
			if (user.getRole().equals(UserRole.READER)) {
				req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_USER, 
												user);
				resp.sendRedirect("login");
			} else {
				
			}
		} catch (DaoException e) {
			String message = "Cannot create user";
			LOG.error(message);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR, 
																	message);
			resp.sendRedirect(Constants.ERROR_SERVLET_MAPPING);
		}
	}

}
