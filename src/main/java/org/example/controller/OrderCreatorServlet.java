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
import org.example.exception.ServiceException;
import org.example.service.OrderService;
import org.example.util.Messages;

/**
 * A servlet that creates a new order and redirects the reader after successful order creation 
 * to the reader orders servlet.
 * If an order cannot be created, then the reader redirects to the previous page 
 * with a corresponding info message.
 * If currently logged user is not a reader, then the user is redirected to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/neworder")
public class OrderCreatorServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = -5792514670640647017L;

	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(OrderCreatorServlet.class);

	/**
	 * The key for getting a localized message from the resource bundles if the order cannot be created
	 */
	private static final String INFO_BOOK_ALREADY_IN_ORDERS = "info.book.already.in.orders";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.READER)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		boolean isCreated = false;
		try {
			isCreated = new OrderService().createOrder(req);
		} catch (DaoException | ServiceException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
			return;
		}
		if (isCreated) {
			resp.sendRedirect(req.getRequestURI());
			return;
		}
		req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_INFO_MESSAGE, 
									  Messages.getMessage(req, INFO_BOOK_ALREADY_IN_ORDERS));
		resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(Constants.READER_ORDERS_SERVLET).forward(req, resp);
	}
}
