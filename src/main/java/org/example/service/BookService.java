package org.example.service;

import static org.example.util.Config.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.util.Config;
import org.example.dao.BookDao;
import org.example.entity.Book;
import org.example.exception.DaoException;
import org.example.exception.ServiceException;

public class BookService {

	private static final Logger LOG = LogManager.getLogger(BookService.class);
	private static final String SORT_TYPE_ASC = "asc";
	private static final String REQ_ATTR_SORT_BY = "sortBy";
	private static final String REQ_ATTR_SORT_TYPE = "sortType";
	private static final String REQ_ATTR_SEARCH_TEXT = "search";
	private static final String REQ_ATTR_CURRENT_PAGE = "page";
	private static final String REQ_PARAM_ISBN = "isbn";
	private static final String REQ_PARAM_AUTHOR = "author";
	private static final String REQ_PARAM_TITLE = "title";
	private static final String REQ_PARAM_PUBLICATION = "publication";
	private static final String REQ_PARAM_PUBLICATION_YEAR = "year";
	private static final String REQ_PARAM_QUANTITY = "quantity";
	private static final String REQ_PARAM_BOOK_ID = "bookid";
	private static final String ISBN_REGEX = "^(97(8|9))?\\d{9}(\\d|X)$";
	private static final String ONLY_DIGITS_REGEX = "\\d+";
	private static final String ERROR_WRONG_ISBN = "error.wrong.isbn";//ISBN is wrong
	private static final String ERROR_WRONG_PUBLICATION_YEAR = "error.wrong.publication.year";//"Year of publication is wrong"
	private static final String ERROR_WRONG_QUANTITY = "error.wrong.quantity";//"Quantity is wrong"
	private static final String ERROR_CANNOT_DELETE_BOOK = "error.cannot.delete.book";//"Cannot delete the book: Wrong id!"
	private static final String ERROR_CANNOT_CHANGE_BOOK = "error.cannot.change.book";//"Cannot change the book: Wrong id!"
	
	public static List<Book> findBooks(HttpServletRequest req, int page) throws DaoException {
		String text = req.getParameter(REQ_ATTR_SEARCH_TEXT);
		String orderBy = getOrderBy(req);
		String sortType = getSortType(req);
		BookDao bookDao = DAO_FACTORY.getBookDao();
		int offset = (page - 1) * Config.LIMIT_BOOKS_ON_PAGE;
		return bookDao.findBooks(text, orderBy, sortType, Config.LIMIT_BOOKS_ON_PAGE, offset);
	}

	public static int getBooksCount(HttpServletRequest req) throws DaoException {
		String text = req.getParameter(REQ_ATTR_SEARCH_TEXT);
		BookDao bookDao = DAO_FACTORY.getBookDao();
		return bookDao.countBooks(text);
	}

	private static String getSortType(HttpServletRequest req) {
		String sortType = req.getParameter(REQ_ATTR_SORT_TYPE);
		if (sortType == null) {
			sortType= SORT_TYPE_ASC;
		}
		req.setAttribute(REQ_ATTR_SORT_TYPE, sortType);
		return sortType;
	}

	private static String getOrderBy(HttpServletRequest req) {
		String sortBy = req.getParameter(REQ_ATTR_SORT_BY);
		if (sortBy != null) {
			req.setAttribute(REQ_ATTR_SORT_BY, sortBy);
		}
		return sortBy;
	}

