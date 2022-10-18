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
//	private static final String QUERY_SELECT_ALL_BOOKS_WITH_LIMITS = "select.all.books.with.limits";
//	private static final String QUERY_SELECT_BOOKS_BY_AUTHOR_TITLE = "select.books.by.author.title";
	private static final String QUERY_COUNT_ALL_BOOKS = "select.count.all.books";
	private static final String QUERY_COUNT_BOOKS_FROM_SEARCH = "select.count.books.from.search";
//	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_AUTHOR = "select.all.books.order.by.author";
//	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_TITLE = "select.all.books.order.by.title";
//	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_PUBLICATION = "select.all.books.order.by.publication";
//	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_YEAR = "select.all.books.order.by.year";
//	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_AUTHOR = "search.books.order.by.author";
//	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_TITLE = "search.books.order.by.title";
//	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_PUBLICATION = "search.books.order.by.publication";
//	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_YEAR = "search.books.order.by.year";
//	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_AUTHOR_DESC = "select.all.books.order.by.author.desc";
//	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_TITLE_DESC = "select.all.books.order.by.title.desc";
//	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_PUBLICATION_DESC = "select.all.books.order.by.publication.desc";
//	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_YEAR_DESC = "select.all.books.order.by.year.desc";
//	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_AUTHOR_DESC = "search.books.order.by.author.desc";
//	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_TITLE_DESC = "search.books.order.by.title.desc";
//	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_PUBLICATION_DESC = "search.books.order.by.publication.desc";
//	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_YEAR_DESC = "search.books.order.by.year.desc";
	private static final String QUERY_INSERT_BOOK = "insert.book";
	private static final String QUERY_SELECT_BOOK_BY_ISBN = "select.book.by.isbn";
	private static final String QUERY_SELECT_COUNT_BOOK_ON_SUBSCRIPTION = "select.count.books.on.subscription.by.id";
	private static final String QUERY_DELETE_BOOK = "delete.book";
	private static final String QUERY_SELECT_BOOK_BY_ID = "select.book.by.id";
	private static final String QUERY_UPDATE_BOOK = "update.book";
	
	@Override
	public Book create(Book book) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_INSERT_BOOK));
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
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return book;
	}

	@Override
	public Book find(Book entity) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Book book) throws DaoException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbManager.getInstance().getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_BOOK_BY_ID));
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
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_UPDATE_BOOK));
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
			DbManager.close(rs);
			DbManager.close(pstmt);
			DbManager.close(con);
		}
	}

	@Override
	public void remove(Book book) throws DaoException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = DbManager.getInstance().getConnection();
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_COUNT_BOOK_ON_SUBSCRIPTION));
			pstmt.setInt(++k, book.getId());
			rs = pstmt.executeQuery();
			if (!rs.next() || rs.getInt(k) > 0) {
				String message = "The book issued to reader, cannot delete it";
				LOG.error(message);
				throw new DaoException(message);
			}
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_DELETE_BOOK));
			pstmt.setInt(++k, book.getId());
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			DbManager.rollback(con);
			String message = "Cannot delete the book";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
			DbManager.close(con);
		}
	}

	@Override
	public List<Book> findBooks(String searchText, String orderBy, 
								String orderType, int limit, int offset) 
															throws DaoException{
		List<Book> books = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			con.setAutoCommit(true);
			int k = 0;
//			String query = selectQuery(searchText, orderBy, orderType);
			String query = makeQuery(searchText, orderBy, orderType);
			pstmt = con.prepareStatement(query);
/*			if (searchText == null || searchText == "") {
				pstmt = con.prepareStatement(
						  Queries.getQuery(QUERY_SELECT_ALL_BOOKS_WITH_LIMITS));
			} else {
				String search = "%" + searchText + "%";
				pstmt = con.prepareStatement(
						  Queries.getQuery(QUERY_SELECT_BOOKS_BY_AUTHOR_TITLE));
				pstmt.setString(++k, search);
				pstmt.setString(++k, search);
			}*/
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
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return books;
	}

	private String makeQuery(String searchText, String orderBy, String orderType) {
		StringBuilder query = new StringBuilder("");
		query.append(Queries.getQuery("select.book")).append(" ");
		if (searchText != null && !searchText.equalsIgnoreCase("")) {
			query.append(Queries.getQuery("search.text")).append(" ");
		}
		query.append(Queries.getQuery("order.by")).append(" ");
		if (orderBy == null || orderBy.equalsIgnoreCase("")) {
			orderBy = "default";
		}
		query.append(Queries.getQuery(orderBy)).append(" ");
		if ("desc".equalsIgnoreCase(orderType)) {
			query.append(Queries.getQuery("order.desc")).append(" ");
		}
		query.append(Queries.getQuery("limit.offset")).append(" ");
		return query.toString();
	}
	
