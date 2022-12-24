package org.example.service;

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

/**
 * The {@code BookService} class is used to provide interaction between controllers and {@code BookDao}
 * 
 * @author Vyacheslav Fedchenko
 */

public class BookService {

	private static final Logger LOG = LogManager.getLogger(BookService.class);
	private static final String SORT_TYPE_ASC = "asc";
	private static final String SORT_TYPE_DESC = "desc";
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
	private static final String ISBN10_REGEX = "^(\\d{9}(\\d|X))$";
	private static final String ISBN13_REGEX = "^(97(8|9)\\d{10})$";
	private static final String SEARCH_TEXT_REGEX = "^[\\wА-яІіЇїЄє'Ґґ !:?.,-]+$";
	private static final String AUTHOR_REGEX = "^[\\wА-яІіЇїЄє'Ґґ .,-]+$";
	private static final String TITLE_REGEX = "^[\\wА-яІіЇїЄє'Ґґ !:?.,-]+$";
	private static final String PUBLICATION_REGEX = "^[\\wА-яІіЇїЄє'Ґґ -]+$";
	private static final String ONLY_DIGITS_REGEX = "\\d+";
	private static final String ERROR_WRONG_SEARCH_TEXT = "error.wrong.search.text";
	private static final String ERROR_WRONG_ISBN = "error.wrong.isbn";
	private static final String ERROR_WRONG_PUBLICATION_YEAR = "error.wrong.publication.year";
	private static final String ERROR_WRONG_QUANTITY = "error.wrong.quantity";
	private static final String ERROR_CANNOT_DELETE_BOOK = "error.cannot.delete.book";
	private static final String ERROR_CANNOT_CHANGE_BOOK = "error.cannot.change.book";
	private static final String ERROR_WRONG_AUTHOR = "error.wrong.author";
	private static final String ERROR_WRONG_TITLE = "error.wrong.title";
	private static final String ERROR_WRONG_PUBLICATION = "error.wrong.publication";
	private static final int ISBN10_NUMBER_OF_DIGITS = 10;
	private static final int ISBN10_CHECKSUM_BASE = 11;
	private static final int ISBN13_NUMBER_OF_DIGITS = 13;
	private static final int ISBN13_CHECKSUM_BASE = 10;
	
	private final BookDao bookDao;
	
    /**
     * Initializes a newly created {@code BookService} object with {@code BookDao} argument
     */
	public BookService(BookDao bookDao) {
		this.bookDao = bookDao;
	}
	
    /**
     * Initializes a newly created {@code BookService} object with {@code BookDao} 
     * produced by configured {@code DaoFactory}
     */
	public BookService() {
		this(Config.DAO_FACTORY.getBookDao());
	}
	
	/**
	 * Extracts from the request the page number, search phrase and sort options 
	 * to find the list of books with {@code BookDao}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} with parameters of search
	 * @return the ordered limited list of found books
	 * @throws DaoException if cannot get list of books from {@code BookDao}
	 * @throws ServiceException if request parameters is wrong
	 */
	public List<Book> findBooks(HttpServletRequest req) throws DaoException, ServiceException {
		int page = getPageNumber(req);
		String searchText = getSearchText(req);
		String orderBy = getOrderBy(req);
		String sortType = getSortType(req);
		int offset = 0;
		if (page > 0) {
			offset = (page - 1) * Config.LIMIT_BOOKS_ON_PAGE;
		}
		return bookDao.findBooks(searchText, orderBy, sortType, Config.LIMIT_BOOKS_ON_PAGE, offset);
	}

	/**
	 * Extracts the search phrase from the request
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing search parameters
	 * @return {@code String} containing search phrase
	 * @throws ServiceException if the search parameter of request is wrong
	 */
	private static String getSearchText(HttpServletRequest req) throws ServiceException {
		String searchText = req.getParameter(REQ_ATTR_SEARCH_TEXT);
		if ((searchText != null) &&!searchText.equalsIgnoreCase("") &&!searchText.matches(SEARCH_TEXT_REGEX)) {
			logAndThrowException(ERROR_WRONG_SEARCH_TEXT);
		}
		return searchText;
	}

	/**
	 * Returns the number of all books corresponding to search parameters
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing search parameters
	 * @return the number of all books corresponding to search parameters
	 * @throws DaoException if cannot get number of books from {@code BookDao}
	 * @throws ServiceException if search parameters is wrong
	 */
	public int getBooksCount(HttpServletRequest req) throws DaoException, ServiceException {
		String searchText = getSearchText(req);
		return bookDao.countBooks(searchText);
	}

