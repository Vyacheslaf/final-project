package org.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.DaoException;
import org.example.service.OrderService;

@WebServlet("/cancelorder")
public class CancelOrderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(CancelOrderServlet.class);
	private static final String CANCEL_ORDER_ERROR_MESSAGE = "Cannot cancel an order";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {
		String orderId = req.getParameter("orderid");
		try {
			if (!OrderService.cancelOrder(orderId)) {
				throw new DaoException(CANCEL_ORDER_ERROR_MESSAGE, null);
			}
			resp.sendRedirect(req.getHeader("Referer"));
		} catch (DaoException e) {
			LOG.error(CANCEL_ORDER_ERROR_MESSAGE);
			resp.sendRedirect(Constants.ERROR_SERVLET_MAPPING);
//			req.getRequestDispatcher(Constants.ERROR_SERVLET_MAPPING).forward(req, resp);
		}
	}
}
