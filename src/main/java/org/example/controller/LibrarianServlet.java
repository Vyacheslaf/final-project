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
 * A librarian start servlet that obtains the list of new orders, 
 * puts this list to the {@code HttpServletRequest} and then forwards the user to the librarian home page.
 * If currently logged user is not a librarian, then the user is redirected to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/librarian")
public class LibrarianServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = 2432945568771491638L;

	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(LibrarianServlet.class);

	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the list of orders to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_ORDERS = "orders";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.LIBRARIAN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		List<Order> orders;
		try {
			orders = new OrderService().getNewOrders();
			req.setAttribute(REQ_ATTR_ORDERS, orders);
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
		}
		req.getRequestDispatcher(Constants.LIBRARIAN_HOME_PAGE).forward(req, resp);
	}
}
