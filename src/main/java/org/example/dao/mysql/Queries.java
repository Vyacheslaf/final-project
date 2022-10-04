package org.example.dao.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Queries {
	
	private static final Logger LOG = LogManager.getLogger(Queries.class);
	private static final String CANNOT_FIND_ERROR_MESSAGE 
												= "Cannot find property file: ";
	private static final String CANNOT_LOAD_ERROR_MESSAGE 
											= "Unable to load property file: ";
    private static final String propFileName = "queries.properties";
    private static Properties properties;

    private static synchronized Properties getQueries() throws SQLException {
		try (InputStream inputStream = Queries.class.getClassLoader()
										.getResourceAsStream(propFileName)) {
			if (inputStream == null) {
				LOG.error(CANNOT_FIND_ERROR_MESSAGE);
				throw new SQLException(CANNOT_FIND_ERROR_MESSAGE 
															+ propFileName);
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
    }

    public static String getQuery(String query) throws SQLException{
        return getQueries().getProperty(query);
    }

}
