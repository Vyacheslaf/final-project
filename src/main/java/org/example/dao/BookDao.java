package org.example.dao;

import org.example.entity.Book;

public interface BookDao extends Dao<Book>{
	Book findByTitleOrPublication(String title, String publication);
}
