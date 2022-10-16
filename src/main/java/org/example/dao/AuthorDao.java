package org.example.dao;

import java.util.List;

import org.example.entity.Author;
import org.example.exception.DaoException;

public interface AuthorDao extends Dao<Author> {
	List<Author> findAllAuthor() throws DaoException;
}
