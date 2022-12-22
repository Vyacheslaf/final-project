package org.example.dao;

import java.util.List;

import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;

/**
 * The {@code UserDao} interface declares additional methods to operate with {@code User} objects, 
 * beyond those CRUD methods specified in the {@code Dao} interface.
 * 
 * @author Vyacheslav Fedchenko
 * @see org.example.dao.Dao
 */

public interface UserDao extends Dao<User> {
	
	/**
	 * Returns the user with the given login and password or {@code null} if user not found
	 * 
	 * @param login
	 * 		  User's login
	 * @param password
	 * 		  User's password
	 * @return the user with the given login and password or {@code null} if user not found
	 * @throws DaoException if cannot get the user
	 */
	User findByLoginAndPassword(String login, String password) throws DaoException;
	
	/**
	 * Returns the list of all users with selected role
	 * 
	 * @param userRole
	 * 		  Selected users' role
	 * @return the list of all users with selected role
	 * @throws DaoException if cannot get list of users
	 */
	List<User> findByRole(UserRole userRole) throws DaoException;
	
	/**
	 * Returns the user with given phone number
	 * 
	 * @param phoneNumber
	 * 		  User's phone number
	 * @return the user with given phone number
	 * @throws DaoException if cannot get user
	 */
	User findByPhone(String phoneNumber) throws DaoException;
	
	/**
	 * Return the list of all users who did not return orders on time
	 * 
	 * @return the list of all users who did not return orders on time
	 * @throws DaoException if cannot get list of users
	 */
	List<User> findByFine() throws DaoException;
	
	/**
	 * Blocks/unblocks selected user
	 * 
	 * @param user
	 * 		  Selected user
	 * @throws DaoException if cannot block/unblock the user
	 */
	void block(User user) throws DaoException;
}
