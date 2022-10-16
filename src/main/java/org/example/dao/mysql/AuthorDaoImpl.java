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
	
	@Override
	public Author create(Author entity) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Author find(Author entity) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Author entity) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Author entity) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Author> findAllAuthor() throws DaoException {
		List<Author> authors = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			stmt = con.createStatement();
			rs = stmt.executeQuery(Queries.getQuery(QUERY_SELECT_ALL_AUTHORS));
			while (rs.next()) {
				authors.add(getAuthor(rs));
			}
		} catch (SQLException e) {
			String message = "Cannot get list of authors";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(stmt);
		}
		return authors;
	}

	private Author getAuthor(ResultSet rs) throws SQLException {
		Author author = new Author();
		int k = 0;
		author.setId(rs.getInt(++k));
		author.setFullName(rs.getString(++k));
		return author;
	}

}
