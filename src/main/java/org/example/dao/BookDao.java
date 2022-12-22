package org.example.dao;

import java.util.List;

import org.example.entity.Book;
import org.example.exception.DaoException;

/**
 * The {@code BookDao} interface declares additional methods to operate with {@code Book} objects, 
 * beyond those CRUD methods specified in the {@code Dao} interface.
 * 
 * @author Vyacheslav Fedchenko
 * @see org.example.dao.Dao
 */

public interface BookDao extends Dao<Book> {

	/**
	 * Returns the limited ordered list of books containing search phrase from the database
	 * 
	 * @param searchText
	 * 		  Search expression that must be included in book's title or in full name of book's author
	 * @param orderBy
	 * 		  A string defining the parameter by which the list is ordered
	 * @param orderType
	 * 		  A string defining ascending or descending ordering type
	 * @param limit
	 * 		  The number of elements in the list
	 * @param offset
	 * 		  Offset relative to the list of all books found
	 * @return the limited ordered list of books containing search phrase
	 * @throws DaoException
	 */
	List<Book> findBooks(String searchText, String orderBy, String orderType,
						 int limit, int offset) throws DaoException;
	
	/**
	 * Returns the number of all books containing search phrase from the database
	 * 
	 * @param searchText
	 * 		  Search expression that must be included in book's title or in full name of book's author
	 * @return the number of all books containing search phrase from the database
	 * @throws DaoException
	 */
	int countBooks(String searchText) throws DaoException;
}
