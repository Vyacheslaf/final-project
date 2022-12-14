package org.example.util;

import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exception.DaoException;
import org.example.service.OrderService;

public class FineUsersTask extends TimerTask{

	private static final Logger LOG = LogManager.getLogger(FineUsersTask.class);

	@Override
	public void run() {
		try {
			new OrderService().setFineForOverdueOrders();
		} catch (DaoException e) {
			LOG.error(e);
		}
		LOG.info("Users fined");
	}

}
