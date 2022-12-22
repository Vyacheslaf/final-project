package org.example.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.example.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.Constants;
import org.example.dao.OrderDao;
import org.example.entity.Book;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;
import org.example.exception.ServiceException;

/**
 * The {@code OrderService} class is used to provide interaction between controllers and {@code OrderDao}
 * 
 * @author Vyacheslav Fedchenko
 */

public class OrderService {
	
	private static final Logger LOG = LogManager.getLogger(OrderService.class);
	private static final String REQ_PARAM_ORDER_ID = "orderid";
	private static final String REQ_PARAM_READER_ID = "readerid";
	private static final String REQ_PARAM_BOOK_ID = "bookid";
	private static final String REQ_PARAM_ON_SUBSCRIPTION = "onsubscription";
	private static final String ONLY_DIGITS_REGEX = "\\d+";
	private static final String ERROR_WRONG_ORDER_ID = "error.wrong.order.id";
	private static final String ERROR_WRONG_READER_ID = "error.wrong.reader.id";
	private static final String ERROR_WRONG_BOOK_ID = "error.wrong.book.id";
	private static final String ERROR_WRONG_USER = "error.wrong.user";

	private final OrderDao orderDao;
	
    /**
     * Initializes a newly created {@code OrderService} object with {@code OrderDao} argument
     */
	public OrderService(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	
    /**
     * Initializes a newly created {@code OrderService} object with {@code OrderDao} 
     * produced by configured {@code DaoFactory}
     */
	public OrderService() {
		this(Config.DAO_FACTORY.getOrderDao());
	}

	/**
	 * Attempts to create a new order for the selected <i>user</i> and the book with the selected <i>ID</i>
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing book's ID and the {@code User} creating an order
	 * @return {@code false} if the book is already in active orders of user, 
	 * 		   {@code true} if an order was created
	 * @throws DaoException if cannot create a new order
	 * @throws ServiceException if cannot get book's ID or the {@code User} from the {@code HttpServletRequest} 
	 */
	public boolean createOrder(HttpServletRequest req) throws DaoException, ServiceException {
		User reader = getCurrentReader(req);
		int bookId = getBookId(req);
		Book book = new Book.Builder().setId(bookId).build();
		if (!isNewOrder(reader, book)) {
			return false;
		}
		Order order = new Order();
		order.setUser(reader);
		order.setBook(book);
		orderDao.create(order);
		return true;
	}
	
	/**
	 * Extracts the book's ID from the {@code HttpServletRequest}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing book's ID
	 * @return the book's ID
	 * @throws ServiceException if the {@code HttpServletRequest} has no book's ID or if the book's ID is not a number
	 */
	private static int getBookId(HttpServletRequest req) throws ServiceException {
		String bookId = req.getParameter(REQ_PARAM_BOOK_ID);
		if ((bookId == null) ||!bookId.matches(ONLY_DIGITS_REGEX)) {
			logAndThrowException(ERROR_WRONG_BOOK_ID);
		}
		return Integer.parseInt(bookId);
	}

	/**
	 * Extracts the {@code User} with role <i>READER</i> from current session
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing the {@code HttpSession} with current {@code User}
	 * @return the {@code User} with role <i>READER</i> from current session
	 * @throws ServiceException if current {@code HttpSession} has no {@code User} 
	 * 		   or if {@code UserRole} of the {@code User} is not <i>READER</i>
	 */
	private static User getCurrentReader(HttpServletRequest req) throws ServiceException {
		User user = (User) req.getSession().getAttribute(Constants.SESSION_ATTRIBUTE_USER);
		if (user == null || !user.getRole().equals(UserRole.READER)) {
			logAndThrowException(ERROR_WRONG_USER);
		}
		return user;
	}

	/**
	 * Checks if there is a book in the user's active orders
	 * 
	 * @param user
	 * 		  The selected {@code User}
	 * @param book
	 * 		  The selected {@code Book}
	 * @return {@code true} if the user has no selected book in active orders
	 * @throws DaoException if cannot check user's orders
	 */
	private boolean isNewOrder(User user, Book book) throws DaoException {
		return orderDao.countActiveOrders(user, book) == 0;
	}
	
	/**
	 * Returns a list of all orders for the user
	 * 
	 * @param user
	 * 		  The selected {@code User}
	 * @return the list of all orders for the user
	 * @throws DaoException if cannot get the list of orders
	 */
	public List<Order> getReaderOrders(User user) throws DaoException {
		int userId = user.getId();
		return orderDao.getUserOrders(userId);
	}

	/**
	 * Cancels the order with selected ID
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing order's ID
	 * @return {@code true} if the order has been canceled, {@code false} if the order's state is not {@code NEW}
	 * @throws DaoException if cannot cancel the order
	 * @throws ServiceException if cannot get order's ID from {@code HttpServletRequest}
	 */
	public boolean cancelOrder(HttpServletRequest req) throws DaoException, ServiceException {
		int orderId = getOrderId(req);
		return orderDao.cancelOrder(orderId);
	}

	/**
	 * Returns the list of all new orders
	 * 
	 * @return the list of all new orders
	 * @throws DaoException if cannot get the list of orders
	 */
	public List<Order> getNewOrders() throws DaoException {
		return orderDao.getNewOrders();
	}

	/**
	 * Searches for the order with selected ID
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing order's ID
	 * @return {@code Order} with selected ID
	 * @throws DaoException if cannot find an Order
	 * @throws ServiceException if cannot get order's ID from the {@code HttpServletRequest}
	 */
	public Order findOrder(HttpServletRequest req) throws DaoException, ServiceException {
		int orderId = getOrderId(req);
		Order order = new Order();
		order.setId(orderId);
		return orderDao.find(order);
	}

	/**
	 * Extracts the order's ID from the {@code HttpServletRequest}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing order's ID
	 * @return the order's ID
	 * @throws ServiceException if the {@code HttpServletRequest} has no order's ID or if the order's ID is not a number
	 */
	private static int getOrderId(HttpServletRequest req) throws ServiceException {
		String orderId = req.getParameter(REQ_PARAM_ORDER_ID);
		if ((orderId == null) ||!orderId.matches(ONLY_DIGITS_REGEX)) {
			LOG.error(ERROR_WRONG_ORDER_ID);
			throw new ServiceException(ERROR_WRONG_ORDER_ID);
		}
		return Integer.parseInt(orderId);
	}

	/**
	 * Attempts to give the book from the order to the reader.
	 * The book gives on a subscription only if the {@code HttpServletRequest} contains the parameter
	 * <i>onsubscription</i> that equals <i>true</i>
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing order's ID
	 * @throws DaoException if cannot give the book from the order
	 * @throws ServiceException if cannot get order's ID from the {@code HttpServletRequest}
	 */
	public void giveOrder(HttpServletRequest req) throws DaoException, ServiceException {
		LocalDateTime returnTime = LocalDate.now(Config.ZONE_ID).plusDays(1).atStartOfDay().minusSeconds(1);
		if (isOnSubscription(req)) {
			returnTime = returnTime.plusDays(Config.DAY_ON_SUBSCRIPTION);
		}
		int orderId = getOrderId(req);
		orderDao.giveOrder(orderId, returnTime);
	}

	/**
	 * Returns {@code true} if giving the book on a subscription, {@code false} if giving the book on a reading room
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest}
	 * @return {@code true} if the {@code HttpServletRequest} contains the parameter <i>onsubscription</i> that equals 
	 * 		   <i>true</i>, {@code false} otherwise
	 */
	private static boolean isOnSubscription(HttpServletRequest req) {
		String onSubscription = req.getParameter(REQ_PARAM_ON_SUBSCRIPTION);
		return Boolean.parseBoolean(onSubscription);
	}

	/**
	 * Returns a list of all orders with books given to readers
	 * 
	 * @return the list of all processed orders
	 * @throws DaoException if cannot get list of orders from {@code OrderDao}
	 */
	public List<Order> getProcessedOrders() throws DaoException {
		return orderDao.getProcessedOrders();
	}

	/**
	 * Completes the order with selected ID
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing order's ID
	 * @throws DaoException if cannot complete the order
	 * @throws ServiceException if cannot get order's ID from {@code HttpServletRequest}
	 */
	public void completeOrder(HttpServletRequest req) throws DaoException, ServiceException {
		LocalDateTime returnTime = LocalDateTime.now(Config.ZONE_ID);
		int orderId = getOrderId(req);
		orderDao.completeOrder(orderId, returnTime);
	}

	/**
	 * Returns a list of all orders with states <i>NEW</i> and <i>PROCESSED</i> for selected reader
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing reader's ID
	 * @return the list of actual orders for selected reader
	 * @throws DaoException if cannot get list of orders
	 * @throws ServiceException if cannot get reader's ID from the {@code HttpServletRequest}
	 */
	public List<Order> getReaderActualOrders(HttpServletRequest req) throws DaoException, ServiceException {
		int readerId = getReaderId(req);
		return orderDao.getUserActualOrders(readerId);
	}

	/**
	 * Extracts the reader's ID from the {@code HttpServletRequest}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing reader's ID
	 * @return the reader's ID
	 * @throws ServiceException if the {@code HttpServletRequest} has no reader's ID 
	 * 		   or if the reader's ID is not a number
	 */
	private static int getReaderId(HttpServletRequest req) throws ServiceException {
		String readerId = req.getParameter(REQ_PARAM_READER_ID);
		if ((readerId == null) ||!readerId.matches(ONLY_DIGITS_REGEX)) {
			LOG.error(ERROR_WRONG_READER_ID);
			throw new ServiceException(ERROR_WRONG_READER_ID);
		}
		return Integer.parseInt(readerId);
	}

	/**
	 * Returns a list of all orders with state <i>PROCESSED</i> for the selected reader
	 * 
	 * @param user
	 * 		  The {@code User}
	 * @return the list of processed orders for selected reader
	 * @throws DaoException if cannot get list of orders
	 */
	public List<Order> getReaderProcessedOrders(User user) throws DaoException {
		int userId = user.getId();
		return orderDao.getUserProcessedOrders(userId);
	}

	/**
	 * Gets a list of overdue orders' IDs and sets fine for them
	 * 
	 * @throws DaoException if cannot get a list of orders' IDs or if cannot sets fine for orders with these IDs
	 */
	public void setFineForOverdueOrders() throws DaoException {
//		LocalDateTime ldt = LocalDate.now(ZONE_ID).atStartOfDay();
		LocalDateTime ldt = LocalDateTime.now(Config.ZONE_ID);
		List<Integer> overdueOrdersIds = orderDao.getOverdueOrdersIds(ldt);
		for (Integer id : overdueOrdersIds) {
			orderDao.setFine(id.intValue(), Config.FINE);
		}
	}
	
	/**
	 * Sends an error message to the log and throws the {@code ServiceException} with this message
	 * 
	 * @param message
	 * 		  an error message for the log and the {@code ServiceException}
	 * @throws ServiceException
	 */
	private static void logAndThrowException(String message) throws ServiceException {
		LOG.error(message);
		throw new ServiceException(message);
	}
}
