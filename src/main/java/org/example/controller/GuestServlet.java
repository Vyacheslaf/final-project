package org.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/guest")
public class GuestServlet extends HttpServlet{

	private static final long serialVersionUID = -1348351527631389917L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {

		req.getRequestDispatcher(Constants.GUEST_HOME_PAGE).forward(req, resp);
	}
}
