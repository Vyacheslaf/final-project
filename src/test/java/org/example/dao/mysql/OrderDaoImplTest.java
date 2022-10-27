package org.example.dao.mysql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.example.entity.Book;
import org.example.entity.Order;
import org.example.entity.OrderState;
import org.example.entity.User;
import org.example.exception.DaoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class OrderDaoImplTest {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/fplibrarydb?user=libadmin&password=111";
	private static final String INIT_SQL_SCRIPT = "sql/OrderDaoImplTest.sql";
	
	private final OrderDaoImpl orderDaoImpl = new OrderDaoImpl();
	
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
	
	@BeforeEach
	public void tuneMockDbManager() throws DaoException, SQLException {
	    DbManager mockDbManager = Mockito.mock(DbManager.class);
	    setMock(mockDbManager);
	    Mockito.when(mockDbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL));
	}

	private void setMock(DbManager mock) {
	    try {
	        Field instance = DbManager.class.getDeclaredField("instance");
	        instance.setAccessible(true);
	        instance.set(instance, mock);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	@AfterEach
	public void resetDbManager() throws Exception {
	   Field instance = DbManager.class.getDeclaredField("instance");
	   instance.setAccessible(true);
	   instance.set(null, null);
	} 

	@Test
	public void orderCreateFindTest() throws DaoException, SQLException {
		User user = new User();
		user.setId(1);
		user = new UserDaoImpl().find(user);
		Book book = new Book.Builder().setISBN("9786175364635").build();
		tuneMockDbManager();
		book = new BookDaoImpl().find(book);
		Order order = new Order();
		order.setUser(user);
		order.setBook(book);
		order.setState(OrderState.NEW);
		
		tuneMockDbManager();
		order = orderDaoImpl.create(order);
		assertTrue(order.getId() > 0);
		
		tuneMockDbManager();
		Order actualOrder = orderDaoImpl.find(order);
		assertEquals(order, actualOrder);
	}

	@Test
	public void countActiveOrdersTest() throws SQLException, DaoException {
		User user = new User();
		user.setId(1);
		user = new UserDaoImpl().find(user);
		Book book = new Book.Builder().setISBN("9786175364635").build();
		tuneMockDbManager();
		book = new BookDaoImpl().find(book);
		tuneMockDbManager();
		int count = orderDaoImpl.countActiveOrders(user, book);
		assertTrue(count > 0);
		
		book = new Book.Builder().setISBN("9789660362536").build();
		tuneMockDbManager();
		book = new BookDaoImpl().find(book);
		tuneMockDbManager();
		count = orderDaoImpl.countActiveOrders(user, book);
		assertFalse(count > 0);
	}
	
	@Test
	public void getUserOrdersTest() throws SQLException, DaoException {
		int userId = 1;
		List<Order> orders = orderDaoImpl.getUserOrders(userId);
		assertTrue(orders.size() > 0);
	}
	
	@Test
	public void cancelOrderTest() throws SQLException, DaoException {
		int orderId = 2;
		boolean isCanceled = orderDaoImpl.cancelOrder(orderId);
		assertTrue(isCanceled);
		tuneMockDbManager();
		isCanceled = orderDaoImpl.cancelOrder(orderId);
		assertFalse(isCanceled);
	}
	
	@Test
	public void getNewOrdersTest() throws SQLException, DaoException {
		List<Order> orders = orderDaoImpl.getNewOrders();
		assertTrue(orders.size() > 0);
	}
	
	@Test
	public void giveOrderTest() throws SQLException, DaoException {
		int orderId = 1;
		Order order = new Order();
		order.setId(orderId);
		LocalDateTime returnTime = LocalDate.now().plusDays(1).atStartOfDay().minusSeconds(1);
		orderDaoImpl.giveOrder(orderId, returnTime);
		tuneMockDbManager();
		order = orderDaoImpl.find(order);
		assertEquals(OrderState.PROCESSED, order.getState());
	}
	
	@Test
	public void completeOrderTest() throws SQLException, DaoException {
		int orderId = 3;
		Order order = new Order();
		order.setId(orderId);
		LocalDateTime returnTime = LocalDate.now().plusDays(1).atStartOfDay().minusSeconds(1);
		orderDaoImpl.completeOrder(orderId, returnTime);
		tuneMockDbManager();
		order = orderDaoImpl.find(order);
		assertEquals(OrderState.COMPLETED, order.getState());
	}

	@Test
	public void getProcessedOrdersTest() throws SQLException, DaoException {
		List<Order> orders = orderDaoImpl.getProcessedOrders();
		assertTrue(orders.size() > 0);
	}

	@Test
	public void getUserActualOrdersTest() throws SQLException, DaoException {
		int userId = 1;
		List<Order> orders = orderDaoImpl.getUserActualOrders(userId);
		assertTrue(orders.size() > 0);
	}
	
	@Test
	public void setFineTest() throws SQLException, DaoException {
		int userId = 1;
		List<Order> orders = orderDaoImpl.getUserOrders(userId);
		Order order = orders.get(0);
		int fine = 50;
		tuneMockDbManager();
		orderDaoImpl.setFine(order.getId(), fine);
		
		tuneMockDbManager();
		order = orderDaoImpl.find(order);
		assertEquals(fine, order.getFine());
	}

	@Test
	public void getUserProcessedOrdersTest() throws SQLException, DaoException {
		int userId = 2;
		List<Order> orders = orderDaoImpl.getUserProcessedOrders(userId);
		assertEquals(2, orders.size());
	}

	@Test
	public void getOverdueOrdersIdsTest() throws SQLException, DaoException {
		LocalDateTime now = LocalDate.now().atStartOfDay();
		List<Integer> idList = orderDaoImpl.getOverdueOrdersIds(now);
		assertEquals(1, idList.size());
	}
	
	@Test
	public void daoExceptionTest() throws SQLException, DaoException {
		assertThrows(DaoException.class, () -> orderDaoImpl.giveOrder(6, null));
		tuneMockDbManager();
		assertThrows(DaoException.class, () -> orderDaoImpl.cancelOrder(0));
		assertThrows(DaoException.class, () -> orderDaoImpl.completeOrder(0, null));
		assertThrows(DaoException.class, () -> orderDaoImpl.countActiveOrders(null, null));
		assertThrows(DaoException.class, () -> orderDaoImpl.create(null));
		assertThrows(DaoException.class, () -> orderDaoImpl.find(null));
		assertThrows(DaoException.class, () -> orderDaoImpl.getNewOrders());
		assertThrows(DaoException.class, () -> orderDaoImpl.getOverdueOrdersIds(null));
		assertThrows(DaoException.class, () -> orderDaoImpl.getProcessedOrders());
		assertThrows(DaoException.class, () -> orderDaoImpl.getUserActualOrders(0));
		assertThrows(DaoException.class, () -> orderDaoImpl.getUserOrders(0));
		assertThrows(DaoException.class, () -> orderDaoImpl.getUserProcessedOrders(0));
		assertThrows(DaoException.class, () -> orderDaoImpl.giveOrder(0, null));
		assertThrows(DaoException.class, () -> orderDaoImpl.setFine(0, 0));
	}
}
