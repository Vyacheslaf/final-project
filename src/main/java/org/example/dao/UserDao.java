package org.example.dao;

import java.util.List;

import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;

public interface UserDao extends Dao<User>{
	User findByLoginAndPassword(String login, String password) 
															throws DaoException;
	List<User> findByRole(UserRole userRole) throws DaoException;
}
