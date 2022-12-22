package org.example.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.BookDao;
import org.example.entity.Book;
import org.example.exception.DaoException;

/**
 * The {@code BookDaoImpl} class is used to realize operations with books in MySQL DBMS
 * 
 * @author Vyacheslav Fedchenko
 * @see org.example.dao.BookDao
 */

public class BookDaoImpl implements BookDao {

	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(BookDaoImpl.class);
	
	/**
	 * The key for getting from the property file a SQL query for count all books.
	 */
	private static final String QUERY_COUNT_ALL_BOOKS = "select.count.all.books";
	
	/**
	 * The key for getting from the property file a SQL query for count all found books,
	 * that match to the search criteria
	 */
	private static final String QUERY_COUNT_BOOKS_FROM_SEARCH = "select.count.books.from.search";
	
	/**
	 * The key for getting from the property file a SQL query for insert a new book.
	 */
	private static final String QUERY_INSERT_BOOK = "insert.book";
	
	/**
	 * The key for getting from the property file a SQL query for select the book by ISBN.
	 */
	private static final String QUERY_SELECT_BOOK_BY_ISBN = "select.book.by.isbn";
	
	/**
	 * The key for getting from the property file a SQL query for count books with selected ID,
	 * that are on subscription.
	 */
	private static final String QUERY_SELECT_COUNT_BOOK_ON_SUBSCRIPTION = "select.count.books.on.subscription.by.id";
	
	/**
	 * The key for getting from the property file a SQL query for delete the book.
	 */
	private static final String QUERY_DELETE_BOOK = "delete.book";
	
	/**
	 * The key for getting from the property file a SQL query for select the book by ID.
	 */
	private static final String QUERY_SELECT_BOOK_BY_ID = "select.book.by.id";
	
	/**
	 * The key for getting from the property file a SQL query for update the book.
	 */
	private static final String QUERY_UPDATE_BOOK = "update.book";
	
	/**
	 * The key for getting from the property file a first part of SQL query 
	 * for search books.
	 */
	private static final String QUERY_SELECT_BOOK = "select.book";
	
	/**
	 * The key for getting from the property file a part of SQL query 
	 * for search books by author/title.
	 */
	private static final String QUERY_SEARCH_TEXT = "search.text";
	
	/**
	 * The key for getting from the property file a part of SQL query 
	 * for select a field, by which to be ordered books.
	 */
	private static final String QUERY_ORDER_BY = "order.by";
	
	/**
	 * The key for getting from the property file a part of SQL query 
	 * for ordering books by default.
	 */
	private static final String ORDER_BY_DEFAULT = "default";
	
	/**
	 * The {@code String} that defines descending order.
	 */
	private static final String ORDER_TYPE_DESC = "desc";
	
	/**
	 * The key for getting from the property file a part of SQL query 
	 * for descending order.
	 */
	private static final String QUERY_ORDER_DESC = "order.desc";
	
	/**
	 * The key for getting from the property file a part of SQL query 
	 * for set search limit and offset.
	 */
	private static final String QUERY_LIMIT_OFFSET = "limit.offset";
	
	/**
	 * The {@code String} that defines any symbol in SQL.
	 */
	private static final String SQL_ANY_SYMBOLS = "%";

	/**
	 * The {@code String} specifying the log message if the book cannot be found.
	 */
	private static final String LOG_MESSAGE_CANNOT_FOUND_BOOK = "Cannot found the book";

	/**
	 * The {@code String} specifying the log message if quantity of the book cannot be changed.
	 */
	private static final String LOG_MESSAGE_CANNOT_CHANGE_QUANTITY = "Cannot change quantity of the book";

	/**
	 * The {@code String} specifying the log message if the book cannot be deleted.
	 */
	private static final String LOG_MESSAGE_CANNOT_DELETE_ISSUED_BOOK = "The book issued to reader, cannot delete it";
	
	/**
	 * The key for getting a localized error message from the resource bundles 
	 * if the book is already in the catalog.
	 */
	private static final String ERROR_BOOK_IS_ALREADY_IN_CATALOG = "error.book.is.already.in.catalog";
	
