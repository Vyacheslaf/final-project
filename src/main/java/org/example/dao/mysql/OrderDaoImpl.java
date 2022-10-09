package org.example.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
	private static final String QUERY_SELECT_COUNT_ACTIVE_ORDERS 
												= "select.count.active.orders";
	private static final String QUERY_INSERT_ORDER = "insert.order";
	private static final String QUERY_SELECT_READER_ORDERS 
													= "select.reader.orders";
	private static final String QUERY_SELECT_ORDER_STATE = "select.order.state";
	private static final String QUERY_UPDATE_ORDER_STATE = "update.order.state";
	
	@Override
	public Order create(Order order) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_INSERT_ORDER), 
									PreparedStatement.RETURN_GENERATED_KEYS);
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
			e.printStackTrace();
			String message = "Cannot create order";
			LOG.error(message);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return order;
	}

	@Override
	public Order find(Order entity) throws DaoException {
		// TODO Auto-generated method stub
		return null;
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
			String query = Queries.getQuery(QUERY_SELECT_COUNT_ACTIVE_ORDERS);
			pstmt = con.prepareStatement(query);
			pstmt.setInt(++k, user.getId());
			pstmt.setInt(++k, book.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			String message = "Cannot get count of active orders";
			LOG.error(message);
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
			e.printStackTrace();
			String message = "Cannot get list of order for user #" + userId;
			LOG.error(message);
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
		Book book = new Book.Builder().setAuthor(rs.getString(++k))
									  .setTitle(rs.getString(++k))
									  .setPublication(rs.getString(++k))
									  .setPublicationYear(rs.getInt(++k))
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
		return order;
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
			if (!OrderState.valueOf(rs.getString(++k).toUpperCase())
							.equals(OrderState.NEW)) {
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
			e.printStackTrace();
			String message = "Cannot cancel order #" + orderId;
			LOG.error(message);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return true;
	}

}
