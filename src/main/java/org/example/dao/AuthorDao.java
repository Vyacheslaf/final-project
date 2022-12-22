package org.example.dao;

import java.util.List;

import org.example.entity.Author;
import org.example.exception.DaoException;

/**
 * The {@code AuthorDao} interface declares additional methods to operate with {@code Author} objects, 
 * beyond those CRUD methods specified in the {@code Dao} interface.
 * 
 * @author Vyacheslav Fedchenko
 * @see org.example.dao.Dao
 */

public interface AuthorDao extends Dao<Author> {
	
	/**
	 * Returns list of all authors from the database
	 * 
	 * @return list of all authors from the database
	 * @throws DaoException
	 */
	List<Author> findAllAuthor() throws DaoException;
}
