package org.example.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.OrderDao;
import org.example.entity.Book;
import org.example.entity.Order;
import org.example.entity.OrderState;
import org.example.entity.User;
import org.example.exception.DaoException;

public class OrderDaoImpl implements OrderDao {

	private static final Logger LOG = LogManager.getLogger(OrderDaoImpl.class);
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final String QUERY_SELECT_COUNT_ACTIVE_ORDERS = "select.count.active.orders";
	private static final String QUERY_INSERT_ORDER = "insert.order";
	private static final String QUERY_SELECT_READER_ORDERS = "select.reader.orders";
	private static final String QUERY_SELECT_ORDER_STATE = "select.order.state";
	private static final String QUERY_UPDATE_ORDER_STATE = "update.order.state";
	private static final String QUERY_SELECT_NEW_ORDERS = "select.new.orders";
	private static final String QUERY_SELECT_ORDER_BY_ID = "select.order.by.id";
	private static final String QUERY_SELECT_BOOK_AVAILABLE = "select.book.available";
	private static final String QUERY_UPDATE_BOOK_AVAILABLE = "update.book.available";
	private static final String QUERY_UPDATE_ORDER_STATE_AND_RETURN_DATE = "update.order.state.and.return.time";
	private static final String QUERY_SELECT_PROCESSED_ORDERS = "select.processed.orders";
	private static final String QUERY_SELECT_READER_ACTUAL_ORDERS = "select.reader.actual.orders";
	private static final String QUERY_SELECT_READER_PROCESSED_ORDERS = "select.reader.processed.orders";
	private static final String QUERY_SELECT_OVERDUE_ORDERS_IDS = "select.overdue.orders.ids";
	private static final String QUERY_SET_FINE = "set.fine";
	private static final String ERROR_CANNOT_CREATE_ORDER = "error.cannot.create.order";
	private static final String ERROR_CANNOT_FIND_ORDER = "error.cannot.find.order";
	private static final String ERROR_CANNOT_GET_ORDERS_COUNT = "error.cannot.get.orders.count";
	private static final String ERROR_CANNOT_GET_USER_ORDERS_LIST = "error.cannot.get.user.orders.list";
	private static final String ERROR_CANNOT_CANCEL_ORDER_NUM = "error.cannot.cancel.order.num";
	private static final String ERROR_CANNOT_GET_NEW_ORDERS_LIST = "error.cannot.get.new.orders.list";
	private static final String ERROR_BOOK_NOT_AVAILABLE = "error.book.not.available";
	private static final String ERROR_CANNOT_GIVE_ORDER = "error.cannot.give.order";
	private static final String ERROR_CANNOT_GET_PROCESSED_ORDERS = "error.cannot.get.processed.orders";
	private static final String ERROR_CANNOT_COMPLETE_ORDER = "error.cannot.complete.order";
	private static final String ERROR_CANNOT_GET_USER_ACTUAL_ORDERS = "error.cannot.get.user.actual.orders";
	private static final String ERROR_CANNOT_GET_USER_PROCESSED_ORDERS = "error.cannot.get.user.processed.orders";
	private static final String ERROR_CANNOT_GET_OVERDUE_ORDERS = "error.cannot.get.overdue.orders";
	private static final String ERROR_CANNOT_SET_ORDER_FINE = "error.cannot.set.order.fine";
	
