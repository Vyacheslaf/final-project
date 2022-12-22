package org.example.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.example.entity.Book;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.exception.DaoException;

/**
 * The {@code BookDao} interface declares additional methods to operate with {@code Order} objects, 
 * beyond those CRUD methods specified in the {@code Dao} interface.
 * 
 * @author Vyacheslav Fedchenko
 * @see org.example.dao.Dao
 */

public interface OrderDao extends Dao<Order> {
	
	/**
	 * Returns quantity of new or processed orders of selected book for selected user
	 * 
	 * @param user
	 * 		  Selected {@code User} 
	 * @param book
	 * 		  Selected {@code Book}
	 * @return quantity of new or processed orders of selected book for selected user
	 * @throws DaoException if cannot get quantity of orders
	 * @see User
	 * @see Book
	 */
	int countActiveOrders(User user, Book book) throws DaoException;
	
	/**
	 * Returns the list of all orders for the user with selected ID
	 * 
	 * @param userId
	 * 		  Selected user's id
	 * @return the list of all orders for the user with selected ID
	 * @throws DaoException if cannot get list of orders
	 * @see User
	 */
	List<Order> getUserOrders(int userId) throws DaoException;
	
	/**
	 * Cancels new order with selected ID
	 * @param orderId
	 * 		  Selected order's ID
	 * @return {@code true} if the order has been canceled, 
	 * 		   {@code false} if the order's state is not {@code NEW}
	 * @throws DaoException if cannot cancel the order
	 * @see Order
	 */
	boolean cancelOrder(int orderId) throws DaoException;
	
	/**
	 * Returns the list of all new orders
	 * 
	 * @return the list of all new orders
	 * @throws DaoException if cannot get list of new orders
	 */
	List<Order> getNewOrders() throws DaoException;
	
	/**
	 * Changes order's state to {@code PROCESSED}, decrease available quantity of books from this order 
	 * and set deadline return time for this order
	 * 
	 * @param orderId
	 * 		  Selected order's ID
	 * @param returnTime
	 * 		  Deadline return time for selected order
	 * @throws DaoException if cannot change order's state, available quantity of books or return time
	 * @see Order
	 */
	void giveOrder(int orderId, LocalDateTime returnTime) throws DaoException;
	
	/**
	 * Returns the list of all processed orders
	 * 
	 * @return the list of all processed orders
	 * @throws DaoException if cannot get list of processed orders
	 */
	List<Order> getProcessedOrders() throws DaoException;
	
	/**
	 * Changes order's state to {@code COMPLETED}, increase available quantity of books from this order 
	 * and save return time of the order
	 * 
	 * @param orderId
	 * 		  Selected order's ID
	 * @param returnTime
	 * 		  Order return time
	 * @throws DaoException if cannot change order's state, available quantity of books or return time
	 * @see Order
	 */
	void completeOrder(int orderId, LocalDateTime returnTime) throws DaoException;
	
	/**
	 * Returns the list of all new and processed orders for the user with selected ID
	 * 
	 * @param userId
	 * 		  Selected user's ID
	 * @return the list of all new and processed orders for the user with selected ID
	 * @throws DaoException if cannot get list of orders
	 * @see User
	 */
	List<Order> getUserActualOrders(int userId) throws DaoException;
	
	/**
	 * Returns the list of all processed orders for the user with selected ID
	 * 
	 * @param userId
	 * 		  Selected user's ID
	 * @return the list of all processed orders for the user with selected ID
	 * @throws DaoException if cannot get list of orders
	 * @see User
	 */
	List<Order> getUserProcessedOrders(int userId) throws DaoException;
	
	/**
	 * Returns the list of orders' IDs whose return time has passed
	 * 
	 * @param ldt
	 * 		  Current time
	 * @return the list of orders' IDs whose return time has passed
	 * @throws DaoException if cannot get list of orders
	 */
	List<Integer> getOverdueOrdersIds(LocalDateTime ldt) throws DaoException;
	
	/**
	 * Sets a penalty for the order with selected ID
	 * 
	 * @param orderId
	 * 		  Selected order's ID
	 * @param fine
	 * 		  Amount of the fine
	 * @throws DaoException if cannot sets a penalty for the order
	 */
	void setFine(int orderId, int fine) throws DaoException;
}
