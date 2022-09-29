package org.example.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionPool {
	private static final String LOOKUP_NAME = "java:/comp/env";
	private static final String DATASOURCE_NAME = "jdbc/fplibrarydb";
	private static DataSource dataSource;
	
	static {
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup(LOOKUP_NAME);
			dataSource = (DataSource)envContext.lookup(DATASOURCE_NAME);
		} catch (NamingException e) {
			System.out.println(e);
		}		
	}
	
	private ConnectionPool() {}
	
	public static Connection getConnection() throws SQLException{
		System.out.println(dataSource);
		Connection con = dataSource.getConnection();
		System.out.println(con);
		return con;
	}

}
