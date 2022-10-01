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
import org.example.Config;
import org.example.dao.DaoFactory;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.exception.DaoException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(LoginServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		UserDao userDao = daoFactory.getUserDao();
		try {
			User user = userDao.findByLoginAndPassword(email, password);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_USER, 
											user);
			resp.sendRedirect(req.getRequestURI());
		} catch (DaoException e) {
			String message = "Cannot get access to DAO";
			LOGGER.error(message);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR, 
																	message);
			resp.sendRedirect(Constants.ERROR_SERVLET_MAPPING);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {

		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (Objects.nonNull(user)) {
			req.getRequestDispatcher(Constants.START_PAGE).forward(req, resp);
			return;
		}
		req.getRequestDispatcher(Constants.LOGIN_PAGE).forward(req, resp);
	}
}
