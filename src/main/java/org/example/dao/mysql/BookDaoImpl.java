package org.example.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.BookDao;
import org.example.entity.Book;
import org.example.exception.DaoException;

public class BookDaoImpl implements BookDao{

	private static final Logger LOG = LogManager.getLogger(BookDaoImpl.class);
	private static final String QUERY_SELECT_ALL_BOOKS_WITH_LIMITS 
											= "select.all.books.with.limits";
	private static final String QUERY_SELECT_BOOKS_BY_AUTHOR_TITLE 
											= "select.books.by.author.title";
	private static final String QUERY_COUNT_ALL_BOOKS 
											= "select.count.all.books";
	private static final String QUERY_COUNT_BOOKS_FROM_SEARCH 
											= "select.count.books.from.search";
	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_AUTHOR 
										= "select.all.books.order.by.author";
	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_TITLE 
										= "select.all.books.order.by.title";
	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_PUBLICATION 
									= "select.all.books.order.by.publication";
	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_YEAR 
										= "select.all.books.order.by.year";
	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_AUTHOR 
											= "search.books.order.by.author";
	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_TITLE 
											= "search.books.order.by.title";
	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_PUBLICATION 
										= "search.books.order.by.publication";
	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_YEAR 
											= "search.books.order.by.year";
	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_AUTHOR_DESC 
									= "select.all.books.order.by.author.desc";
	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_TITLE_DESC 
									= "select.all.books.order.by.title.desc";
	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_PUBLICATION_DESC 
								= "select.all.books.order.by.publication.desc";
	private static final String QUERY_SELECT_ALL_BOOKS_ORDER_BY_YEAR_DESC 
										= "select.all.books.order.by.year.desc";
	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_AUTHOR_DESC 
										= "search.books.order.by.author.desc";
	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_TITLE_DESC 
										= "search.books.order.by.title.desc";
	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_PUBLICATION_DESC 
									= "search.books.order.by.publication.desc";
	private static final String QUERY_SEARCH_BOOKS_ORDER_BY_YEAR_DESC 
										= "search.books.order.by.year.desc";
	
	@Override
	public Book create(Book entity) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Book find(Book entity) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Book entity) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Book entity) throws DaoException {
		// TODO Auto-generated method stub
		
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
			String query = selectQuery(searchText, orderBy, orderType);
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

	private String selectQuery(String searchText, String orderBy, 
										String orderType) throws SQLException {
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
	}

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
								.build();
	}
}
