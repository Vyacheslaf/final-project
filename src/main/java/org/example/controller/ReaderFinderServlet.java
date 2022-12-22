package org.example.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.example.service.UserService;
import org.example.util.Messages;

/**
 * A servlet that finds a reader by phone number 
 * if the logged user's role is <code>ADMIN</code> or <code>LIBRARIAN</code>.
 * If there is no logged user or the logged user's role is <code>READER</code>,
 * then the user redirects to the welcome page.
 * If the logged user's role is <code>LIBRARIAN</code> and the reader found,
 * then the librarian redirects to the reader's details servlet.
 * If the logged user's role is <code>ADMIN</code> and the reader found,
 * then the reader is added to the empty list, that puts to the {@code HttpServletRequest}
 * and the admin forwards to the manage readers page.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

@WebServlet("/findreader")
public class ReaderFinderServlet extends HttpServlet {

	/**
	 * A unique serial version identifier
	 * @see java.io.Serializable#serialVersionUID
	 */
	private static final long serialVersionUID = 4987013653510425615L;

	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(ReaderFinderServlet.class);
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the reader's phone number to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_PHONE_NUMBER = "phonenumber";
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the list of readers to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_READERS = "readers";
	
	/**
	 * The {@code String} specifying the name of the attribute 
	 * to store the found reader's ID to the <code>HttpServletRequest</code>
	 */
	private static final String REQ_ATTR_READER_ID = "readerid";
	
	/**
	 * The key for getting a localized error message from the resource bundles if the reader not found
	 */
	private static final String ERROR_READER_NOT_FOUND = "error.reader.not.found";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User)req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if ((user == null) || user.getRole().equals(UserRole.READER)) {
			req.getRequestDispatcher(Constants.START_PAGE).forward(req, resp);
			return;
		}
		String phoneNumber = req.getParameter(REQ_ATTR_PHONE_NUMBER);
		req.setAttribute(REQ_ATTR_PHONE_NUMBER, phoneNumber);
		try {
			User reader = new UserService().findUserByPhone(phoneNumber);
			if (reader == null) {
				req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
											  Messages.getMessage(req, ERROR_READER_NOT_FOUND));
				resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
				return;
			}
			if (user.getRole().equals(UserRole.LIBRARIAN)) {
				String reqParameter = "?" + REQ_ATTR_READER_ID + "=" + reader.getId();
				resp.sendRedirect(Constants.READER_DETAILS_SERVLET_MAPPING + reqParameter);
				return;
			}
			List<User> readers = new ArrayList<>();
			readers.add(reader);
			req.setAttribute(REQ_ATTR_READERS, readers);
			req.getRequestDispatcher(Constants.MANAGE_READERS_PAGE).forward(req, resp);
		} catch (DaoException | ServiceException e) {
			LOG.error(e.getMessage());
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
					  					  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
		}
	}
}
