package org.example.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Config;
import org.example.exception.DaoException;

public class DbManager {
	private static final Logger LOG = LogManager.getLogger(DbManager.class);
	private static final String INIT_CONTEXT_NAME = "java:/comp/env";
	private static final String GET_DATASOURCE_ERROR_MESSAGE = "Cannot get a datasource";
	private static final String GET_CONNECTION_ERROR_MESSAGE = "Cannot get connection";
	private static final String CLOSE_RESULT_SET_ERROR_MESSAGE = "Cannot close ResultSet";
	private static final String CLOSE_STATEMENT_ERROR_MESSAGE = "Cannot close Statement";
	private static final String CLOSE_CONNECTION_ERROR_MESSAGE = "Cannot close connection";
	private static final String ROLLBACK_ERROR_MESSAGE = "Cannot rollback transaction";
	private static DbManager instance;
	
	public static synchronized DbManager getInstance() throws DaoException{
		if (instance == null) {
			instance = new DbManager();
		}
		return instance;
	}

	private final DataSource dataSource;

	private DbManager() throws DaoException {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup(INIT_CONTEXT_NAME);
			dataSource = (DataSource)envContext
										  .lookup(Config.MYSQL_DATASOURCE_NAME);
		} catch (NamingException e) {
			LOG.error(GET_DATASOURCE_ERROR_MESSAGE);
			throw new DaoException(GET_DATASOURCE_ERROR_MESSAGE, e);
		}
	}

	public Connection getConnection() throws DaoException {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			LOG.error(GET_CONNECTION_ERROR_MESSAGE);
			throw new DaoException(GET_CONNECTION_ERROR_MESSAGE, e);
		}
	}
	
	public static void close(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				LOG.error(CLOSE_STATEMENT_ERROR_MESSAGE);
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				LOG.error(CLOSE_RESULT_SET_ERROR_MESSAGE);
			}
		}
	}

	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				LOG.error(CLOSE_STATEMENT_ERROR_MESSAGE);
			}
		}
	}

	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error(CLOSE_CONNECTION_ERROR_MESSAGE);
			}
		}
	}

	public static void rollback(Connection con) {
		try {
			con.rollback();
		} catch (SQLException e) {
			LOG.error(ROLLBACK_ERROR_MESSAGE);
		}
	}
}
