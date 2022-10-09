package org.example.service;

import java.util.List;

import org.example.Config;
import org.example.dao.DaoFactory;
import org.example.dao.OrderDao;
import org.example.entity.Book;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.exception.DaoException;

public class OrderService {

	public static boolean createOrder(String bookId, User user) 
														   throws DaoException {
		Book book = new Book.Builder().setId(Integer.parseInt(bookId))
									  .build();
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		OrderDao orderDao = daoFactory.getOrderDao();
		if (!isNewOrder(user, book, orderDao)) {
			return false;
		}
		Order order = new Order();
		order.setUser(user);
		order.setBook(book);
		orderDao.create(order);
		return true;
	}
	
	private static boolean isNewOrder(User user, Book book, 
									  OrderDao orderDao) throws DaoException {
		return orderDao.countActiveOrders(user, book) == 0;
	}
	
	public static List<Order> getReaderOrders(User user) throws DaoException {
		int userId = user.getId();
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		OrderDao orderDao = daoFactory.getOrderDao();
		return orderDao.getUserOrders(userId);
	}

	public static boolean cancelOrder(String orderId) throws DaoException {
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		OrderDao orderDao = daoFactory.getOrderDao();
		int id = Integer.parseInt(orderId);
		return orderDao.cancelOrder(id);
	}
}
