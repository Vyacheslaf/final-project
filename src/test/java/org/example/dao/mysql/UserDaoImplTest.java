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
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

public class UserDaoImplTest {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/fplibrarydb?user=libadmin&password=111";
	private static final String INIT_SQL_SCRIPT = "sql/UserDaoImplTest.sql";
	
	private final UserDaoImpl userDaoImpl = new UserDaoImpl();
	
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
	public void findUserByIdTest() throws DaoException, SQLException {
		User expectedUser = new User();
		expectedUser.setId(1);
		expectedUser.setEmail("admin@library.com");
		expectedUser.setFirstName("Mykola");
		expectedUser.setLastName("Petrenko");
		expectedUser.setPhoneNumber("380970000000");
		expectedUser.setPassportNumber("AA000000");
		expectedUser.setRole(UserRole.ADMIN);
		
		User actualUser = userDaoImpl.find(expectedUser);
		assertEquals(expectedUser, actualUser);
	}

	@Test
	public void findUserByPhoneTest() throws DaoException, SQLException {
		User expectedUser = new User();
		expectedUser.setEmail("reader1@gmail.com");
		expectedUser.setFirstName("Reader1");
		expectedUser.setLastName("Readerenko1");
		expectedUser.setPhoneNumber("380970000001");
		expectedUser.setPassportNumber("AA000001");
		expectedUser.setRole(UserRole.READER);
		
		User actualUser = userDaoImpl.findByPhone(expectedUser.getPhoneNumber());
		assertEquals(expectedUser, actualUser);
	}
	
	@Test
	public void findUserByLoginAndPasswordTest() throws DaoException, SQLException {
		User expectedUser = new User();
		expectedUser.setId(1);
		expectedUser.setEmail("admin@library.com");
		expectedUser.setFirstName("Mykola");
		expectedUser.setLastName("Petrenko");
		expectedUser.setPhoneNumber("380970000000");
		expectedUser.setPassportNumber("AA000000");
		expectedUser.setRole(UserRole.ADMIN);
		
		String password = "a4cbb2f3933c5016da7e83fd135ab8a48b67bf61";
		
		User actualUser = userDaoImpl.findByLoginAndPassword(expectedUser.getEmail(), password);
		assertEquals(expectedUser, actualUser);
	}
	
	@ParameterizedTest
	@CsvSource({
		"admin@library.com, asdf",
		"admin@library.com, ''",
		"admin@library.com, ",
		"incorrect_email, nimda",
		"'', nimda",
		" , nimda",
		"'', ",
		" , ''",
		"'', ''",
		" , "
	})
	public void failedFindUserByLoginAndPasswordTest(String email, String password) throws SQLException, DaoException {
		User actualUser = userDaoImpl.findByLoginAndPassword(email, password);
		assertNull(actualUser);
	}

	@ParameterizedTest
	@CsvSource({
		"admin",
		"librarian",
		"reader"
	})
	public void findUserByRole(String role) throws SQLException, DaoException {
		UserRole userRole = UserRole.valueOf(role.toUpperCase());
		List<User> users = userDaoImpl.findByRole(userRole);
		assertTrue(users.size() > 0);
	}
	
	@Test
	public void daoExceptionTest() throws SQLException {
		User user = new User();
		user.setId(1);
		user.setEmail("admin@library.com");
		user.setFirstName("Mykola");
		user.setLastName("Petrenko");
		user.setPhoneNumber("380970000000");
		user.setPassportNumber("AA000000");
		user.setRole(UserRole.ADMIN);
		user.setPassword("nimda");
		assertThrows(DaoException.class, () -> userDaoImpl.create(user));
		assertThrows(DaoException.class, () -> userDaoImpl.create(user));
		assertThrows(DaoException.class, () -> userDaoImpl.find(user));
		assertThrows(DaoException.class, () -> userDaoImpl.update(user));
		assertThrows(DaoException.class, () -> userDaoImpl.block(user));
		assertThrows(DaoException.class, () -> userDaoImpl.remove(user));
		assertThrows(DaoException.class, () -> userDaoImpl.findByLoginAndPassword(user.getEmail(), user.getPassword()));
		assertThrows(DaoException.class, () -> userDaoImpl.findByRole(user.getRole()));
		assertThrows(DaoException.class, () -> userDaoImpl.findByPhone(user.getPhoneNumber()));
	}
	
	@Test
	public void userCRUDTest() throws SQLException, DaoException {
		User expectedUser = new User();
		expectedUser.setEmail("reader11@gmail.com");
		expectedUser.setFirstName("Reader11");
		expectedUser.setLastName("Readerenko11");
		expectedUser.setPhoneNumber("380970000011");
		expectedUser.setPassportNumber("AA000011");
		expectedUser.setRole(UserRole.READER);
		expectedUser.setPassword("6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2");
		
		User actualUser = userDaoImpl.create(expectedUser);
		assertEquals(expectedUser, actualUser);

		expectedUser.setId(actualUser.getId());
		tuneMockDbManager();
		actualUser = userDaoImpl.find(expectedUser);
		assertEquals(expectedUser, actualUser);
		
		expectedUser.setPhoneNumber("380970000111");
		tuneMockDbManager();
		userDaoImpl.update(expectedUser);
		tuneMockDbManager();
		actualUser = userDaoImpl.find(expectedUser);
		assertEquals(expectedUser, actualUser);
		
		expectedUser.setBlocked(true);
		tuneMockDbManager();
		userDaoImpl.block(expectedUser);
		tuneMockDbManager();
		actualUser = userDaoImpl.find(expectedUser);
		assertEquals(expectedUser, actualUser);

		expectedUser.setBlocked(false);
		tuneMockDbManager();
		userDaoImpl.block(expectedUser);
		tuneMockDbManager();
		actualUser = userDaoImpl.find(expectedUser);
		assertEquals(expectedUser, actualUser);
		
		tuneMockDbManager();
		userDaoImpl.remove(expectedUser);
		tuneMockDbManager();
		actualUser = userDaoImpl.findByPhone(expectedUser.getPhoneNumber());
		assertNull(actualUser);
	}
}
