package org.example.dao;

import org.example.entity.Publication;

public interface PublicationDao extends Dao<Publication>{
	Publication findByName(String publicationName);
}
