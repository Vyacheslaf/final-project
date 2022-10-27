package org.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.example.util.Config;
//import org.example.dao.DaoFactory;
import org.example.dao.OrderDao;
import org.example.entity.Book;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.exception.DaoException;

import static org.example.util.Config.*;

public class OrderService {

	public static boolean createOrder(String bookId, User user) throws DaoException {
		Book book = new Book.Builder().setId(Integer.parseInt(bookId)).build();
		OrderDao orderDao = DAO_FACTORY.getOrderDao();
		if (!isNewOrder(user, book, orderDao)) {
			return false;
		}
		Order order = new Order();
		order.setUser(user);
		order.setBook(book);
		orderDao.create(order);
		return true;
	}
	
	private static boolean isNewOrder(User user, Book book, OrderDao orderDao) throws DaoException {
		return orderDao.countActiveOrders(user, book) == 0;
	}
	
	public static List<Order> getReaderOrders(User user) throws DaoException {
		int userId = user.getId();
		OrderDao orderDao = DAO_FACTORY.getOrderDao();
		return orderDao.getUserOrders(userId);
	}

	public static boolean cancelOrder(String orderId) throws DaoException {
		OrderDao orderDao = DAO_FACTORY.getOrderDao();
		int id = Integer.parseInt(orderId);
		return orderDao.cancelOrder(id);
	}

	public static List<Order> getNewOrders() throws DaoException {
		OrderDao orderDao = DAO_FACTORY.getOrderDao();
		return orderDao.getNewOrders();
	}

	public static Order findOrder(int orderId) throws DaoException {
		Order order = new Order();
		order.setId(orderId);
		OrderDao orderDao = DAO_FACTORY.getOrderDao();
		return orderDao.find(order);
	}

	public static void giveOrder(String orderId, boolean onSubscription) throws DaoException {
		LocalDateTime returnTime;
		if (onSubscription) {
			returnTime = LocalDate.now().atStartOfDay().plusDays(Config.DAY_ON_SUBSCRIPTION + 1).minusSeconds(1);
		} else {
			returnTime = LocalDate.now().plusDays(1).atStartOfDay().minusSeconds(1);
		}
		int id = Integer.parseInt(orderId);
		OrderDao orderDao = DAO_FACTORY.getOrderDao();
		orderDao.giveOrder(id, returnTime);
	}

	public static List<Order> getProcessedOrders() throws DaoException {
		OrderDao orderDao = DAO_FACTORY.getOrderDao();
		return orderDao.getProcessedOrders();
	}

	public static void completeOrder(String orderId) throws DaoException {
		LocalDateTime returnTime = LocalDateTime.now();
		int id = Integer.parseInt(orderId);
		OrderDao orderDao = DAO_FACTORY.getOrderDao();
		orderDao.completeOrder(id, returnTime);
	}

	public static List<Order> getReaderActualOrders(User user) throws DaoException {
		int userId = user.getId();
		OrderDao orderDao = DAO_FACTORY.getOrderDao();
		return orderDao.getUserActualOrders(userId);
	}

	public static List<Order> getReaderProcessedOrders(User user) throws DaoException {
		int userId = user.getId();
		OrderDao orderDao = DAO_FACTORY.getOrderDao();
		return orderDao.getUserProcessedOrders(userId);
	}

	public static void setFineForOverdueOrders() throws DaoException {
		LocalDateTime ldt = LocalDate.now(ZONE_ID).atStartOfDay();
		OrderDao orderDao = DAO_FACTORY.getOrderDao();
		List<Integer> overdueOrdersIds = orderDao.getOverdueOrdersIds(ldt);
		for (Integer id : overdueOrdersIds) {
			orderDao.setFine(id.intValue(), FINE);
		}
	}
}
