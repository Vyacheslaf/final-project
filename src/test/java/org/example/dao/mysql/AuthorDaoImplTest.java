package org.example.dao.mysql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.example.entity.Author;
import org.example.exception.DaoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AuthorDaoImplTest {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/fplibrarydb?user=libadmin&password=111";
	private static final String INIT_SQL_SCRIPT = "sql/BookDaoImplTest.sql";
	
	private final AuthorDaoImpl authorDaoImpl = new AuthorDaoImpl();

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
	public void findAllPublicationTest() throws Exception {
		List<Author> authors = authorDaoImpl.findAllAuthor();
		assertEquals(2, authors.size());
	}

	@Test
	public void daoExceptionTest() throws SQLException, DaoException {
		authorDaoImpl.findAllAuthor();
		assertThrows(DaoException.class, () -> authorDaoImpl.findAllAuthor());
	}
}
