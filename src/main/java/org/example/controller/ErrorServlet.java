package org.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/error")
public class ErrorServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {
		String errorMessage = (String)req.getSession()
							.getAttribute(Constants.SESSION_ATTRIBUTE_ERROR);
		req.getSession().removeAttribute(Constants.SESSION_ATTRIBUTE_ERROR);
		req.getRequestDispatcher(Constants.ERROR_PAGE);
	}
}