	@Override
	public Order create(Order order) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_INSERT_ORDER), PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(++k, order.getUser().getId());
			pstmt.setInt(++k, order.getBook().getId());
			k = pstmt.executeUpdate();
			if (k > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					order.setId(rs.getInt(1));
				}
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_CREATE_ORDER, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return order;
	}
	
	@Override
	public Order find(Order order) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_ORDER_BY_ID));
			pstmt.setInt(++k, order.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				order = getOrder(rs);
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_FIND_ORDER, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return order;
	}
	
	@Override
	public void update(Order entity) throws DaoException {}

	@Override
	public void remove(Order entity) throws DaoException {}

	@Override
	public int countActiveOrders(User user, Book book) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_COUNT_ACTIVE_ORDERS));
			pstmt.setInt(++k, user.getId());
			pstmt.setInt(++k, book.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_ORDERS_COUNT, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
	}
	

	@Override
	public List<Order> getUserOrders(int userId) throws DaoException {
		List<Order> orders = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_READER_ORDERS));
			pstmt.setInt(++k, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orders.add(getOrder(rs));
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_USER_ORDERS_LIST + userId, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return orders;
	}
	
	private static Order getOrder(ResultSet rs) throws SQLException {
		Order order = new Order();
		int k=0;
		order.setId(rs.getInt(++k));
		User user = new User();
		user.setId(rs.getInt(++k));
		user.setEmail(rs.getString(++k));
		user.setFirstName(rs.getString(++k));
		user.setLastName(rs.getString(++k));
		user.setPhoneNumber(rs.getString(++k));
		user.setPassportNumber(rs.getString(++k));
		user.setBlocked(intToBoolean(rs.getInt(++k)));
		order.setUser(user);
		Book book = new Book.Builder().setId(rs.getInt(++k))
									  .setAuthor(rs.getString(++k))
									  .setTitle(rs.getString(++k))
									  .setPublication(rs.getString(++k))
									  .setPublicationYear(rs.getInt(++k))
									  .setQuantity(rs.getInt(++k))
									  .setAvailable(rs.getInt(++k))
									  .build();
		order.setBook(book);
		order.setState(OrderState.valueOf(rs.getString(++k).toUpperCase()));
		String dateTime = rs.getString(++k).replace(" ", "T");
		order.setCreateTime(LocalDateTime.parse(dateTime));
		dateTime = rs.getString(++k);
		if (dateTime != null) {
			order.setReturnTime(LocalDateTime.parse(dateTime.replace(" ", "T")));
		}
		order.setFine(rs.getInt(++k));
		book.setIsbn(rs.getString(++k));
		return order;
	}

	private static boolean intToBoolean(int num) {
		return num != 0 ? true : false;
	}

	@Override
	public boolean cancelOrder(int orderId) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_ORDER_STATE));
			pstmt.setInt(++k, orderId);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				throw new SQLException();
			}
			k = 0;
			if (!OrderState.valueOf(rs.getString(++k).toUpperCase()).equals(OrderState.NEW)) {
				return false;
			}
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_UPDATE_ORDER_STATE));
			pstmt.setString(++k, OrderState.CANCELED.toString().toLowerCase());
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_CANCEL_ORDER_NUM + orderId, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return true;
	}
	
	@Override
	public List<Order> getNewOrders() throws DaoException {
		List<Order> orders = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(Queries.getInstance().getQuery(QUERY_SELECT_NEW_ORDERS));
			while (rs.next()) {
				orders.add(getOrder(rs));
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_NEW_ORDERS_LIST, e);
		} finally {
			DbManager.closeResources(con, stmt, rs);
		}
		return orders;
	}
	
	@Override
	public void giveOrder(int orderId, LocalDateTime returnTime) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_BOOK_AVAILABLE));
			pstmt.setInt(++k, orderId);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				throw new SQLException();
			}
			k = 0;
			int available = rs.getInt(++k);
			if (available == 0) {
				LOG.error(ERROR_BOOK_NOT_AVAILABLE);
				throw new DaoException(ERROR_BOOK_NOT_AVAILABLE);
			}
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_UPDATE_ORDER_STATE_AND_RETURN_DATE));
			pstmt.setString(++k, returnTime.format(DATE_TIME_FORMATTER));
			pstmt.setString(++k, OrderState.PROCESSED.toString().toLowerCase());
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_UPDATE_BOOK_AVAILABLE));
			pstmt.setInt(++k, --available);
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			DbManager.rollback(con);
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GIVE_ORDER + orderId, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
	}
	
	@Override
	public List<Order> getProcessedOrders() throws DaoException {
		List<Order> orders = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(Queries.getInstance().getQuery(QUERY_SELECT_PROCESSED_ORDERS));
			while (rs.next()) {
				orders.add(getOrder(rs));
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_PROCESSED_ORDERS, e);
		} finally {
			DbManager.closeResources(con, stmt, rs);
		}
		return orders;
	}
	
	@Override
	public void completeOrder(int orderId, LocalDateTime returnTime) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_BOOK_AVAILABLE));
			pstmt.setInt(++k, orderId);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				throw new SQLException();
			}
			k = 0;
			int available = rs.getInt(++k);
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_UPDATE_ORDER_STATE_AND_RETURN_DATE));
			pstmt.setString(++k, returnTime.format(DATE_TIME_FORMATTER));
			pstmt.setString(++k, OrderState.COMPLETED.toString().toLowerCase());
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_UPDATE_BOOK_AVAILABLE));
			pstmt.setInt(++k, ++available);
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			DbManager.rollback(con);
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_COMPLETE_ORDER + orderId, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
	}

	@Override
	public List<Order> getUserActualOrders(int userId) throws DaoException {
		List<Order> orders = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_READER_ACTUAL_ORDERS));
			pstmt.setInt(++k, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orders.add(getOrder(rs));
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_USER_ACTUAL_ORDERS + userId, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return orders;
	}

	@Override
	public List<Order> getUserProcessedOrders(int userId) throws DaoException {
		List<Order> orders = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_READER_PROCESSED_ORDERS));
			pstmt.setInt(++k, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orders.add(getOrder(rs));
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_USER_PROCESSED_ORDERS + userId, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return orders;
	}
	
	@Override
	public List<Integer> getOverdueOrdersIds(LocalDateTime ldt) throws DaoException {
		List<Integer> overdueOrdersIds = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_OVERDUE_ORDERS_IDS));
			pstmt.setString(++k, ldt.format(DATE_TIME_FORMATTER));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				overdueOrdersIds.add(rs.getInt(k));
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_OVERDUE_ORDERS, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return overdueOrdersIds;
	}
	
	@Override
	public void setFine(int orderId, int fine) throws DaoException {
		PreparedStatement pstmt = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SET_FINE));
			pstmt.setInt(++k, fine);
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_SET_ORDER_FINE + orderId, e);
		} finally {
			DbManager.closeResources(con, pstmt);
		}
	}
	
}
