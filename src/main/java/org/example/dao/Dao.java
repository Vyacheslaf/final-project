package org.example.dao;

import org.example.exception.DaoException;

public interface Dao<T> {
	T create(T entity) throws DaoException;
	T find(T entity) throws DaoException;
	void update(T entity) throws DaoException;
	void remove(T entity) throws DaoException;
}
