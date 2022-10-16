package org.example.dao;

import java.util.List;

import org.example.entity.Publication;
import org.example.exception.DaoException;

public interface PublicationDao extends Dao<Publication>{
	Publication findByName(String publicationName);
	List<Publication> findAllPublication() throws DaoException;
}