	/**
	 * Extracts ordering type from and sets ordering type attribute to the request
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing ordering type parameter
	 * @return Ordering type: {@code asc} | {@code desc}
	 */
	private static String getSortType(HttpServletRequest req) {
		String sortType = req.getParameter(REQ_ATTR_SORT_TYPE);
		if (!SORT_TYPE_ASC.equalsIgnoreCase(sortType)) {
			sortType = SORT_TYPE_DESC;
		}
		req.setAttribute(REQ_ATTR_SORT_TYPE, sortType);
		return sortType;
	}

	/**
	 * Extracts ordering parameter from the request
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing ordering parameter
	 * @return
	 * 		  Ordering by: {@code author} | {@code title} | {@code publication} | {@code year}
	 */
	private static String getOrderBy(HttpServletRequest req) {
		String sortBy = req.getParameter(REQ_ATTR_SORT_BY);
		if (sortBy != null) {
			req.setAttribute(REQ_ATTR_SORT_BY, sortBy);
		}
		return sortBy;
	}

	/**
	 * Extracts the page number from the request
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing page number parameter
	 * @return The page number or 1 if the parameter is wrong
	 */
	public static int getPageNumber(HttpServletRequest req) {
		String str = req.getParameter(REQ_ATTR_CURRENT_PAGE);
		if ((str == null) ||!str.matches(ONLY_DIGITS_REGEX) || (Integer.parseInt(str) == 0)) {
			return 1;
		}
		return Integer.parseInt(str);
	}

	/**
	 * Returns next page number according to current page number and quantity of books
	 * 
	 * @param page
	 * 		  the current page number
	 * @param booksCount
	 * 		  the quantity of books
	 * @return the next page number
	 */
	public static int getNextPage(int page, int booksCount) {
		if ((page <= 0) || (booksCount < 0)) {
			return 1;
		}
		if ((booksCount - page * Config.LIMIT_BOOKS_ON_PAGE) > 0) {
			page++;
		}
		return page;
	}
	
	/**
	 * Returns previous page number according to current page number
	 * 
	 * @param page
	 * 		  the current page number
	 * @return the previous page number
	 */
	public static int getPrevPage(int page) {
		return ((page > 1) ? --page : 1);
	}

	/**
	 * Adds to the catalog the book extracted from the request
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing book's info
	 * @throws ServiceException if book's info is wrong
	 * @throws DaoException if cannot add the book to the catalog
	 */
	public void addBook(HttpServletRequest req) throws ServiceException, DaoException {
		bookDao.create(getBookFromRequest(req));
	}
	
	/**
	 * Builds the {@code Book} object from request parameters
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing parameters of {@code Book}'s fields
	 * @return the {@code Book}
	 * @throws ServiceException if some of parameters of {@code Book}'s fields is wrong
	 */
	private static Book getBookFromRequest(HttpServletRequest req) throws ServiceException {
		return new Book.Builder().setISBN(getISBN(req))
								 .setAuthor(getAuthor(req))
								 .setTitle(getTitle(req))
								 .setPublication(getPublication(req))
								 .setPublicationYear(getPublicationYear(req))
								 .setQuantity(getQuantity(req))
								 .build();
	}

	/**
	 * Check the {@code String} for positive number
	 * 
	 * @param str
	 * 		  The {@code String}
	 * @return {@code true} only if the {@code String} is a positive number
	 */
	private static boolean isPositiveNumber(String str) {
		return ((str != null) && str.matches(ONLY_DIGITS_REGEX) && (Integer.parseInt(str) > 0));
	}
	
	/**
	 * Returns ISBN from the {@code HttpServletRequest}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing ISBN
	 * @return ISBN
	 * @throws ServiceException if ISBN is wrong
	 */
	private static String getISBN(HttpServletRequest req) throws ServiceException {
		String isbn = req.getParameter(REQ_PARAM_ISBN);
		if (!isISBN10(isbn) &&!isISBN13(isbn)) {
			LOG.error(ERROR_WRONG_ISBN + ": " + isbn);
			throw new ServiceException(ERROR_WRONG_ISBN);
		}
		return isbn;
	}
	
