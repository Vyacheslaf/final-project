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
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class UserDaoImplTest {
	
	private final static String DB_URL = "jdbc:mysql://localhost:3306/fplibrarydb?user=libadmin&password=111";
	private final static String INIT_SQL_SCRIPT = "sql/UserDaoImplTest.sql";
	
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
	public void findUserByIdTest() throws DaoException, SQLException {
		User expectedUser = new User();
		expectedUser.setId(1);
		expectedUser.setEmail("admin@library.com");
		expectedUser.setFirstName("Mykola");
		expectedUser.setLastName("Petrenko");
		expectedUser.setPhoneNumber("380970000000");
		expectedUser.setPassportNumber("AA000000");
		expectedUser.setRole(UserRole.ADMIN);
		
		User actualUser = new User();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualUser = UserDaoImpl.find(con, expectedUser);
		}
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
		
		User actualUser = new User();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualUser = UserDaoImpl.findByPhone(con, expectedUser.getPhoneNumber());
		}
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
		
		User actualUser = new User();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualUser = UserDaoImpl.findByLoginAndPassword(con, expectedUser.getEmail(), password);
		}
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
		User actualUser = new User();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualUser = UserDaoImpl.findByLoginAndPassword(con, email, password);
		}
		assertEquals(null, actualUser);
	}

	@ParameterizedTest
	@CsvSource({
		"admin",
		"librarian",
		"reader"
	})
	public void findUserByRole(String role) throws SQLException, DaoException {
		UserRole userRole = UserRole.valueOf(role.toUpperCase());
		List<User> users = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			users = UserDaoImpl.findByRole(con, userRole);
		}
		assertEquals(true, users.size() > 0);
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
		Connection con = DriverManager.getConnection(DB_URL);
		assertThrows(DaoException.class, () -> UserDaoImpl.create(con, user));
//		con.close();
		assertThrows(DaoException.class, () -> UserDaoImpl.create(con, user));
		assertThrows(DaoException.class, () -> UserDaoImpl.find(con, user));
		assertThrows(DaoException.class, () -> UserDaoImpl.update(con, user));
		assertThrows(DaoException.class, () -> UserDaoImpl.block(con, user));
		assertThrows(DaoException.class, () -> UserDaoImpl.remove(con, user));
		assertThrows(DaoException.class, () -> UserDaoImpl.findByLoginAndPassword(con, user.getEmail(), user.getPassword()));
		assertThrows(DaoException.class, () -> UserDaoImpl.findByRole(con, user.getRole()));
		assertThrows(DaoException.class, () -> UserDaoImpl.findByPhone(con, user.getPhoneNumber()));
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
		
		User actualUser = new User();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualUser = UserDaoImpl.create(con, expectedUser);
		}
		assertEquals(expectedUser, actualUser);

		expectedUser.setId(actualUser.getId());
		actualUser = new User();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualUser = UserDaoImpl.find(con, expectedUser);
		}
		assertEquals(expectedUser, actualUser);
		
		expectedUser.setPhoneNumber("380970000111");
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			UserDaoImpl.update(con, expectedUser);
		}
		 
		actualUser = new User();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualUser = UserDaoImpl.find(con, expectedUser);
		}
		assertEquals(expectedUser, actualUser);
		
		expectedUser.setBlocked(true);
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			UserDaoImpl.block(con, expectedUser);
		}
 
		actualUser = new User();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualUser = UserDaoImpl.find(con, expectedUser);
		}
		assertEquals(expectedUser, actualUser);

		expectedUser.setBlocked(false);
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			UserDaoImpl.block(con, expectedUser);
		}
 
		actualUser = new User();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualUser = UserDaoImpl.find(con, expectedUser);
		}
		assertEquals(expectedUser, actualUser);
		
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			UserDaoImpl.remove(con, expectedUser);
		}
		
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			actualUser = UserDaoImpl.findByPhone(con, expectedUser.getPhoneNumber());
		}
		assertEquals(null, actualUser);
	}
}
