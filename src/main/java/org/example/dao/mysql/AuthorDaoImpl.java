package org.example.dao.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.AuthorDao;
import org.example.entity.Author;
import org.example.exception.DaoException;

public class AuthorDaoImpl implements AuthorDao{

	private static final Logger LOG = LogManager.getLogger(AuthorDaoImpl.class);
	private static final String QUERY_SELECT_ALL_AUTHORS = "select.all.authors";
	private static final String ERROR_CANNOT_GET_AUTHORS = "error.cannot.get.authors";
	
	@Override
	public Author create(Author entity) throws DaoException {
		return null;
	}

	@Override
	public Author find(Author entity) throws DaoException {
		return null;
	}

	@Override
	public void update(Author entity) throws DaoException {
	}

	@Override
	public void remove(Author entity) throws DaoException {
	}

	@Override
	public List<Author> findAllAuthor() throws DaoException {
		List<Author> authors = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(Queries.getInstance().getQuery(QUERY_SELECT_ALL_AUTHORS));
			while (rs.next()) {
				authors.add(getAuthor(rs));
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_AUTHORS, e);
		} finally {
			DbManager.closeResources(con, stmt, rs);
		}
		return authors;
	}

	private static Author getAuthor(ResultSet rs) throws SQLException {
		Author author = new Author();
		int k = 0;
		author.setId(rs.getInt(++k));
		author.setFullName(rs.getString(++k));
		return author;
	}
}
