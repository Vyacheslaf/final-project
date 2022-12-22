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

/**
 * A servlet that obtains the list of all orders of the reader,
 * puts this list to the {@code HttpServletRequest}
 * and forwards the reader to the reader's orders page.
 * If currently logged user is not a reader, then the user is redirected to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/readerorders")
public class ReaderOrdersListServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = -2641046429670637442L;
	
	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(ReaderOrdersListServlet.class);

	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the list of orders to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_ORDERS = "orders";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User)req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.READER)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		List<Order> orders;
		try {
			orders = new OrderService().getReaderOrders(user);
			req.setAttribute(REQ_ATTR_ORDERS, orders);
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
			return;
		}
		req.getRequestDispatcher(Constants.READER_ORDERS_PAGE).forward(req, resp);
	}
}
