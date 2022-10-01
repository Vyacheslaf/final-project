package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
	private static final Logger LOG = LogManager.getLogger(Config.class);
	public static final String DAO_NAME;
	public static final String MYSQL_DATASOURCE_NAME;
	
	static {
		Properties prop = new Properties();
		try (InputStream inputStream = Config.class.getClassLoader()
									.getResourceAsStream("config.properties")) {
			if (inputStream == null) {
				LOG.error("Cannot find config.properties");
				throw new IOException();
			}
			prop.load(inputStream);
			DAO_NAME = prop.getProperty("dao.name");
			MYSQL_DATASOURCE_NAME = prop.getProperty("mysql.datasource.name");
		} catch (IOException e) {
			LOG.error("Cannot load a config file");
			throw new RuntimeException(e);
		}
	}
}