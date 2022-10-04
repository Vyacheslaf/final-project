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
import org.example.Config;
import org.example.dao.BookDao;
import org.example.dao.DaoFactory;
import org.example.entity.Book;
import org.example.exception.DaoException;

@WebServlet("/guest")
public class GuestServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(GuestServlet.class);
	private static final String FIND_BOOK_ERROR_MESSAGE = "Cannot find books";
	private static final int LIMIT_BOOKS_ON_PAGE = 5;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {
		String text = req.getParameter("text");
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
		req.getRequestDispatcher(Constants.GUEST_HOME_PAGE).forward(req, resp);
	}

	private int getPageNumber(String str) {
		if (str == null || str == "" || !str.matches("\\d+") 
												|| Integer.parseInt(str) == 0) {
			return 1;
		}
		return Integer.parseInt(str);
	}
	
	private int getNextPage(int page, int booksCount) {
		int maxPagesCount;
		if (booksCount % LIMIT_BOOKS_ON_PAGE != 0) {
			maxPagesCount = (booksCount / LIMIT_BOOKS_ON_PAGE) + 1;
		} else {
			maxPagesCount = booksCount / LIMIT_BOOKS_ON_PAGE;
		}
		if (maxPagesCount > page) {
			page++;
		}
		return page;
	}
	
	private int getPrevPage(int page) {
		return page > 1 ? --page : 1;
	}
}
