package org.example.dao;

import java.util.List;

import org.example.entity.Publication;
import org.example.exception.DaoException;

/**
 * The {@code PublicationDao} interface declares additional methods to operate with {@code Publication} objects, 
 * beyond those CRUD methods specified in the {@code Dao} interface.
 * 
 * @author Vyacheslav Fedchenko
 * @see org.example.dao.Dao
 */

public interface PublicationDao extends Dao<Publication> {
	
	/**
	 * Returns the list of all publications
	 * 
	 * @return the list of all publications
	 * @throws DaoException 
	 */
	List<Publication> findAllPublication() throws DaoException;
}
