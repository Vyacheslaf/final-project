package org.example.dao;

import org.example.dao.mysql.MysqlDaoFactory;

/**
 * The {@code DaoFactory} class is used to produce factories which then produce data access objects
 * 
 * @author Vyacheslav Fedchenko
 */

public abstract class DaoFactory {
	
	/** The name of <i>dbms</i> property for MySQL DBMS */
	private static final String MYSQL_DBMS = "mysql";
	
	/**
	 * Creates new {@code UserDao} object
	 * 
	 * @return new {@code UserDao} object
	 * @see UserDao
	 */
	public abstract UserDao getUserDao();

	/**
	 * Creates new {@code BookDao} object
	 * 
	 * @return new {@code BookDao} object
	 * @see BookDao
	 */
	public abstract BookDao getBookDao();

	/**
	 * Creates new {@code PublicationDao} object
	 * 
	 * @return new {@code PublicationDao} object
	 * @see PublicationDao
	 */
	public abstract PublicationDao getPublicationDao();

	/**
	 * Creates new {@code OrderDao} object
	 * 
	 * @return new {@code OrderDao} object
	 * @see OrderDao
	 */
	public abstract OrderDao getOrderDao();

	/**
	 * Creates new {@code AuthorDao} object
	 * 
	 * @return new {@code AuthorDao} object
	 * @see AuthorDao
	 */
	public abstract AuthorDao getAuthorDao();
	
	/**
	 * Returns the {@code DaoFactory} corresponding to selected DBMS
	 * 
	 * @param dbmsName
	 * 		  Selected DBMS's name
	 * @return the {@code DaoFactory} corresponding to selected DBMS
	 */
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
