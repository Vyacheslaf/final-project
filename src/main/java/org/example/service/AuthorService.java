package org.example.service;

import java.util.List;

import org.example.dao.AuthorDao;
import org.example.entity.Author;
import org.example.exception.DaoException;
import org.example.util.Config;

/**
 * The {@code AuthorService} class is used to provide interaction between controllers and {@code AuthorDao}
 * 
 * @author Vyacheslav Fedchenko
 */

public class AuthorService {
	
	private final AuthorDao authorDao;
	
    /**
     * Initializes a newly created {@code AuthorService} object with {@code AuthorDao} argument
     */
	public AuthorService(AuthorDao authorDao) {
		this.authorDao = authorDao;
	}
	
    /**
     * Initializes a newly created {@code AuthorService} object with {@code AuthorDao} 
     * produced by configured {@code DaoFactory}
     */
	public AuthorService() {
		this(Config.DAO_FACTORY.getAuthorDao());
	}

	/**
	 * Returns the list of authors received from {@code AuthorDao}
	 * 
	 * @return the list of authors received from {@code AuthorDao}
	 * @throws DaoException
	 */
	public List<Author> getAllAuthors() throws DaoException {
		return authorDao.findAllAuthor();
	}
}
