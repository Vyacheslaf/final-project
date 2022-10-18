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
import org.example.entity.Book;
import org.example.exception.DaoException;
import org.example.service.BookService;

@WebServlet("/guestt")
public class GuestServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(GuestServlet.class);
	private static final String FIND_BOOK_ERROR_MESSAGE = "Cannot find books";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String text = req.getParameter("text");
		int page = BookService.getPageNumber(req);
		int booksCount = 0;
		List<Book> books = new ArrayList<>();
		try {
			booksCount = BookService.getBooksCount(req);
			books = BookService.findBooks(req, page);
		} catch (DaoException e) {
			LOG.error(e);
			req.getSession().setAttribute(Constants.SESSION_ATTRIBUTE_ERROR_MESSAGE, FIND_BOOK_ERROR_MESSAGE);
			resp.sendRedirect(req.getHeader("Referer"));
			return;
		}
		req.setAttribute("text", text);
		req.setAttribute("books", books);
		req.setAttribute("nextPage", BookService.getNextPage(page, booksCount));
		req.setAttribute("prevPage", BookService.getPrevPage(page));
		req.setAttribute("page", page);
		req.getRequestDispatcher(Constants.GUEST_HOME_PAGE).forward(req, resp);
/*		String text = req.getParameter("text");
		int page = getPageNumber(req.getParameter("page"));
		int booksCount = 0;
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		BookDao bookDao = daoFactory.getBookDao();
		List<Book> books = new ArrayList<>();
		try {
			booksCount = bookDao.countBooks(text);
			books = bookDao.findBooks(text, null, null,
									  LIMIT_BOOKS_ON_PAGE, 
									  (page - 1) * LIMIT_BOOKS_ON_PAGE);
		} catch (DaoException e) {
			LOG.error(FIND_BOOK_ERROR_MESSAGE);
			req.getRequestDispatcher(Constants.ERROR_SERVLET_MAPPING)
															.forward(req, resp);
		}
		req.setAttribute("text", text);
		req.setAttribute("books", books);
		req.setAttribute("nextPage", getNextPage(page, booksCount));
		req.setAttribute("prevPage", getPrevPage(page));
		req.setAttribute("page", page);
		req.getRequestDispatcher(Constants.GUEST_HOME_PAGE).forward(req, resp);*/
	}
}
