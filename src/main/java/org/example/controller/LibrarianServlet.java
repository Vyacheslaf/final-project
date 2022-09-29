package org.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/librarian")
public class LibrarianServlet extends HttpServlet{

	private static final long serialVersionUID = 1069005210155866971L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {
		
		req.getRequestDispatcher(Constants.LIBRARIAN_HOME_PAGE)
															.forward(req, resp);
	}
}