//	private String selectQuery(String searchText, String orderBy, String orderType) throws SQLException {
/*	private String selectQuery(String searchText, String orderBy, String orderType) {
		String query;
		if (orderBy == null) {
			orderBy = "";
		}
		if (orderType == null || orderType.equalsIgnoreCase("asc")) {
			if (searchText == null || searchText.equalsIgnoreCase("")) {
				switch (orderBy) {
				case "author":
					query = Queries.getQuery(QUERY_SELECT_ALL_BOOKS_ORDER_BY_AUTHOR);
					break;
				case "title":
					query = Queries.getQuery(QUERY_SELECT_ALL_BOOKS_ORDER_BY_TITLE);
					break;
				case "publication":
					query = Queries.getQuery(QUERY_SELECT_ALL_BOOKS_ORDER_BY_PUBLICATION);
					break;
				case "year":
					query = Queries.getQuery(QUERY_SELECT_ALL_BOOKS_ORDER_BY_YEAR);
					break;
				default:
					query = Queries.getQuery(QUERY_SELECT_ALL_BOOKS_WITH_LIMITS);
					break;
				}
			} else {
				switch (orderBy) {
				case "author":
					query = Queries.getQuery(QUERY_SEARCH_BOOKS_ORDER_BY_AUTHOR);
					break;
				case "title":
					query = Queries.getQuery(QUERY_SEARCH_BOOKS_ORDER_BY_TITLE);
					break;
				case "publication":
					query = Queries.getQuery(QUERY_SEARCH_BOOKS_ORDER_BY_PUBLICATION);
					break;
				case "year":
					query = Queries.getQuery(QUERY_SEARCH_BOOKS_ORDER_BY_YEAR);
					break;
				default:
					query = Queries.getQuery(QUERY_SELECT_BOOKS_BY_AUTHOR_TITLE);
					break;
				}
			}
		} else {
			if (searchText == null || searchText.equalsIgnoreCase("")) {
				switch (orderBy) {
				case "author":
					query = Queries.getQuery(QUERY_SELECT_ALL_BOOKS_ORDER_BY_AUTHOR_DESC);
					break;
				case "title":
					query = Queries.getQuery(QUERY_SELECT_ALL_BOOKS_ORDER_BY_TITLE_DESC);
					break;
				case "publication":
					query = Queries.getQuery(QUERY_SELECT_ALL_BOOKS_ORDER_BY_PUBLICATION_DESC);
					break;
				case "year":
					query = Queries.getQuery(QUERY_SELECT_ALL_BOOKS_ORDER_BY_YEAR_DESC);
					break;
				default:
					query = Queries.getQuery(QUERY_SELECT_ALL_BOOKS_WITH_LIMITS);
					break;
				}
			} else {
				switch (orderBy) {
				case "author":
					query = Queries.getQuery(QUERY_SEARCH_BOOKS_ORDER_BY_AUTHOR_DESC);
					break;
				case "title":
					query = Queries.getQuery(QUERY_SEARCH_BOOKS_ORDER_BY_TITLE_DESC);
					break;
				case "publication":
					query = Queries.getQuery(QUERY_SEARCH_BOOKS_ORDER_BY_PUBLICATION_DESC);
					break;
				case "year":
					query = Queries.getQuery(QUERY_SEARCH_BOOKS_ORDER_BY_YEAR_DESC);
					break;
				default:
					query = Queries.getQuery(QUERY_SELECT_BOOKS_BY_AUTHOR_TITLE);
					break;
				}
			}
		}
		return query;
	}*/

	@Override
	public int countBooks(String searchText) throws DaoException{
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try (Connection con = DbManager.getInstance().getConnection()) {
			con.setAutoCommit(true);
			int k = 0;
			if (searchText == null || searchText == "") {
				pstmt = con.prepareStatement(
						  Queries.getQuery(QUERY_COUNT_ALL_BOOKS));
			} else {
				String search = "%" + searchText + "%";
				pstmt = con.prepareStatement(
						  Queries.getQuery(QUERY_COUNT_BOOKS_FROM_SEARCH));
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
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return count;
	}
	
	private Book getBook(ResultSet rs) throws SQLException {
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

	@Override
	public Book findByISBN(String isbn) throws DaoException {
		Book book = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try (Connection con = DbManager.getInstance().getConnection()) {
			con.setAutoCommit(true);
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_BOOK_BY_ISBN));
			pstmt.setString(++k, isbn);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				book = getBook(rs);
			}
		} catch (SQLException e) {
			String message = "Cannot get data from database";
			LOG.error(message);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return book;
	}
}
