package org.example.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.example.entity.Book;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.exception.DaoException;

public interface OrderDao extends Dao<Order> {
	
	int countActiveOrders(User user, Book book) throws DaoException;
	List<Order> getUserOrders(int userId) throws DaoException;
	boolean cancelOrder(int orderId) throws DaoException;
	List<Order> getNewOrders() throws DaoException;
	void giveOrder(int orderId, LocalDateTime returnTime) throws DaoException;
	List<Order> getProcessedOrders() throws DaoException;
	void completeOrder(int id, LocalDateTime returnTime) throws DaoException;
	List<Order> getUserActualOrders(int userId) throws DaoException;
	List<Order> getUserProcessedOrders(int userId) throws DaoException;
}
