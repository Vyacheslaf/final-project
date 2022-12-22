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
import org.example.exception.ServiceException;
import org.example.service.OrderService;
import org.example.util.Messages;

/**
 * A servlet that obtains the list of actual orders for the selected reader,
 * puts this list to the {@code HttpServletRequest}
 * and forwards the librarian to the reader details page.
 * If currently logged user is not a librarian, then the user is redirected to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/readerdetails")
public class ReaderOrdersManagerServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = 4705451728876354962L;
	
	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(ReaderOrdersManagerServlet.class);

	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the list of orders to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_ORDERS = "orders";

	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the reader's ID to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_READER_ID = "readerid";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User)req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.LIBRARIAN)) {
			req.getRequestDispatcher(Constants.START_PAGE).forward(req, resp);
			return;
		}
		try {
			List<Order> orders = new OrderService().getReaderActualOrders(req);
			req.setAttribute(REQ_ATTR_ORDERS, orders);
			req.setAttribute(REQ_ATTR_READER_ID, req.getParameter(REQ_ATTR_READER_ID));
		} catch (DaoException | ServiceException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(Constants.LIBRARIAN_SERVLET_MAPPING);
			return;
		}
		req.getRequestDispatcher(Constants.READER_DETAILS_PAGE).forward(req, resp);
	}
}
