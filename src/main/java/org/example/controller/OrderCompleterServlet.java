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
 * A servlet that completes the order and redirects the librarian to the reader details servlet.
 * If currently logged user is not a librarian, then the user is redirected to the welcome page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/completeorder")
public class OrderCompleterServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = -3019181649257652300L;
	
	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(OrderCompleterServlet.class);

	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the reader's ID from the completed order to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_READER_ID = "readerid";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) ||!user.getRole().equals(UserRole.LIBRARIAN)) {
			resp.sendRedirect(Constants.START_PAGE);
			return;
		}
		String readerId = req.getParameter(REQ_ATTR_READER_ID);
		try {
			new OrderService().completeOrder(req);
			resp.sendRedirect(Constants.READER_DETAILS_SERVLET_MAPPING + "?" + REQ_ATTR_READER_ID + "=" + readerId);
		} catch (DaoException | ServiceException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
										  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
		}
	}

}
