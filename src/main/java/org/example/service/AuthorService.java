package org.example.service;

import static org.example.util.Config.*;

import java.util.List;

import org.example.dao.AuthorDao;
import org.example.entity.Author;
import org.example.exception.DaoException;

public class AuthorService {

	public static List<Author> getAllAuthors() throws DaoException {
		AuthorDao authorDao = DAO_FACTORY.getAuthorDao();
		return authorDao.findAllAuthor();
	}
}
