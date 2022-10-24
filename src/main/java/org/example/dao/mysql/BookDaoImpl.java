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

public class BookDaoImpl implements BookDao{

	private static final Logger LOG = LogManager.getLogger(BookDaoImpl.class);
	private static final String QUERY_COUNT_ALL_BOOKS = "select.count.all.books";
	private static final String QUERY_COUNT_BOOKS_FROM_SEARCH = "select.count.books.from.search";
	private static final String QUERY_INSERT_BOOK = "insert.book";
	private static final String QUERY_SELECT_BOOK_BY_ISBN = "select.book.by.isbn";
	private static final String QUERY_SELECT_COUNT_BOOK_ON_SUBSCRIPTION = "select.count.books.on.subscription.by.id";
	private static final String QUERY_DELETE_BOOK = "delete.book";
	private static final String QUERY_SELECT_BOOK_BY_ID = "select.book.by.id";
	private static final String QUERY_UPDATE_BOOK = "update.book";
	
	@Override
	public Book create(Book book) throws DaoException {
		return create(DbManager.getInstance().getConnection(), book);
	}
	
	static Book create(Connection con, Book book) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
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
			String message = "The book is already in the catalog";
			LOG.error(ex);
			throw new DaoException(message, ex);
		} catch (SQLException e) {
			String message = "Cannot create a book";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return book;
	}

	@Override
	public Book find(Book book) throws DaoException {
		return find(DbManager.getInstance().getConnection(), book);
	}

	static Book find(Connection con, Book book) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
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
			String message = "Cannot get data from database";
			LOG.error(message);
			throw new DaoException(message, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
	}
	
	@Override
	public void update(Book book) throws DaoException {
		update(DbManager.getInstance().getConnection(), book);
	}

	static void update(Connection con, Book book) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_BOOK_BY_ID));
			pstmt.setInt(++k, book.getId());
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				String message = "Cannot found the book";
				LOG.error(message);
				throw new DaoException(message);
			}
			Book bookFromCatalog = getBook(rs);
			int available = bookFromCatalog.getAvailable() + book.getQuantity() - bookFromCatalog.getQuantity();
			if (available < 0) {
				String message = "Cannot change quantity of the book";
				LOG.error(message);
				throw new DaoException(message);
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
			String message = "Cannot save changes for the book";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
	}

	@Override
	public void remove(Book book) throws DaoException {
		remove(DbManager.getInstance().getConnection(), book);
	}

	static void remove(Connection con, Book book) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_COUNT_BOOK_ON_SUBSCRIPTION));
			pstmt.setInt(++k, book.getId());
			rs = pstmt.executeQuery();
			if (!rs.next() || rs.getInt(k) > 0) {
				String message = "The book issued to reader, cannot delete it";
				LOG.error(message);
				throw new DaoException(message);
			}
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_DELETE_BOOK));
			pstmt.setInt(++k, book.getId());
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			DbManager.rollback(con);
			String message = "Cannot delete the book";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
	}
	
	@Override
	public List<Book> findBooks(String searchText, String orderBy,
								String orderType, int limit, int offset) throws DaoException{
		return findBooks(DbManager.getInstance().getConnection(), searchText, orderBy, orderType, limit, offset);
	}

	static List<Book> findBooks(Connection con, String searchText, String orderBy,
											String orderType, int limit, int offset) throws DaoException{
		List<Book> books = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			int k = 0;
			String query = makeQuery(searchText, orderBy, orderType);
			pstmt = con.prepareStatement(query);
			if (searchText != null && !searchText.equalsIgnoreCase("")) {
				String search = "%" + searchText + "%";
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
			String message = "Cannot get data from database";
			LOG.error(message);
			throw new DaoException(message, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return books;
	}
	
	@Override
	public int countBooks(String searchText) throws DaoException{
		return countBooks(DbManager.getInstance().getConnection(), searchText);
	}

	static int countBooks(Connection con, String searchText) throws DaoException{
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			int k = 0;
			if (searchText == null || searchText.equalsIgnoreCase("")) {
				pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_COUNT_ALL_BOOKS));
			} else {
				String search = "%" + searchText + "%";
				pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_COUNT_BOOKS_FROM_SEARCH));
				pstmt.setString(++k, search);
				pstmt.setString(++k, search);
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			String message = "Cannot get data from database";
			LOG.error(message);
			throw new DaoException(message, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return count;
	}
	
	private static String makeQuery(String searchText, String orderBy, String orderType) {
		StringBuilder query = new StringBuilder("");
		query.append(Queries.getInstance().getQuery("select.book")).append(" ");
		if (searchText != null && !searchText.equalsIgnoreCase("")) {
			query.append(Queries.getInstance().getQuery("search.text")).append(" ");
		}
		query.append(Queries.getInstance().getQuery("order.by")).append(" ");
		if (orderBy == null || orderBy.equalsIgnoreCase("")) {
			orderBy = "default";
		}
		query.append(Queries.getInstance().getQuery(orderBy)).append(" ");
		if ("desc".equalsIgnoreCase(orderType)) {
			query.append(Queries.getInstance().getQuery("order.desc")).append(" ");
		}
		query.append(Queries.getInstance().getQuery("limit.offset")).append(" ");
		return query.toString();
	}
	
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