	/**
	 * The key for getting a localized error message from the resource bundles 
	 * if the book cannot be created.
	 */
	private static final String ERROR_CANNOT_CREATE_BOOK = "error.cannot.create.book";
	
	/**
	 * The key for getting a localized error message from the resource bundles 
	 * if data cannot be obtained.
	 */
	private static final String ERROR_CANNOT_GET_DATA = "error.cannot.get.data";
	
	/**
	 * The key for getting a localized error message from the resource bundles 
	 * if the book cannot be found.
	 */
	private static final String ERROR_CANNOT_FOUND_BOOK = "error.cannot.found.book";
	
	/**
	 * The key for getting a localized error message from the resource bundles 
	 * if quantity of the book cannot be changed.
	 */
	private static final String ERROR_CANNOT_CHANGE_BOOK_QUANTITY = "error.cannot.change.book.quantity";
	
	/**
	 * The key for getting a localized error message from the resource bundles 
	 * if changes of the book cannot be saved.
	 */
	private static final String ERROR_CANNOT_SAVE_BOOK_CHANGES = "error.cannot.save.book.changes";
	
	/**
	 * The key for getting a localized error message from the resource bundles 
	 * if the book issued to the reader and cannot be deleted.
	 */
	private static final String ERROR_CANNOT_DELETE_ISSUED_BOOK = "error.cannot.delete.issued.book";
	
	/**
	 * The key for getting a localized error message from the resource bundles 
	 * if the book cannot be deleted.
	 */
	private static final String ERROR_DAO_CANNOT_DELETE_BOOK = "error.dao.cannot.delete.book";
	
