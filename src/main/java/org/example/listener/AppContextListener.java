package org.example.listener;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.DaoException;
import org.example.service.OrderService;
import org.example.util.Config;
import org.example.util.FineUsersTask;

/**
 * The {@code InitServletListener} class is used to configure web application
 * 
 * @author Vyacheslav Fedchenko
 */

@WebListener
public class AppContextListener implements ServletContextListener {
	
	private static final Logger LOG = LogManager.getLogger(AppContextListener.class);
	private static final Timer PENALTY_TIMER = new Timer();
	private static final String LOCALES_PATH = "i18n/locales.properties";
	private static final String CANNOT_FIND_ERROR_MESSAGE = "Cannot find property file";
	private static final String CANNOT_LOAD_ERROR_MESSAGE = "Unable to load property file";
	private static final String CONTEXT_ATTR_LOCALES = "locales";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		setLocales(context);
		initPenaltyTimer();
		LOG.info("Servlet at " + context.getRealPath("") + " started");
		try {
			new OrderService().setFineForOverdueOrders();
		} catch (DaoException e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Initializes user penalty timer to run it everyday at 00:01:00
	 */
	private void initPenaltyTimer() {
		LocalDateTime tomorrow = LocalDate.now().atStartOfDay().plusDays(1).plusMinutes(1);
		ZonedDateTime zdt = tomorrow.atZone(Config.ZONE_ID);
		Date tomorrowDate = Date.from(zdt.toInstant());
		PENALTY_TIMER.schedule(new FineUsersTask(), tomorrowDate, TimeUnit.DAYS.toMillis(1));
		LOG.info("Penalty timer started");
	}

	/**
	 * Creates the attribute with currently available locales in the servlet context
	 * 
	 * @param context
	 * 		  The servlet context
	 */
	private void setLocales(ServletContext context) {
		try (InputStream is = AppContextListener.class.getClassLoader().getResourceAsStream(LOCALES_PATH)) {
			if (is == null) {
				LOG.error(CANNOT_FIND_ERROR_MESSAGE);
				throw new IOException();
			}
			Properties locales = new Properties();
			locales.load(is);
			context.setAttribute(CONTEXT_ATTR_LOCALES, locales);
		} catch (IOException e) {
			LOG.error(CANNOT_LOAD_ERROR_MESSAGE);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @implNote Cancels user penalty timer at destroy servlet context
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContextListener.super.contextDestroyed(sce);
		PENALTY_TIMER.cancel();
	}

}
