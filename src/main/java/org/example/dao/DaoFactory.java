package org.example.dao;

import org.example.dao.mysql.MysqlDaoFactory;

public abstract class DaoFactory {
	private static final String MYSQL_DBMS = "mysql";
	
	public abstract UserDao getUserDao();
	public abstract BookDao getBookDao();
	public abstract PublicationDao getPublicationDao();
	
	public static DaoFactory getDaoFactory(String dbmsName) {
		if (dbmsName != null) {
			switch (dbmsName.toLowerCase()) {
			case MYSQL_DBMS:
				return new MysqlDaoFactory();
			default:
				return null;
			}
		}
		return null;
	}
}
