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

@WebServlet("/reader")
public class ReaderServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(GuestServlet.class);
	private static final String FIND_BOOK_ERROR_MESSAGE = "Cannot find books";
	private static final String SORT_TYPE_ASC = "asc";
	private static final String SORT_TYPE_DESC = "desc";
	private static final String REQ_ATTR_SORT_BY = "sortBy";
	private static final String REQ_ATTR_SORT_TYPE = "sortType";
	private static final int LIMIT_BOOKS_ON_PAGE = 5;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
										throws ServletException, IOException {
		String text = req.getParameter("text");
		String orderBy = getOrderBy(req);
		String sortType = getSortType(req);
		int page = getPageNumber(req.getParameter("page"));
		int booksCount = 0;
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		BookDao bookDao = daoFactory.getBookDao();
		List<Book> books = new ArrayList<>();
		try {
			booksCount = bookDao.countBooks(text);
			books = bookDao.findBooks(text, orderBy, sortType,
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
		req.getRequestDispatcher(Constants.READER_HOME_PAGE).forward(req, resp);
	}

	private String getSortType(HttpServletRequest req) {
		String sessionSortBy = (String)req.getSession()
												.getAttribute(REQ_ATTR_SORT_BY);
		if (sessionSortBy == null) {
			return SORT_TYPE_ASC;
		}
		String reqSortBy = req.getParameter(REQ_ATTR_SORT_BY);
		String sortType = (String)req.getSession().getAttribute(REQ_ATTR_SORT_TYPE);
		if (reqSortBy == null) {
			return sortType;
		}
		if (sessionSortBy.equalsIgnoreCase(reqSortBy)) {
			if (sortType == null || sortType.equalsIgnoreCase(SORT_TYPE_DESC)) {
				sortType = SORT_TYPE_ASC;
			} else {
				sortType = SORT_TYPE_DESC;
			}
		} else {
			sortType = SORT_TYPE_ASC;
		}
		req.getSession().setAttribute(REQ_ATTR_SORT_TYPE, sortType);
		return sortType;
	}

	private String getOrderBy(HttpServletRequest req) {
		String sortBy = req.getParameter(REQ_ATTR_SORT_BY);
		if (sortBy != null) {
			req.getSession().setAttribute(REQ_ATTR_SORT_BY, sortBy);
		} else {
			sortBy = (String)req.getSession().getAttribute(REQ_ATTR_SORT_BY);
		}
		if (sortBy == null) {
			sortBy = "id";
		}
		return sortBy;
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
