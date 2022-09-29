package org.example.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reader")
public class ReaderServlet extends HttpServlet{

	private static final long serialVersionUID = -6323568463534306645L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {

		req.getRequestDispatcher(Constants.READER_HOME_PAGE).forward(req, resp);
	}
}
