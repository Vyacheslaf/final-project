package org.example.dao;

import java.util.List;

import org.example.entity.Book;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.exception.DaoException;

public interface OrderDao extends Dao<Order> {
	
	int countActiveOrders(User user, Book book) throws DaoException;
	List<Order> getUserOrders(int userId) throws DaoException;
	boolean cancelOrder(int orderId) throws DaoException;
}
