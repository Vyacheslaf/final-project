package org.example.service;

import java.util.List;

import org.example.Config;
import org.example.dao.AuthorDao;
import org.example.dao.DaoFactory;
import org.example.entity.Author;
import org.example.exception.DaoException;

public class AuthorService {

	public static List<Author> getAllAuthors() throws DaoException {
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		AuthorDao authorDao = daoFactory.getAuthorDao();
		return authorDao.findAllAuthor();
	}
}
