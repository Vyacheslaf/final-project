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

@WebServlet("/findreader")
public class FindReaderServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(FindReaderServlet.class);
	private static final String REQ_ATTR_PHONE_NUMBER = "phonenumber";
	private static final Object ERROR_MESSAGE = "Reader not found";
	private static final String REQ_ATTR_READERS = "readers";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User user = (User)req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user == null) {
			req.getRequestDispatcher(Constants.START_PAGE).forward(req, resp);
			return;
		}
		String phoneNumber = req.getParameter(REQ_ATTR_PHONE_NUMBER);
		try {
			User reader = UserService.findUserByPhone(phoneNumber);
			if (reader == null) {
				req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, ERROR_MESSAGE);
				resp.sendRedirect(req.getHeader("Referer"));
				return;
			}
			if (user.getRole().equals(UserRole.LIBRARIAN)) {
				resp.sendRedirect(Constants.READER_DETAILS_SERVLET_MAPPING + "?readerid=" + reader.getId());
				return;
			}
			List<User> readers = new ArrayList<>();
			readers.add(reader);
			req.setAttribute(REQ_ATTR_READERS, readers);
			req.getRequestDispatcher(Constants.MANAGE_READERS_PAGE).forward(req, resp);
		} catch (DaoException e) {
			LOG.error(e.getMessage());
			req.getRequestDispatcher(Constants.ERROR_SERVLET_MAPPING).forward(req, resp);
			return;
		}
	}
}