	@Override
	public Book create(Book book) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_INSERT_BOOK));
			pstmt.setString(++k, book.getAuthor());
			pstmt.setString(++k, book.getTitle());
			pstmt.setString(++k, book.getPublication());
			pstmt.setInt(++k, book.getPublicationYear());
			pstmt.setInt(++k, book.getQuantity());
			pstmt.setString(++k, book.getIsbn());
			pstmt.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException ex){
			LOG.error(ex);
			throw new DaoException(ERROR_BOOK_IS_ALREADY_IN_CATALOG, ex);
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_CREATE_BOOK, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return book;
	}

	@Override
	public Book find(Book book) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_BOOK_BY_ISBN));
			pstmt.setString(++k, book.getIsbn());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return getBook(rs);
			}
			return null;
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_DATA, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
	}
	
	@Override
	public void update(Book book) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_BOOK_BY_ID));
			pstmt.setInt(++k, book.getId());
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				LOG.error(LOG_MESSAGE_CANNOT_FOUND_BOOK);
				throw new DaoException(ERROR_CANNOT_FOUND_BOOK);
			}
			Book bookFromCatalog = getBook(rs);
			int available = bookFromCatalog.getAvailable() + book.getQuantity() - bookFromCatalog.getQuantity();
			if (available < 0) {
				LOG.error(LOG_MESSAGE_CANNOT_CHANGE_QUANTITY);
				throw new DaoException(ERROR_CANNOT_CHANGE_BOOK_QUANTITY);
			}
			book.setAvailable(available);
			k = 0;
			DbManager.close(pstmt);
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_UPDATE_BOOK));
			pstmt.setString(++k, book.getAuthor());
			pstmt.setString(++k, book.getTitle());
			pstmt.setString(++k, book.getPublication());
			pstmt.setInt(++k, book.getPublicationYear());
			pstmt.setInt(++k, book.getQuantity());
			pstmt.setString(++k, book.getIsbn());
			pstmt.setInt(++k, available);
			pstmt.setInt(++k, book.getId());
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			DbManager.rollback(con);
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_SAVE_BOOK_CHANGES, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
	}

	@Override
	public void remove(Book book) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_COUNT_BOOK_ON_SUBSCRIPTION));
			pstmt.setInt(++k, book.getId());
			rs = pstmt.executeQuery();
			if (!rs.next() || rs.getInt(k) > 0) {
				LOG.error(LOG_MESSAGE_CANNOT_DELETE_ISSUED_BOOK);
				throw new DaoException(ERROR_CANNOT_DELETE_ISSUED_BOOK);
			}
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_DELETE_BOOK));
			pstmt.setInt(++k, book.getId());
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			DbManager.rollback(con);
			LOG.error(e);
			throw new DaoException(ERROR_DAO_CANNOT_DELETE_BOOK, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
	}
	
	@Override
	public List<Book> findBooks(String searchText, String orderBy,
								String orderType, int limit, int offset) throws DaoException{
		List<Book> books = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			String query = createQuery(searchText, orderBy, orderType);
			pstmt = con.prepareStatement(query);
			if (searchText != null && !searchText.equalsIgnoreCase("")) {
				String search = SQL_ANY_SYMBOLS + searchText + SQL_ANY_SYMBOLS;
				pstmt.setString(++k, search);
				pstmt.setString(++k, search);
			}
			pstmt.setInt(++k, limit);
			pstmt.setInt(++k, offset);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				books.add(getBook(rs));
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_DATA, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return books;
	}
	
	@Override
	public int countBooks(String searchText) throws DaoException{
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			if (searchText == null || searchText.equalsIgnoreCase("")) {
				pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_COUNT_ALL_BOOKS));
			} else {
				String search = SQL_ANY_SYMBOLS + searchText + SQL_ANY_SYMBOLS;
				pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_COUNT_BOOKS_FROM_SEARCH));
				pstmt.setString(++k, search);
				pstmt.setString(++k, search);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_DATA, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return count;
	}
	
	/**
	 * Creates SQL query to get ordered list of books containing search phrase from the database
	 * 
	 * @param searchText
	 * 		  Search expression that must be included in book's title or in full name of book's author
	 * 
	 * @param orderBy
	 * 		  Ordering by: {@code author} | {@code title} | {@code publication} | {@code year}
	 * 
	 * @param orderType
	 * 		  Ordering type: {@code asc} | {@code desc}
	 * 
	 * @return {@code String} containing prepared SQL query
	 */
	private static String createQuery(String searchText, String orderBy, String orderType) {
		StringBuilder query = new StringBuilder("");
		query.append(Queries.getInstance().getQuery(QUERY_SELECT_BOOK)).append(" ");
		if (searchText != null && !searchText.equalsIgnoreCase("")) {
			query.append(Queries.getInstance().getQuery(QUERY_SEARCH_TEXT)).append(" ");
		}
		query.append(Queries.getInstance().getQuery(QUERY_ORDER_BY)).append(" ");
		if (orderBy == null || orderBy.equalsIgnoreCase("") || Queries.getInstance().getQuery(orderBy) == null) {
			orderBy = ORDER_BY_DEFAULT;
		}
		query.append(Queries.getInstance().getQuery(orderBy)).append(" ");
		if (ORDER_TYPE_DESC.equalsIgnoreCase(orderType)) {
			query.append(Queries.getInstance().getQuery(QUERY_ORDER_DESC)).append(" ");
		}
		query.append(Queries.getInstance().getQuery(QUERY_LIMIT_OFFSET)).append(" ");
		return query.toString();
	}
	
	/**
	 * Extracts the {@code Book} object from the {@code ResultSet}
	 * 
	 * @param rs
	 * 		  {@code ResultSet} that contains {@code Book}'s fields
	 * 
	 * @return {@code Book} object extracted from the {@code ResultSet}
	 * 
	 * @throws SQLException
	 * 
	 * @see org.example.entity.Book
	 */
	private static Book getBook(ResultSet rs) throws SQLException {
		int k = 0;
		return new Book.Builder().setId(rs.getInt(++k))
								.setAuthor(rs.getString(++k))
								.setTitle(rs.getString(++k))
								.setPublication(rs.getString(++k))
								.setPublicationYear(rs.getInt(++k))
								.setQuantity(rs.getInt(++k))
								.setAvailable(rs.getInt(++k))
								.setISBN(rs.getString(++k))
								.build();
	}
}
