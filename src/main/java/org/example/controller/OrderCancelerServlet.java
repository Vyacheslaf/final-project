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
 * A servlet that tries to cancel the new order 
 * if the logged user's role is <code>READER</code> or <code>LIBRARIAN</code>.
 * If the logged user's role is <code>LIBRARIAN</code>, 
 * then the user redirects to the reader details servlet after successful order cancellation.
 * If the order cannot be canceled, then the error message will be put to the {@code HttpSession}.
 * If there is no logged user or the logged user's role is <code>ADMIN</code>,
 * then the user redirects to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/cancelorder")
public class OrderCancelerServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = 7512231316997526613L;
	
	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(OrderCancelerServlet.class);
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the reader's ID from the canceled order to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_READER_ID = "readerid";
	
	/**
	 * The key for getting a localized error message from the resource bundles if the order cannot be canceled
	 */
	private static final String ERROR_CANNOT_CANCEL_ORDER = "error.cannot.cancel.order";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) || user.getRole().equals(UserRole.ADMIN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		try {
			OrderService orderService = new OrderService();
			if (!orderService.cancelOrder(req)) {
				throw new DaoException(ERROR_CANNOT_CANCEL_ORDER, null);
			}
			if (user.getRole().equals(UserRole.LIBRARIAN)) {
				String readerId = req.getParameter(REQ_ATTR_READER_ID);
				resp.sendRedirect(Constants.READER_DETAILS_SERVLET_MAPPING + "?" + REQ_ATTR_READER_ID + "=" + readerId);
				return;
			}
		} catch (DaoException | ServiceException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
					  					  Messages.getMessage(req, e.getMessage()));
		}
		resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
	}
}