	/**
	 * Check the {@code String} for corresponding to ISBN-10 format
	 * 
	 * @param isbn
	 * 		  The {@code String}
	 * @return {@code true} only if the {@code String} matches ISBN-10 format
	 */
	private static boolean isISBN10(String isbn) {
		if ((isbn == null) ||!isbn.matches(ISBN10_REGEX)) {
			return false;
		}
		
		/* Getting the list of ISBN-10 weighting coefficients: {10, 9, 8, 7, 6, 5, 4, 3, 2, 1} */
		List<Integer> weights = IntStream.rangeClosed(1, ISBN10_NUMBER_OF_DIGITS)
										 .mapToObj(Integer::valueOf)
										 .sorted(Collections.reverseOrder())
										 .collect(Collectors.toList());
		
		/* Getting the list of digits from ISBN-10 string */
		List<Integer> digits = Arrays.asList(isbn.split(""))
									 .stream()
									 .map(BookService::convertISBNSymbolToDigit)
									 .collect(Collectors.toList());
		
		/* Calculate ISBN-10 checksum: the sum of results of multiplying weighting coefficients by ISBN-10 digits */
		int checksum = IntStream.range(0, ISBN10_NUMBER_OF_DIGITS)
								.map(i -> weights.get(i) * digits.get(i))
								.sum();
		return (checksum % ISBN10_CHECKSUM_BASE == 0);
	}
	
	/**
	 * Check the {@code String} for corresponding to ISBN-13 format
	 * 
	 * @param isbn
	 * 		  The {@code String}
	 * @return {@code true} only if the {@code String} matches ISBN-13 format
	 */
	private static boolean isISBN13(String isbn) {
		if ((isbn == null) ||!isbn.matches(ISBN13_REGEX)) {
			return false;
		}
		
		/* Getting the list of ISBN-13 weighting coefficients: {1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1} */
		List<Integer> weights = IntStream.range(0, ISBN13_NUMBER_OF_DIGITS)
										 .map(i -> (i % 2) * 2 + 1)
										 .mapToObj(Integer::valueOf)
										 .collect(Collectors.toList());
		
		/* Getting the list of digits from ISBN-13 string */
		List<Integer> digits = Arrays.asList(isbn.split(""))
									 .stream()
									 .map(BookService::convertISBNSymbolToDigit)
									 .collect(Collectors.toList());
		
		/* Calculate ISBN-13 checksum: the sum of results of multiplying weighting coefficients by ISBN-13 digits */
		int checksum = IntStream.range(0, ISBN13_NUMBER_OF_DIGITS)
								.map(i -> weights.get(i) * digits.get(i))
								.sum();
		return (checksum % ISBN13_CHECKSUM_BASE == 0);
	}
	
	/**
	 * Converts ISBN's symbol to {@code Integer}
	 * 
	 * @param sym
	 * 		  The ISBN's symbol that can be only a digit form 0 to 9 or a latin letter "X"
	 * @return the {@code Integer} holding the value from 0 to 9 if {@code sym} is a digit or 
	 * 		   the value 10 if it equals to "X"
	 */
	private static Integer convertISBNSymbolToDigit(String sym) {
		return sym.equalsIgnoreCase("X") ? Integer.valueOf(10) : Integer.valueOf(sym);
	}

	/**
	 * Extracts the year of the book's publication from the {@code HttpServletRequest}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing the parameter of publication year of the book
	 * @return the year of the book's publication
	 * @throws ServiceException if the {@code HttpServletRequest} does not contain this parameter or
	 * 		   the parameter is wrong
	 */
	private static int getPublicationYear(HttpServletRequest req) throws ServiceException {
		String publicationYear = req.getParameter(REQ_PARAM_PUBLICATION_YEAR);
		if (!isPositiveNumber(publicationYear)) {
			logAndThrowException(ERROR_WRONG_PUBLICATION_YEAR);
		}
		int year = Integer.parseInt(publicationYear);
		if ((year < Config.EARLIEST_PUBLICATION_YEAR) || (year > LocalDateTime.now().getYear())) {
			logAndThrowException(ERROR_WRONG_PUBLICATION_YEAR);
		}
		return year;
	}
	
	/**
	 * Extracts the quantity of books from the {@code HttpServletRequest}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing the parameter of quantity of books
	 * @return the quantity of books
	 * @throws ServiceException if the {@code HttpServletRequest} does not contain this parameter or
	 * 		   the parameter is wrong
	 */
	private static int getQuantity(HttpServletRequest req) throws ServiceException {
		String quantity = req.getParameter(REQ_PARAM_QUANTITY);
		if (!isPositiveNumber(quantity)) {
			logAndThrowException(ERROR_WRONG_QUANTITY);
		}
		return Integer.parseInt(quantity);
	}
	
