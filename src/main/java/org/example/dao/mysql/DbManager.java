package org.example.dao.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.DaoException;

/**
 * The {@code DbManager} class manages connection to the MySQL DBMS.
 * 
 * @author Vyacheslav Fedchenko
 *
 */

public class DbManager {

	/**
	 * The Log4j Logger
	 */
	private static final Logger LOG = LogManager.getLogger(DbManager.class);
	
	/**
	 * The {@code String} that defines the name of init context.
	 */
	private static final String INIT_CONTEXT_NAME = "java:/comp/env";

	/**
	 * The {@code String} specifying the error message if the datasource cannot be obtained.
	 */
	private static final String GET_DATASOURCE_ERROR_MESSAGE = "Cannot get a datasource";

	/**
	 * The {@code String} specifying the error message if the connection cannot be obtained.
	 */
	private static final String GET_CONNECTION_ERROR_MESSAGE = "Cannot get a connection";

	/**
	 * The {@code String} specifying the error message if the {@code ResultSet} cannot be closed.
	 */
	private static final String CLOSE_RESULT_SET_ERROR_MESSAGE = "Cannot close ResultSet";

	/**
	 * The {@code String} specifying the error message if the {@code Statement} cannot be closed.
	 */
	private static final String CLOSE_STATEMENT_ERROR_MESSAGE = "Cannot close Statement";

	/**
	 * The {@code String} specifying the error message if the {@code Connection} cannot be closed.
	 */
	private static final String CLOSE_CONNECTION_ERROR_MESSAGE = "Cannot close Connection";

	/**
	 * The {@code String} specifying the error message if cannot rollback the transaction.
	 */
	private static final String ROLLBACK_ERROR_MESSAGE = "Cannot rollback transaction";

	/**
	 * The {@code String} specifying the error message if the property file cannot be found.
	 */
	private static final String CANNOT_FIND_ERROR_MESSAGE = "Cannot find property file";

	/**
	 * The {@code String} specifying the error message if the property file cannot be loaded.
	 */
	private static final String CANNOT_LOAD_ERROR_MESSAGE = "Unable to load property file";

	/**
	 * The {@code String} specifying the name of MySQL properties file.
	 */
	private static final String PROPERTIES_FILENAME = "mysql.properties";
	
	/**
	 * The key for getting the name of datasource from the MySQL properties file.
	 */
	private static final String PROPERTY_DATASOURCE = "datasource";
	
	/**
	 * An instance of {@code DbManager}.
	 */
	private static DbManager instance;
	
	/**
	 * Creates an instance of {@code DbManager} if it is null and returns it.
	 * @return an instance of {@code DbManager}
	 */
	public static synchronized DbManager getInstance() {
		if (instance == null) {
			instance = new DbManager();
		}
		return instance;
	}

	private final DataSource dataSource;

	/**
	 * Initializes a newly created object of {@code DbManager}
	 */
	private DbManager() {
		try (InputStream inputStream = DbManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME)) {
			if (inputStream == null) {
				LOG.error(CANNOT_FIND_ERROR_MESSAGE);
				throw new IOException();
			}
			Properties prop = new Properties();
			prop.load(inputStream);
			String datasourceName = prop.getProperty(PROPERTY_DATASOURCE);
			Context initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup(INIT_CONTEXT_NAME);
			dataSource = (DataSource)envContext.lookup(datasourceName);
		} catch (NamingException e) {
			LOG.error(GET_DATASOURCE_ERROR_MESSAGE);
			throw new RuntimeException(e);
		} catch (IOException ex) {
			LOG.error(CANNOT_LOAD_ERROR_MESSAGE);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Attempts to establish a connection with the database.
	 * @return a connection from a connections pool
	 * @throws DaoException if a database access error occurs or the timeout has been exceeded
	 */
	public Connection getConnection() throws DaoException {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			LOG.error(GET_CONNECTION_ERROR_MESSAGE);
			throw new DaoException(GET_CONNECTION_ERROR_MESSAGE, e);
		}
	}
	
	/**
	 * Attempts to close {@code ResultSet} if it is not null
	 * @param rs 
	 * 		  {@code ResultSet} to be closed
	 * 
	 * @see java.sql.ResultSet
	 */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				LOG.error(CLOSE_RESULT_SET_ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Attempts to close {@code Statement} if it is not null
	 * @param stmt 
	 * 		  {@code Statement} to be closed
	 * 
	 * @see java.sql.Statement
	 */
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				LOG.error(CLOSE_STATEMENT_ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Attempts to close {@code Connection} if it is not null
	 * @param con 
	 * 		  {@code Connection} to be closed
	 * 
	 * @see java.sql.Connection
	 */
	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error(CLOSE_CONNECTION_ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Attempts to undo all changes made in the current transaction 
	 * and releases any database locks currently held by {@code Connection} object.
	 * @param con
	 * 		  {@code Connection} object.
	 */
	public static void rollback(Connection con) {
		try {
			con.rollback();
		} catch (SQLException e) {
			LOG.error(ROLLBACK_ERROR_MESSAGE);
		}
	}
	
	/**
	 * Closes {@code ResultSet} first, then closes {@code Statement} and then closes {@code Connection}.
	 * @param con
	 * 		  {@code Connection} to be closed.
	 * @param stmt
	 * 		  {@code Statement} to be closed.
	 * @param rs
	 * 		  {@code ResultSet} to be closed.
	 */
	public static void closeResources(Connection con, Statement stmt, ResultSet rs) {
		close(rs);
		close(stmt);
		close(con);
	}

	/**
	 * Closes {@code Statement} first and then closes {@code Connection}.
	 * @param con
	 * 		  {@code Connection} to be closed.
	 * @param stmt
	 * 		  {@code Statement} to be closed.
	 */
	public static void closeResources(Connection con, Statement stmt) {
		close(stmt);
		close(con);
	}
}
