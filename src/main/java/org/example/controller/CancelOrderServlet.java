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

@WebServlet("/cancelorder")
public class CancelOrderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(CancelOrderServlet.class);
	private static final String CANCEL_ORDER_ERROR_MESSAGE = "Cannot cancel an order";
	private static final String REQ_ATTR_ORDER_ID = "orderid";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user == null || user.getRole().equals(UserRole.ADMIN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		String orderId = req.getParameter(REQ_ATTR_ORDER_ID);
		try {
			if (!OrderService.cancelOrder(orderId)) {
				throw new DaoException(CANCEL_ORDER_ERROR_MESSAGE, null);
			}
			if (user.getRole().equals(UserRole.READER)) {
				resp.sendRedirect(req.getHeader("Referer"));
				return;
			}
			resp.sendRedirect(Constants.LIBRARIAN_SERVLET_MAPPING);
		} catch (DaoException e) {
			LOG.error(CANCEL_ORDER_ERROR_MESSAGE);
			resp.sendRedirect(req.getHeader("Referer"));
//			req.getRequestDispatcher(Constants.ERROR_SERVLET_MAPPING).forward(req, resp);
		}
	}
}
