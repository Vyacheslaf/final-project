package org.example.service;

import static org.example.util.Config.*;

import java.util.List;

import org.example.dao.PublicationDao;
import org.example.entity.Publication;
import org.example.exception.DaoException;

public class PublicationService {
	
	public static List<Publication> getAllPublications() throws DaoException {
		PublicationDao publicationDao = DAO_FACTORY.getPublicationDao();
		return publicationDao.findAllPublication();
	}

}
