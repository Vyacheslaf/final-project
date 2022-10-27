package org.example.dao.mysql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.example.entity.Book;
import org.example.exception.DaoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

public class BookDaoImplTest {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/fplibrarydb?user=libadmin&password=111";
	private static final String INIT_SQL_SCRIPT = "sql/BookDaoImplTest.sql";
	
	private final BookDaoImpl bookDaoImpl = new BookDaoImpl();
	
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
	public void bookCRUDTest() throws SQLException, DaoException {
		Book expectedBook = new Book.Builder().setISBN("9780131872486")
									  .setTitle("Thinking in Java 4th Edition")
									  .setAuthor("Bruce Eckel")
									  .setPublication("Pearson")
									  .setPublicationYear(2006)
									  .setQuantity(1)
									  .setAvailable(1)
									  .build();
		bookDaoImpl.create(expectedBook);
		tuneMockDbManager();
		Book actualBook = bookDaoImpl.find(expectedBook);
		assertEquals(expectedBook, actualBook);
		
		tuneMockDbManager();
		expectedBook.setId(actualBook.getId());
		expectedBook.setQuantity(2);
		bookDaoImpl.update(expectedBook);
		
		tuneMockDbManager();
		actualBook = bookDaoImpl.find(expectedBook);
		assertEquals(expectedBook.getQuantity(), actualBook.getQuantity());
		assertEquals(expectedBook.getQuantity(), actualBook.getAvailable());
		
		tuneMockDbManager();
		bookDaoImpl.remove(expectedBook);
		tuneMockDbManager();
		actualBook = bookDaoImpl.find(expectedBook);
		assertNull(actualBook);
	}
	
	@ParameterizedTest
	@CsvSource({
		", author, desc", 
		"'', '', ''",
		"a, , "})
	public void findBooksCountsTest(String searchText, String orderBy, String orderType) throws DaoException, SQLException {
		List<Book> books = new ArrayList<>();
		books = bookDaoImpl.findBooks(searchText, orderBy, orderType, 1000, 0);
		assertTrue(books.size() > 0);
		
		tuneMockDbManager();
		int count = bookDaoImpl.countBooks(searchText);
		assertEquals(count, books.size());
	}
	
	@Test
	public void daoException() throws SQLException, DaoException {
		Book book = new Book.Builder().setISBN("9780131872486")
				  .setTitle("Thinking in Java 4th Edition")
				  .setAuthor("Bruce Eckel")
				  .setPublication("Pearson")
				  .setPublicationYear(2006)
				  .setQuantity(2)
				  .setAvailable(2)
				  .build();
		bookDaoImpl.create(book);
		tuneMockDbManager();
		Book actualBook = bookDaoImpl.find(book);
		book.setId(actualBook.getId());
		tuneMockDbManager();
		assertThrows(DaoException.class, () -> bookDaoImpl.create(book));

		tuneMockDbManager();
		bookDaoImpl.remove(book);

		Book issuedBook = new Book.Builder().setId(2).setQuantity(1).build();
		tuneMockDbManager();
		assertThrows(DaoException.class, () -> bookDaoImpl.remove(issuedBook));
		tuneMockDbManager();
		assertThrows(DaoException.class, () -> bookDaoImpl.update(issuedBook));
		Book nonExistentBook = new Book.Builder().setId(4).build();
		tuneMockDbManager();
		assertThrows(DaoException.class, () -> bookDaoImpl.update(nonExistentBook));
		assertThrows(DaoException.class, () -> bookDaoImpl.create(book));
		assertThrows(DaoException.class, () -> bookDaoImpl.find(book));
		assertThrows(DaoException.class, () -> bookDaoImpl.update(book));
		assertThrows(DaoException.class, () -> bookDaoImpl.remove(book));
		assertThrows(DaoException.class, () -> bookDaoImpl.countBooks(null));
		assertThrows(DaoException.class, () -> bookDaoImpl.findBooks(null, null, null, 0, 0));
	}
}
