package org.example.dao.mysql;

import java.io.IOException;
import java.io.InputStream;
//import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code Queries} class is used to get SQL queries from properties file
 * 
 * @author Vyacheslav Fedchenko
 *
 */

public class Queries {

	private static final Logger LOG = LogManager.getLogger(Queries.class);
	private static final String CANNOT_FIND_ERROR_MESSAGE = "Cannot find property file";
	private static final String CANNOT_LOAD_ERROR_MESSAGE = "Unable to load property file";
	private static final String QUERIES_FILENAME = "queries.properties";
	private static Properties properties;
	private static Queries instance;

	/**
	 * Creates an instance of {@code Queries} if it is null and returns it.
	 * @return an instance of {@code Queries}
	 */
	public static synchronized Queries getInstance() {
		if (instance == null) {
			instance = new Queries();
		}
		return instance;
	}

	/**
	 * Load SQL queries from properties file and initializes a newly created object of {@code Queries}
	 */
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

	/**
	 * Searches for the query with the specified key in property list.
	 * The method returns {@code null} if the property is not found.
	 * @param key
	 * 		  the query key
	 * @return the query in property list with the specified key value
	 */
	public String getQuery(String key) {
		return properties.getProperty(key);
	}

}
