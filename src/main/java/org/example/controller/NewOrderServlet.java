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
import org.example.exception.DaoException;
import org.example.service.OrderService;

@WebServlet("/neworder")
public class NewOrderServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(NewOrderServlet.class);
	private static final String CREATE_ORDER_ERROR_MESSAGE 
													= "Cannot create an order";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {
//		System.out.println(req.getHeader("Referer"));
		String orderId = req.getParameter("orderid");
		if (orderId == null) {
			String bookId = req.getParameter("bookid");
			User user = (User)req.getSession()
								.getAttribute(Constants.SESSION_ATTRIBUTE_USER);
			boolean isCreated = false;
			try {
				isCreated = OrderService.createOrder(bookId, user);
			} catch (DaoException e) {
				LOG.error(CREATE_ORDER_ERROR_MESSAGE);
				req.getRequestDispatcher(Constants.ERROR_SERVLET_MAPPING)
													.forward(req, resp);
			}
			if (isCreated) {
				resp.sendRedirect(req.getRequestURI());
			} else {
				resp.sendRedirect(req.getHeader("Referer"));
			}
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(Constants.READER_ORDERS_SERVLET).forward(req, resp);
	}
}
