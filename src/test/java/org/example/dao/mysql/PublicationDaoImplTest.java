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
import org.example.entity.Publication;
import org.example.exception.DaoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PublicationDaoImplTest {

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
	public void findAllPublicationTest() throws SQLException, DaoException {
		List<Publication> publications = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(DB_URL)) {
			publications = PublicationDaoImpl.findAllPublication(con);
		}
		assertEquals(3, publications.size());
	}

	@Test
	public void daoExceptionTest() throws SQLException, DaoException {
		Connection con = DriverManager.getConnection(DB_URL);
		PublicationDaoImpl.findAllPublication(con);
		assertThrows(DaoException.class, () -> PublicationDaoImpl.findAllPublication(con));
	}
}
