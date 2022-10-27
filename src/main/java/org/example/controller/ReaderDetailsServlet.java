package org.example.controller;

import java.io.IOException;
import java.util.List;

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
import org.example.util.Messages;

@WebServlet("/readerdetails")
public class ReaderDetailsServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(ReaderDetailsServlet.class);
	private static final String REQ_ATTR_ORDERS = "orders";
	private static final String REQ_ATTR_READER_ID = "readerid";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User)req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user == null || !user.getRole().equals(UserRole.LIBRARIAN)) {
			req.getRequestDispatcher(Constants.START_PAGE).forward(req, resp);
			return;
		}
		List<Order> orders;
		user = new User();
		user.setId(Integer.parseInt(req.getParameter(REQ_ATTR_READER_ID)));
		try {
			orders = OrderService.getReaderActualOrders(user);
			req.setAttribute(REQ_ATTR_ORDERS, orders);
			req.setAttribute(REQ_ATTR_READER_ID, req.getParameter(REQ_ATTR_READER_ID));
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
			return;
		}
		req.getRequestDispatcher(Constants.READER_DETAILS_PAGE).forward(req, resp);
	}
}
