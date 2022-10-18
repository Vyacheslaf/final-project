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
	
	@Override
	public Order create(Order order) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_INSERT_ORDER), PreparedStatement.RETURN_GENERATED_KEYS);
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
			String message = "Cannot create order";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return order;
	}

	@Override
	public Order find(Order order) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_ORDER_BY_ID));
			pstmt.setInt(++k, order.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				order = getOrder(rs);
			}
		} catch (SQLException e) {
			String message = "Cannot create order";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return order;
	}

	@Override
	public void update(Order entity) throws DaoException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Order entity) throws DaoException {
		// TODO Auto-generated method stub

	}

	@Override
	public int countActiveOrders(User user, Book book) throws DaoException {
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try (Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_COUNT_ACTIVE_ORDERS));
			pstmt.setInt(++k, user.getId());
			pstmt.setInt(++k, book.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			String message = "Cannot get count of active orders";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return count;
	}

	@Override
	public List<Order> getUserOrders(int userId) throws DaoException {
		List<Order> orders = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_READER_ORDERS));
			pstmt.setInt(++k, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orders.add(getOrder(rs));
			}
		} catch (SQLException e) {
			String message = "Cannot get list of order for user #" + userId;
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return orders;
	}

	private Order getOrder(ResultSet rs) throws SQLException {
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
		try(Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_ORDER_STATE));
			pstmt.setInt(++k, orderId);
			rs = pstmt.executeQuery();
			k = 0;
			if (!rs.next()) {
				throw new SQLException();
			}
			if (!OrderState.valueOf(rs.getString(++k).toUpperCase()).equals(OrderState.NEW)) {
				return false;
			}
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_UPDATE_ORDER_STATE));
			pstmt.setString(++k, OrderState.CANCELED.toString().toLowerCase());
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			String message = "Cannot cancel order #" + orderId;
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return true;
	}

	@Override
	public List<Order> getNewOrders() throws DaoException {
		List<Order> orders = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			stmt = con.createStatement();
			rs = stmt.executeQuery(Queries.getQuery(QUERY_SELECT_NEW_ORDERS));
			while (rs.next()) {
				orders.add(getOrder(rs));
			}
		} catch (SQLException e) {
			String message = "Cannot get list of new orders";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(stmt);
		}
		return orders;
	}

	@Override
	public void giveOrder(int orderId, LocalDateTime returnTime) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbManager.getInstance().getConnection();
			int k = 0;
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_BOOK_AVAILABLE));
			pstmt.setInt(++k, orderId);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				throw new SQLException();
			}
			k = 0;
			int available = rs.getInt(++k);
			if (available == 0) {
				String message = "Book is not available";
				LOG.error(message);
				throw new DaoException(message);
			}
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_UPDATE_ORDER_STATE_AND_RETURN_DATE));
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			pstmt.setString(++k, returnTime.format(DATE_TIME_FORMATTER));
			pstmt.setString(++k, OrderState.PROCESSED.toString().toLowerCase());
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_UPDATE_BOOK_AVAILABLE));
			pstmt.setInt(++k, --available);
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			DbManager.rollback(con);
			String message = "Cannot give an order #" + orderId;
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
			DbManager.close(con);
		}
	}

	@Override
	public List<Order> getProcessedOrders() throws DaoException {
		List<Order> orders = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			stmt = con.createStatement();
			rs = stmt.executeQuery(Queries.getQuery(QUERY_SELECT_PROCESSED_ORDERS));
			while (rs.next()) {
				orders.add(getOrder(rs));
			}
		} catch (SQLException e) {
			String message = "Cannot get list of processed orders";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(stmt);
		}
		return orders;
	}

	@Override
	public void completeOrder(int orderId, LocalDateTime returnTime) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = DbManager.getInstance().getConnection();
			int k = 0;
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_BOOK_AVAILABLE));
			pstmt.setInt(++k, orderId);
			rs = pstmt.executeQuery();
			if (!rs.next()) {
				throw new SQLException();
			}
			k = 0;
			int available = rs.getInt(++k);
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_UPDATE_ORDER_STATE_AND_RETURN_DATE));
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			pstmt.setString(++k, returnTime.format(DATE_TIME_FORMATTER));
			pstmt.setString(++k, OrderState.COMPLETED.toString().toLowerCase());
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
			DbManager.close(pstmt);
			k=0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_UPDATE_BOOK_AVAILABLE));
			pstmt.setInt(++k, ++available);
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			DbManager.rollback(con);
			String message = "Cannot give an order #" + orderId;
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
			DbManager.close(con);
		}
	}

	@Override
	public List<Order> getUserActualOrders(int userId) throws DaoException {
		List<Order> orders = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_READER_ACTUAL_ORDERS));
			pstmt.setInt(++k, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orders.add(getOrder(rs));
			}
		} catch (SQLException e) {
			String message = "Cannot get list of order for user #" + userId;
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return orders;
	}

	@Override
	public List<Order> getUserProcessedOrders(int userId) throws DaoException {
		List<Order> orders = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_READER_PROCESSED_ORDERS));
			pstmt.setInt(++k, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orders.add(getOrder(rs));
			}
		} catch (SQLException e) {
			String message = "Cannot get list of orders for user #" + userId;
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return orders;
	}

	@Override
	public List<Integer> getOverdueOrdersIds(LocalDateTime ldt) throws DaoException {
		List<Integer> overdueOrdersIds = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_OVERDUE_ORDERS_IDS));
			pstmt.setString(++k, ldt.format(DATE_TIME_FORMATTER));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				overdueOrdersIds.add(rs.getInt(k));
			}
		} catch (SQLException e) {
			String message = "Cannot get list of overdue orders ids";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return overdueOrdersIds;
	}

	@Override
	public void setFine(int orderId, int fine) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SET_FINE));
			pstmt.setInt(++k, fine);
			pstmt.setInt(++k, orderId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			String message = "Cannot set fine for order #" + orderId;
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
	}

	
}
