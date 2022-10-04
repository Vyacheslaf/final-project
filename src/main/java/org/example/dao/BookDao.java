package org.example.dao;

import java.util.List;

import org.example.entity.Book;
import org.example.exception.DaoException;

public interface BookDao extends Dao<Book>{
	List<Book> findBooks(String searchText, String orderBy, String orderType,
							int limit, int offset) throws DaoException;
	int countBooks(String searchText) throws DaoException;
}
