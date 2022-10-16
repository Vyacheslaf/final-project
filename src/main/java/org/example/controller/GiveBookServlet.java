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
import org.example.service.OrderService;

@WebServlet("/givebook")
public class GiveBookServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(GiveBookServlet.class);
	private static final String REQ_ATTR_ORDER_ID = "orderid";
	private static final String REQ_ATTR_ON_SUBSCRIPTION = "onsubscription";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user == null) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		String orderId = req.getParameter(REQ_ATTR_ORDER_ID);
		boolean onSubscription = Boolean.parseBoolean(req.getParameter(REQ_ATTR_ON_SUBSCRIPTION));
		try {
			OrderService.giveOrder(orderId, onSubscription);
			resp.sendRedirect(Constants.LIBRARIAN_SERVLET_MAPPING);
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, e.getMessage());
			resp.sendRedirect(req.getHeader("Referer"));
		}
	}

}