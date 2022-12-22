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
 * A servlet that obtains the list of all processed orders of currently logged reader
 * and forwards the reader to the reader's books page.
 * If currently logged user is not a reader, then the user is redirected to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/readerbooks")
public class ReaderBooksListServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = -5447484776865622749L;

	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(ReaderBooksListServlet.class);

	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the orders' list to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_ORDERS = "orders";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User)req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.READER)) {
			req.getRequestDispatcher(Constants.START_PAGE).forward(req, resp);
			return;
		}
		List<Order> orders;
		try {
			orders = new OrderService().getReaderProcessedOrders(user);
			req.setAttribute(REQ_ATTR_ORDERS, orders);
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
			return;
		}
		req.getRequestDispatcher(Constants.READER_BOOKS_PAGE).forward(req, resp);
	}
}
