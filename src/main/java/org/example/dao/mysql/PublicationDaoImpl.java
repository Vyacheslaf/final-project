package org.example.dao.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.PublicationDao;
import org.example.entity.Publication;
import org.example.exception.DaoException;

public class PublicationDaoImpl implements PublicationDao{

	private static final Logger LOG = LogManager.getLogger(PublicationDaoImpl.class);
	private static final String QUERY_SELECT_ALL_PUBLICATIONS = "select.all.publications";
	private static final String ERROR_CANNOT_GET_PUBLICATIONS = "error.cannot.get.publications";
	
	@Override
	public Publication create(Publication entity) throws DaoException {
		return null;
	}

	@Override
	public Publication find(Publication entity) throws DaoException {
		return null;
	}

	@Override
	public void update(Publication entity) throws DaoException {}

	@Override
	public void remove(Publication entity) throws DaoException {}

	@Override
	public Publication findByName(String publicationName) {
		return null;
	}

	@Override
	public List<Publication> findAllPublication() throws DaoException {
		List<Publication> publications = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(Queries.getInstance().getQuery(QUERY_SELECT_ALL_PUBLICATIONS));
			while (rs.next()) {
				publications.add(getPublication(rs));
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_PUBLICATIONS, e);
		} finally {
			DbManager.closeResources(con, stmt, rs);
		}
		return publications;
	}
	
	private static Publication getPublication(ResultSet rs) throws SQLException {
		Publication publication = new Publication();
		int k = 0;
		publication.setId(rs.getInt(++k));
		publication.setPublicationName(rs.getString(++k));
		return publication;
	}

}
