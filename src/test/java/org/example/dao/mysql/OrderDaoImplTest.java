package org.example.dao.mysql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.example.entity.Book;
import org.example.entity.Order;
import org.example.entity.OrderState;
import org.example.entity.User;
import org.example.exception.DaoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class OrderDaoImplTest {
	
	private final static String DB_URL = "jdbc:mysql://localhost:3306/fplibrarydb?user=libadmin&password=111";
	private final static String INIT_SQL_SCRIPT = "sql/OrderDaoImplTest.sql";
	
	@BeforeAll
	public static void initDB() throws SQLException, FileNotFoundException, IOException, URISyntaxException {
		URL url = UserDaoImplTest.class.getClassLoader().getResource(INIT_SQL_SCRIPT);
		File file = new File(url.toURI());
		try (Connection con = DriverManager.getConnection(DB_URL);
			 InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
			ScriptRunner runner=new ScriptRunner(con);
			runner.runScript(reader);
		}
	}

	@Test
	public void orderCreateFindTest() throws DaoException, SQLException {
		User user = new User();
		user.setId(1);
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			user = UserDaoImpl.find(con, user);
		}
		Book book = new Book.Builder().setISBN("9786175364635").build();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			book = BookDaoImpl.find(con, book);
		}
		Order order = new Order();
		order.setUser(user);
		order.setBook(book);
		order.setState(OrderState.NEW);
		
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			order = OrderDaoImpl.create(con, order);
		}
		assertEquals(true, order.getId() > 0);
		
		Order actualOrder;
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualOrder = OrderDaoImpl.find(con, order);
		}
		assertEquals(order, actualOrder);
	}

	@Test
	public void countActiveOrdersTest() throws SQLException, DaoException {
		User user = new User();
		user.setId(1);
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			user = UserDaoImpl.find(con, user);
		}
		Book book = new Book.Builder().setISBN("9786175364635").build();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			book = BookDaoImpl.find(con, book);
		}
		
		int count = 0;
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			count = OrderDaoImpl.countActiveOrders(con, user, book);
		}
		assertEquals(true, count > 0);
		
		book = new Book.Builder().setISBN("9789660362536").build();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			book = BookDaoImpl.find(con, book);
		}
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			count = OrderDaoImpl.countActiveOrders(con, user, book);
		}
		assertEquals(false, count > 0);
	}
	
	@Test
	public void getUserOrdersTest() throws SQLException, DaoException {
		int userId = 1;
		List<Order> orders = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			orders = OrderDaoImpl.getUserOrders(con, userId);
		}
		assertEquals(true, orders.size() > 0);
	}
	
	@Test
	public void cancelOrderTest() throws SQLException, DaoException {
		int orderId = 2;
		boolean isCanceled = false;
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			isCanceled = OrderDaoImpl.cancelOrder(con, orderId);
		}
		assertEquals(true, isCanceled);
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			isCanceled = OrderDaoImpl.cancelOrder(con, orderId);
		}
		assertEquals(false, isCanceled);
	}
	
	@Test
	public void getNewOrdersTest() throws SQLException, DaoException {
		List<Order> orders = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			orders = OrderDaoImpl.getNewOrders(con);
		}
		assertEquals(true, orders.size() > 0);
	}
	
	@Test
	public void giveOrderTest() throws SQLException, DaoException {
		int orderId = 1;
		LocalDateTime returnTime = LocalDate.now().plusDays(1).atStartOfDay().minusSeconds(1);
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			OrderDaoImpl.giveOrder(con, orderId, returnTime);
		}
		Order order = new Order();
		order.setId(orderId);
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			order = OrderDaoImpl.find(con, order);
		}
		assertEquals(OrderState.PROCESSED, order.getState());
	}
	
	@Test
	public void completeOrderTest() throws SQLException, DaoException {
		int orderId = 3;
		LocalDateTime returnTime = LocalDate.now().plusDays(1).atStartOfDay().minusSeconds(1);
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			OrderDaoImpl.completeOrder(con, orderId, returnTime);
		}
		Order order = new Order();
		order.setId(orderId);
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			order = OrderDaoImpl.find(con, order);
		}
		assertEquals(OrderState.COMPLETED, order.getState());
	}

	@Test
	public void getProcessedOrdersTest() throws SQLException, DaoException {
		List<Order> orders = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			orders = OrderDaoImpl.getProcessedOrders(con);
		}
		assertEquals(true, orders.size() > 0);
	}

	@Test
	public void getUserActualOrdersTest() throws SQLException, DaoException {
		int userId = 1;
		List<Order> orders = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			orders = OrderDaoImpl.getUserActualOrders(con, userId);
		}
		assertEquals(true, orders.size() > 0);
	}
	
	@Test
	public void setFineTest() throws SQLException, DaoException {
		int userId = 1;
		List<Order> orders = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			orders = OrderDaoImpl.getUserOrders(con, userId);
		}
		Order order = orders.get(0);
		int fine = 50;
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			OrderDaoImpl.setFine(con, order.getId(), fine);
		}
		
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			order = OrderDaoImpl.find(con, order);
		}
		assertEquals(fine, order.getFine());
	}

	@Test
	public void getUserProcessedOrdersTest() throws SQLException, DaoException {
		int userId = 2;
		List<Order> orders = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			orders = OrderDaoImpl.getUserProcessedOrders(con, userId);
		}
		assertEquals(2, orders.size());
	}

	@Test
	public void getOverdueOrdersIdsTest() throws SQLException, DaoException {
		LocalDateTime now = LocalDate.now().atStartOfDay();
		List<Integer> idList = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			idList = OrderDaoImpl.getOverdueOrdersIds(con, now);
		}
		assertEquals(1, idList.size());
	}
	
	@Test
	public void daoExceptionTest() throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL);
		assertThrows(DaoException.class, () -> OrderDaoImpl.giveOrder(conn, 6, null));
		Connection con = DriverManager.getConnection(DB_URL);
		assertThrows(DaoException.class, () -> OrderDaoImpl.cancelOrder(con, 0));
		assertThrows(DaoException.class, () -> OrderDaoImpl.completeOrder(con, 0, null));
		assertThrows(DaoException.class, () -> OrderDaoImpl.countActiveOrders(con, null, null));
		assertThrows(DaoException.class, () -> OrderDaoImpl.create(con, null));
		assertThrows(DaoException.class, () -> OrderDaoImpl.find(con, null));
		assertThrows(DaoException.class, () -> OrderDaoImpl.getNewOrders(con));
		assertThrows(DaoException.class, () -> OrderDaoImpl.getOverdueOrdersIds(con, null));
		assertThrows(DaoException.class, () -> OrderDaoImpl.getProcessedOrders(con));
		assertThrows(DaoException.class, () -> OrderDaoImpl.getUserActualOrders(con, 0));
		assertThrows(DaoException.class, () -> OrderDaoImpl.getUserOrders(con, 0));
		assertThrows(DaoException.class, () -> OrderDaoImpl.getUserProcessedOrders(con, 0));
		assertThrows(DaoException.class, () -> OrderDaoImpl.giveOrder(con, 0, null));
		assertThrows(DaoException.class, () -> OrderDaoImpl.setFine(con, 0, 0));
	}
}
