package org.example.service;

import java.util.List;

import org.example.util.Config;
import org.example.dao.DaoFactory;
import org.example.dao.PublicationDao;
import org.example.entity.Publication;
import org.example.exception.DaoException;

public class PublicationService {
	
	public static List<Publication> getAllPublications() throws DaoException {
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DBMS);
		PublicationDao publicationDao = daoFactory.getPublicationDao();
		return publicationDao.findAllPublication();
	}

}
