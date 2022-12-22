package org.example.service;

import java.util.List;

import org.example.dao.PublicationDao;
import org.example.entity.Publication;
import org.example.exception.DaoException;
import org.example.util.Config;

/**
 * The {@code PublicationService} class is used to provide interaction between controllers and {@code PublicationDao}
 * 
 * @author Vyacheslav Fedchenko
 */

public class PublicationService {
	
	private final PublicationDao publicationDao;
	
    /**
     * Initializes a newly created {@code PublicationService} object with {@code PublicationDao} argument
     */
	public PublicationService(PublicationDao publicationDao) {
		this.publicationDao = publicationDao;
	}
	
    /**
     * Initializes a newly created {@code PublicationService} object with {@code PublicationDao} 
     * produced by configured {@code DaoFactory}
     */
	public PublicationService() {
		this(Config.DAO_FACTORY.getPublicationDao());
	}

	/**
	 * Returns the list of all publications
	 * 
	 * @return the list of all publications
	 * @throws DaoException if cannot get the list of publications
	 */
	public List<Publication> getAllPublications() throws DaoException {
		return publicationDao.findAllPublication();
	}

}
