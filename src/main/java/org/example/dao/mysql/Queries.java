package org.example.dao.mysql;

import java.io.IOException;
import java.io.InputStream;
//import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Queries {

	private static final Logger LOG = LogManager.getLogger(Queries.class);
	private static final String CANNOT_FIND_ERROR_MESSAGE = "Cannot find property file";
	private static final String CANNOT_LOAD_ERROR_MESSAGE = "Unable to load property file";
	private static final String QUERIES_FILENAME = "queries.properties";
	private static Properties properties;
	private static Queries instance;

	public static synchronized Queries getInstance() {
		if (instance == null) {
			instance = new Queries();
		}
		return instance;
	}

	private Queries() {
		try (InputStream inputStream = Queries.class.getClassLoader().getResourceAsStream(QUERIES_FILENAME)) {
			if (inputStream == null) {
				LOG.error(CANNOT_FIND_ERROR_MESSAGE);
				throw new IOException(CANNOT_FIND_ERROR_MESSAGE);
			}
			properties = new Properties();
			properties.load(inputStream);
		} catch (IOException e) {
			LOG.error(CANNOT_LOAD_ERROR_MESSAGE);
			throw new RuntimeException(e);
		}
	}

/*	private static synchronized Properties getQueries() throws SQLException {
		try (InputStream inputStream = Queries.class.getClassLoader().getResourceAsStream(propFileName)) {
			if (inputStream == null) {
				LOG.error(CANNOT_FIND_ERROR_MESSAGE);
				throw new SQLException(CANNOT_FIND_ERROR_MESSAGE + propFileName);
			}
			if (properties == null) {
				properties = new Properties();
			}
			properties.load(inputStream);
		} catch (IOException e) {
			LOG.error(CANNOT_LOAD_ERROR_MESSAGE);
			throw new SQLException(CANNOT_LOAD_ERROR_MESSAGE + propFileName, e);
		}
		return properties;
	}*/

//	public static String getQuery(String query) throws SQLException {
	public static String getQuery(String query) {
//		return getQueries().getProperty(query);
		return properties.getProperty(query);
	}

}
