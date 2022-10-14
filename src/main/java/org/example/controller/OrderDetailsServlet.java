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
import org.example.exception.DaoException;
import org.example.service.OrderService;

@WebServlet("/orderdetails")
public class OrderDetailsServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(NewOrderServlet.class);
	private static final String ERROR_MESSAGE = "Cannot find an order";
	private static final String REQ_ATTR_ORDER = "order";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {
		int orderId = Integer.parseInt(req.getParameter("orderid"));
		try {
			Order order = OrderService.findOrder(orderId);
			req.setAttribute(REQ_ATTR_ORDER, order);
		} catch (DaoException e) {
			LOG.error(ERROR_MESSAGE);
			req.getSession().setAttribute("errormessage", ERROR_MESSAGE);
			req.getRequestDispatcher(Constants.ERROR_SERVLET_MAPPING).forward(req, resp);
		}
		req.getRequestDispatcher(Constants.ORDER_DETAILS_PAGE).forward(req, resp);
	}
}
