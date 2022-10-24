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
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.example.entity.Book;
import org.example.exception.DaoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BookDaoImplTest {
	
	private final static String DB_URL = "jdbc:mysql://localhost:3306/fplibrarydb?user=libadmin&password=111";
	private final static String INIT_SQL_SCRIPT = "sql/BookDaoImplTest.sql";
	
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
	public void bookCRUDTest() throws SQLException, DaoException {
		Book expectedBook = new Book.Builder().setISBN("9780131872486")
									  .setTitle("Thinking in Java 4th Edition")
									  .setAuthor("Bruce Eckel")
									  .setPublication("Pearson")
									  .setPublicationYear(2006)
									  .setQuantity(1)
									  .setAvailable(1)
									  .build();
		
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			BookDaoImpl.create(con, expectedBook);
		}

		Book actualBook = null;
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualBook = BookDaoImpl.find(con, expectedBook);
		}
		assertEquals(expectedBook, actualBook);
		
		expectedBook.setId(actualBook.getId());
		expectedBook.setQuantity(2);
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			BookDaoImpl.update(con, expectedBook);
		}
		
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualBook = BookDaoImpl.find(con, expectedBook);
		}
		assertEquals(expectedBook.getQuantity(), actualBook.getQuantity());
		assertEquals(expectedBook.getQuantity(), actualBook.getAvailable());
		
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			BookDaoImpl.remove(con, expectedBook);
		}
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualBook = BookDaoImpl.find(con, expectedBook);
		}
		assertEquals(null, actualBook);
	}
	
/*	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"a"},
			   nullValues = {"NULL"})*/
	@ParameterizedTest
	@CsvSource({
		", author, desc", 
		"'', '', ''",
		"a, , "})
	public void findBooksCountsTest(String searchText, String orderBy, String orderType) throws DaoException, SQLException {
		List<Book> books = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			books = BookDaoImpl.findBooks(con, searchText, orderBy, orderType, 1000, 0);
		}
		assertEquals(true, books.size() > 0);
		
		int count = 0;
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			count = BookDaoImpl.countBooks(con, searchText);
		}
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
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			BookDaoImpl.create(con, book);
		}
		Book actualBook;
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualBook = BookDaoImpl.find(con, book);
		}
		book.setId(actualBook.getId());
		assertThrows(DaoException.class, () -> BookDaoImpl.create(DriverManager.getConnection(DB_URL), book));
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			BookDaoImpl.remove(con, book);
		}

		Book issuedBook = new Book.Builder().setId(2).setQuantity(1).build();
		assertThrows(DaoException.class, () -> BookDaoImpl.remove(DriverManager.getConnection(DB_URL), issuedBook));
		assertThrows(DaoException.class, () -> BookDaoImpl.update(DriverManager.getConnection(DB_URL), issuedBook));
		Book nonExistentBook = new Book.Builder().setId(4).build();
		Connection con = DriverManager.getConnection(DB_URL);
		assertThrows(DaoException.class, () -> BookDaoImpl.update(con, nonExistentBook));
		assertThrows(DaoException.class, () -> BookDaoImpl.create(con, book));
		assertThrows(DaoException.class, () -> BookDaoImpl.find(con, book));
		assertThrows(DaoException.class, () -> BookDaoImpl.update(con, book));
		assertThrows(DaoException.class, () -> BookDaoImpl.remove(con, book));
		assertThrows(DaoException.class, () -> BookDaoImpl.countBooks(con, null));
		assertThrows(DaoException.class, () -> BookDaoImpl.findBooks(con, null, null, null, 0, 0));
	}
}
