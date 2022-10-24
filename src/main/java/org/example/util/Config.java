package org.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.DaoFactory;

public class Config {

	public static final String DBMS;
	public static final int LIMIT_BOOKS_ON_PAGE;
	public static final int DAY_ON_SUBSCRIPTION;
	public static final ZoneId ZONE_ID;
	public static final DaoFactory DAO_FACTORY;
	public static final int FINE;
	private static final Logger LOG = LogManager.getLogger(Config.class);
	
	static {
		try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (inputStream == null) {
				LOG.error("Cannot find config.properties");
				throw new IOException();
			}
			Properties prop = new Properties();
			prop.load(inputStream);
			DBMS = prop.getProperty("dbms");
			DAO_FACTORY = DaoFactory.getDaoFactory(DBMS);
			LIMIT_BOOKS_ON_PAGE = Integer.parseInt(prop.getProperty("limit.books.on.page"));
			DAY_ON_SUBSCRIPTION = Integer.parseInt(prop.getProperty("count.days.on.subscription"));
			ZONE_ID = ZoneId.of(prop.getProperty("time.zone.id"));
			FINE = Integer.parseInt(prop.getProperty("fine"));
		} catch (IOException e) {
			LOG.error("Cannot load a config file");
			throw new RuntimeException(e);
		}
	}
}