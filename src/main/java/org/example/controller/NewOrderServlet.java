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
import org.example.service.OrderService;

@WebServlet("/neworder")
public class NewOrderServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(NewOrderServlet.class);
	private static final String INFO_MESSAGE = "The book is already in orders";
	private static final String REQ_PARAM_BOOK_ID = "bookid";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user == null || !user.getRole().equals(UserRole.READER)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		String bookId = req.getParameter(REQ_PARAM_BOOK_ID);
		boolean isCreated = false;
		try {
			isCreated = OrderService.createOrder(bookId, user);
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, e.getMessage());
			resp.sendRedirect(req.getHeader("Referer"));
			return;
		}
		if (isCreated) {
			resp.sendRedirect(req.getRequestURI());
			return;
		}
		req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_INFO_MESSAGE, INFO_MESSAGE);
		resp.sendRedirect(req.getHeader("Referer"));
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(Constants.READER_ORDERS_SERVLET).forward(req, resp);
	}
}
