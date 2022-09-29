package org.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet{

	private static final long serialVersionUID = 4157755494235871684L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {
		
		req.getSession().removeAttribute(Constants.SESSION_ATTRIBUTE_USER);
		req.getRequestDispatcher(Constants.START_PAGE).forward(req, resp);
	}
}
