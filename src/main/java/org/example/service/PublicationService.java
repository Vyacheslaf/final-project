package org.example.service;

import java.util.List;

import org.example.Config;
import org.example.dao.DaoFactory;
import org.example.dao.PublicationDao;
import org.example.entity.Publication;
import org.example.exception.DaoException;

public class PublicationService {
	
	public static List<Publication> getAllPublications() throws DaoException {
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		PublicationDao publicationDao = daoFactory.getPublicationDao();
		return publicationDao.findAllPublication();
	}

}
