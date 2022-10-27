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
import org.example.service.UserService;
import org.example.util.Messages;

@WebServlet("/findreader")
public class FindReaderServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(FindReaderServlet.class);
	private static final String REQ_ATTR_PHONE_NUMBER = "phonenumber";
	private static final String ERROR_READER_NOT_FOUND = "error.reader.not.found";
	private static final String REQ_ATTR_READERS = "readers";
	private static final String REQ_ATTR_READER_ID = "readerid";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User)req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user == null || user.getRole().equals(UserRole.READER)) {
			req.getRequestDispatcher(Constants.START_PAGE).forward(req, resp);
			return;
		}
		String phoneNumber = req.getParameter(REQ_ATTR_PHONE_NUMBER);
		req.setAttribute(REQ_ATTR_PHONE_NUMBER, req.getParameter(REQ_ATTR_PHONE_NUMBER));
		try {
			User reader = UserService.findUserByPhone(phoneNumber);
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
		} catch (DaoException e) {
			LOG.error(e.getMessage());
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, 
					  					  Messages.getMessage(req, e.getMessage()));
			resp.sendRedirect(req.getHeader(Constants.PREV_PAGE_HEADER_NAME));
		}
	}
}
