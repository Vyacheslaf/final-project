package org.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.entity.User;

@WebServlet("/start")
public class StartServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {
		
		User user = (User) req.getSession()
							  .getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		req.getRequestDispatcher(getServletPath(user)).forward(req, resp);
	}
	
	private static String getServletPath(User user) {
		if (user == null) {
			return Constants.GUEST_SERVLET_MAPPING;
		}
		String path;
		switch (user.getRole()) {
		case READER:
			path = Constants.READER_SERVLET_MAPPING;
			break;
		case LIBRARIAN:
			path = Constants.LIBRARIAN_SERVLET_MAPPING;
			break;
		default:
			path = Constants.ADMIN_SERVLET_MAPPING;
			break;
		}
		return path;
	}
}
