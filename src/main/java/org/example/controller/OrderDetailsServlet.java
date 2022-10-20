package org.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;
import org.example.service.OrderService;

@WebServlet("/orderdetails")
public class OrderDetailsServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(NewOrderServlet.class);
	private static final String ERROR_MESSAGE = "Cannot find an order";
	private static final String REQ_ATTR_ORDER = "order";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user == null || !user.getRole().equals(UserRole.LIBRARIAN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		int orderId = Integer.parseInt(req.getParameter("orderid"));
		try {
			Order order = OrderService.findOrder(orderId);
			req.setAttribute(REQ_ATTR_ORDER, order);
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, ERROR_MESSAGE);
			resp.sendRedirect(req.getHeader("Referer"));
			return;
		}
		req.getRequestDispatcher(Constants.ORDER_DETAILS_PAGE).forward(req, resp);
	}
}
