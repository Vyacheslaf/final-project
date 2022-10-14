package org.example.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.example.Config;
import org.example.dao.BookDao;
import org.example.dao.DaoFactory;
import org.example.entity.Book;
import org.example.exception.DaoException;

public class BookService {
	private static final String SORT_TYPE_ASC = "asc";
	private static final String SORT_TYPE_DESC = "desc";
	private static final String SORT_BY_ID = "id";
	private static final String REQ_ATTR_SORT_BY = "sortBy";
	private static final String REQ_ATTR_SORT_TYPE = "sortType";
	private static final String REQ_ATTR_SEARCH_TEXT = "text";
	private static final String REQ_ATTR_CURRENT_PAGE = "page";
	
	public static List<Book> findBooks(HttpServletRequest req, int page) throws DaoException {
		String text = req.getParameter(REQ_ATTR_SEARCH_TEXT);
		String orderBy = getOrderBy(req);
		String sortType = getSortType(req);
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		BookDao bookDao = daoFactory.getBookDao();
		int offset = (page - 1) * Config.LIMIT_BOOKS_ON_PAGE;
		return bookDao.findBooks(text, orderBy, sortType, Config.LIMIT_BOOKS_ON_PAGE, offset);
	}

	public static int getBooksCount(HttpServletRequest req) throws DaoException {
		String text = req.getParameter(REQ_ATTR_SEARCH_TEXT);
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		BookDao bookDao = daoFactory.getBookDao();
		return bookDao.countBooks(text);
	}

	private static String getSortType(HttpServletRequest req) {
		String sessionSortBy = (String) req.getSession().getAttribute(REQ_ATTR_SORT_BY);
		if (sessionSortBy == null) {
			return SORT_TYPE_ASC;
		}
		String reqSortBy = req.getParameter(REQ_ATTR_SORT_BY);
		String sortType = (String) req.getSession().getAttribute(REQ_ATTR_SORT_TYPE);
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

	private static String getOrderBy(HttpServletRequest req) {
		String sortBy = req.getParameter(REQ_ATTR_SORT_BY);
		if (sortBy != null) {
			req.getSession().setAttribute(REQ_ATTR_SORT_BY, sortBy);
		} else {
			sortBy = (String)req.getSession().getAttribute(REQ_ATTR_SORT_BY);
		}
		if (sortBy == null) {
			sortBy = SORT_BY_ID;
		}
		return sortBy;
	}

	public static int getPageNumber(HttpServletRequest req) {
		String str = req.getParameter(REQ_ATTR_CURRENT_PAGE);
		if (str == null || !str.matches("\\d+") || Integer.parseInt(str) == 0) {
			return 1;
		}
		return Integer.parseInt(str);
	}

	public static int getNextPage(int page, int booksCount) {
		int maxPagesCount;
		if (booksCount % Config.LIMIT_BOOKS_ON_PAGE != 0) {
			maxPagesCount = (booksCount / Config.LIMIT_BOOKS_ON_PAGE) + 1;
		} else {
			maxPagesCount = booksCount / Config.LIMIT_BOOKS_ON_PAGE;
		}
		if (maxPagesCount > page) {
			page++;
		}
		return page;
	}
	
	public static int getPrevPage(int page) {
		return page > 1 ? --page : 1;
	}
}
