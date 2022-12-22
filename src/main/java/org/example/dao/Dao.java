package org.example.dao;

import org.example.exception.DaoException;

/**
 * The root interface for data access objects.
 * This interface is used to define CRUD operations with objects
 *
 * @param <T> the type of data access object
 * 
 * @author Vyacheslav Fedchenko
 */

public interface Dao<T> {
	
	/**
	 * Creates and returns a new object
	 * 
	 * @param entity
	 * 		  An object that must be created
	 * @return The created object
	 * @throws DaoException if the object cannot be created
	 */
	T create(T entity) throws DaoException;
	
	/**
	 * Searches a stored object
	 * 
	 * @param entity
	 * 		  An object to be found
	 * @return Found object
	 * @throws DaoException if the object cannot be found
	 */
	T find(T entity) throws DaoException;
	
	/**
	 * Updates a stored object
	 * 
	 * @param entity
	 * 		  An object to be updated
	 * @throws DaoException if the object cannot be updated
	 */
	void update(T entity) throws DaoException;
	
	/**
	 * Deletes a stored object
	 * 
	 * @param entity
	 * 		  An object to be deleted
	 * @throws DaoException if the object cannot be deleted
	 */
	void remove(T entity) throws DaoException;
}
