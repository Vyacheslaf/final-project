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
import org.example.util.Messages;

@WebServlet("/completeorder")
public class CompleteOrderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(CompleteOrderServlet.class);
	private static final String REQ_ATTR_ORDER_ID = "orderid";
	private static final String REQ_ATTR_READER_ID = "readerid";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user == null || !user.getRole().equals(UserRole.LIBRARIAN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		String orderId = req.getParameter(REQ_ATTR_ORDER_ID);
		String readerId = req.getParameter(REQ_ATTR_READER_ID);
		try {
			OrderService.completeOrder(orderId);
			resp.sendRedirect(Constants.READER_DETAILS_SERVLET_MAPPING + "?" + REQ_ATTR_READER_ID + "=" +readerId);
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
		}
	}

}
