package org.example.dao.mysql;

import org.example.dao.BookDao;
import org.example.dao.DaoFactory;
import org.example.dao.OrderDao;
import org.example.dao.PublicationDao;
import org.example.dao.UserDao;

public class MysqlDaoFactory extends DaoFactory{

	@Override
	public UserDao getUserDao() {
		return new UserDaoImpl();
	}

	@Override
	public BookDao getBookDao() {
		return new BookDaoImpl();
	}

	@Override
	public PublicationDao getPublicationDao() {
		return new PublicationDaoImpl();
	}

	@Override
	public OrderDao getOrderDao() {
		return new OrderDaoImpl();
	}

}
