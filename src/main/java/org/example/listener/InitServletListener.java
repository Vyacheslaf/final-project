package org.example.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Config;

@WebListener
public class InitServletListener implements ServletContextListener{
	private static final Logger LOG 
							= LogManager.getLogger(InitServletListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		LOG.info("Servlet at " + context.getRealPath("") + " started");
		LOG.info("Selected DAO is " + Config.DAO_NAME);
	}

}