	/**
	 * Extracts author's full name from the {@code HttpServletRequest}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing the parameter of author's full name
	 * @return author's full name
	 * @throws ServiceException if the {@code HttpServletRequest} does not contain this parameter or
	 * 		   the parameter is wrong
	 */
	private static String getAuthor(HttpServletRequest req) throws ServiceException {
		String author = req.getParameter(REQ_PARAM_AUTHOR);
		if ((author == null) ||!author.matches(AUTHOR_REGEX)) {
			logAndThrowException(ERROR_WRONG_AUTHOR);
		}
		return author;
	}
	
	/**
	 * Extracts book's title from the {@code HttpServletRequest}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing the parameter of book's title
	 * @return book's title
	 * @throws ServiceException if the {@code HttpServletRequest} does not contain this parameter or
	 * 		   the parameter is wrong
	 */
	private static String getTitle(HttpServletRequest req) throws ServiceException {
		String title = req.getParameter(REQ_PARAM_TITLE);
		if ((title == null) ||!title.matches(TITLE_REGEX)) {
			logAndThrowException(ERROR_WRONG_TITLE);
		}
		return title;
	}
	
	/**
	 * Extracts publication name from the {@code HttpServletRequest}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing the parameter of publication name
	 * @return the name of publication
	 * @throws ServiceException if the {@code HttpServletRequest} does not contain this parameter or
	 * 		   the parameter is wrong
	 */
	private static String getPublication(HttpServletRequest req) throws ServiceException {
		String publication = req.getParameter(REQ_PARAM_PUBLICATION);
		if ((publication == null) ||!publication.matches(PUBLICATION_REGEX)) {
			logAndThrowException(ERROR_WRONG_PUBLICATION);
		}
		return publication;
	}
	/**
	 * Returns the {@code Book} with ISBN extracted from the {@code HttpServletRequest}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing ISBN
	 * @return the {@code Book} with the required ISBN 
	 * @throws DaoException if cannot get the {@code Book} from the catalog
	 * @throws ServiceException if ISBN is wrong
	 */
	public Book findBookByISBN(HttpServletRequest req) throws DaoException, ServiceException {
		String isbn = getISBN(req);
		Book book = new Book.Builder().setISBN(isbn).build();
		return bookDao.find(book);
	}

	/**
	 * Updates the book's info in the catalog
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing book's info
	 * @throws ServiceException if book's info is wrong
	 * @throws DaoException if cannot save changed info to the catalog
	 */
	public void changeBook(HttpServletRequest req) throws ServiceException, DaoException {
		int bookId = getBookId(req, ERROR_CANNOT_CHANGE_BOOK);
		Book book = getBookFromRequest(req);
		book.setId(bookId);
		bookDao.update(book);
	}

	/**
	 * Obtains the book's ID from the {@code HttpServletRequest} and removes the book with this ID from the catalog
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing book's ID
	 * @throws ServiceException if book's ID is wrong
	 * @throws DaoException if cannot delete the book from the catalog
	 */
	public void deleteBook(HttpServletRequest req) throws ServiceException, DaoException {
		int bookId = getBookId(req, ERROR_CANNOT_DELETE_BOOK);
		Book book = new Book.Builder().setId(bookId).build();
		bookDao.remove(book);
	}
	
	/**
	 * Obtains the book's ID from the {@code HttpServletRequest}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing book's ID
	 * @param errorMessage
	 * 		  The error message if the book's ID is wrong
	 * @return the book's ID
	 * @throws ServiceException if the book's ID is wrong
	 */
	private static int getBookId(HttpServletRequest req, String errorMessage) throws ServiceException {
		String bookId = req.getParameter(REQ_PARAM_BOOK_ID);
		if (!isPositiveNumber(bookId)) {
			logAndThrowException(errorMessage);
		}
		return Integer.parseInt(bookId);
	}
	
	/**
	 * Sends an error message to the log and throws the {@code ServiceException} with this message
	 * 
	 * @param message
	 * 		  an error message for the log and the {@code ServiceException}
	 * @throws ServiceException
	 */
	private static void logAndThrowException(String message) throws ServiceException {
		LOG.error(message);
		throw new ServiceException(message);
	}
}