	public static int getPageNumber(HttpServletRequest req) {
		String str = req.getParameter(REQ_ATTR_CURRENT_PAGE);
		if (str == null || !str.matches(ONLY_DIGITS_REGEX) || Integer.parseInt(str) == 0) {
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

	public static void addBook(HttpServletRequest req) throws ServiceException, DaoException {
		BookDao bookDao = DAO_FACTORY.getBookDao();
		bookDao.create(getBookFromRequest(req));
	}
	
	private static Book getBookFromRequest(HttpServletRequest req) throws ServiceException {
		if (!checkISBN(req)) {
			logAndThrowException(ERROR_WRONG_ISBN);
		}
		if (!checkPublicationYear(req)) {
			logAndThrowException(ERROR_WRONG_PUBLICATION_YEAR);
		}
		if (!isNumber(req.getParameter(REQ_PARAM_QUANTITY))) {
			logAndThrowException(ERROR_WRONG_QUANTITY);
		}
		return new Book.Builder().setISBN(req.getParameter(REQ_PARAM_ISBN))
								 .setAuthor(req.getParameter(REQ_PARAM_AUTHOR))
								 .setTitle(req.getParameter(REQ_PARAM_TITLE))
								 .setPublication(req.getParameter(REQ_PARAM_PUBLICATION))
								 .setPublicationYear(Integer.parseInt(req.getParameter(REQ_PARAM_PUBLICATION_YEAR)))
								 .setQuantity(Integer.parseInt(req.getParameter(REQ_PARAM_QUANTITY)))
								 .build();
	}

	private static boolean isNumber(String str) {
		return str != null && str.matches(ONLY_DIGITS_REGEX);
	}

	private static boolean checkISBN(HttpServletRequest req) {
		String isbn = req.getParameter(REQ_PARAM_ISBN);
		if (isbn == null || !isbn.matches(ISBN_REGEX)) {
			return false;
		}
		if (isbn.length() == 10) {
			return checkISBN10(isbn);
		}
		return checkISBN13(isbn);
	}
	
	private static boolean checkISBN10(String isbn) {
		List<Integer> weights = IntStream.rangeClosed(1, 10)
										 .mapToObj(Integer::valueOf)
										 .sorted(Collections.reverseOrder())
										 .collect(Collectors.toList());
		List<Integer> digits = Arrays.asList(isbn.split(""))
									 .stream()
									 .map(BookService::convertISBNSymbolToDigit)
									 .collect(Collectors.toList());
		int checksum = IntStream.range(0, 10)
								.map(i -> weights.get(i) * digits.get(i))
								.sum();
		return checksum % 11 == 0;
	}
	
	private static boolean checkISBN13(String isbn) {
		List<Integer> weights = IntStream.range(0, 13)
										 .map(i -> (i % 2) * 2 + 1)
										 .mapToObj(Integer::valueOf)
										 .collect(Collectors.toList());
		List<Integer> digits = Arrays.asList(isbn.split(""))
									 .stream()
									 .map(BookService::convertISBNSymbolToDigit)
									 .collect(Collectors.toList());
		int checksum = IntStream.range(0, 13)
								.map(i -> weights.get(i) * digits.get(i))
								.sum();
		return checksum % 10 == 0;
	}
	
	private static Integer convertISBNSymbolToDigit(String sym) {
		return sym.equalsIgnoreCase("X") ? Integer.valueOf(10) : Integer.valueOf(sym);
	}

	private static boolean checkPublicationYear(HttpServletRequest req) {
		String publicationYear = req.getParameter(REQ_PARAM_PUBLICATION_YEAR);
		if (!isNumber(publicationYear)) {
			return false;
		}
		int year = Integer.parseInt(publicationYear);
		if (year < 1900 || year > LocalDateTime.now().getYear()) {
			return false;
		}
		return true;
	}
	
	public static Book findBookByISBN(String isbn) throws DaoException {
		BookDao bookDao = DAO_FACTORY.getBookDao();
		Book book = new Book.Builder().setISBN(isbn).build();
		return bookDao.find(book);
	}

	public static void deleteBook(String bookId) throws ServiceException, DaoException {
		if (!isNumber(bookId)) {
			logAndThrowException(ERROR_CANNOT_DELETE_BOOK);
		}
		Book book = new Book.Builder().setId(Integer.parseInt(bookId))
									  .build();
		BookDao bookDao = DAO_FACTORY.getBookDao();
		bookDao.remove(book);
	}

	public static void changeBook(HttpServletRequest req) throws ServiceException, DaoException {
		String bookId = req.getParameter(REQ_PARAM_BOOK_ID);
		if (!isNumber(bookId)) {
			logAndThrowException(ERROR_CANNOT_CHANGE_BOOK);
		}
		Book book = getBookFromRequest(req);
		book.setId(Integer.parseInt(bookId));
		BookDao bookDao = DAO_FACTORY.getBookDao();
		bookDao.update(book);
	}
	
	private static void logAndThrowException(String message) throws ServiceException {
		LOG.error(message);
		throw new ServiceException(message);
	}
}
