package org.example.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.example.util.Config;
//import org.example.dao.DaoFactory;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;
import org.example.exception.ServiceException;

import static org.example.util.Config.*;

public class UserService {

	private static final Logger LOG = LogManager.getLogger(UserService.class);
	private static final String REQ_PARAM_EMAIL = "email";
	private static final String REQ_PARAM_PASSWORD = "password";
	private static final String REQ_PARAM_FIRST_NAME = "firstname";
	private static final String REQ_PARAM_LAST_NAME = "lastname";
	private static final String REQ_PARAM_PHONE_NUMBER = "phone";
	private static final String REQ_PARAM_PASSPORT_NUMBER = "passport";
	private static final String REQ_PARAM_ROLE = "role";
	private static final String REQ_PARAM_ID = "id";
	private static final String REQ_PARAM_BLOCKED = "blocked";
	private static final String ONLY_DIGITS_REGEX = "\\d+";
	private static final String ERROR_CANNOT_DELETE_USER_WRONG_ID = "error.cannot.delete.user.wrong.id";
	private static final String ERROR_CANNOT_BLOCK_USER_WRONG_ID = "error.cannot.block.user.wrong.id";

	public static User register(HttpServletRequest req) throws DaoException {
		User user = getUser(req);
		UserDao userDao = DAO_FACTORY.getUserDao();
		user.setPassword(DigestUtils.sha1Hex(user.getPassword()));
		user = userDao.create(user);
		return user;
	}
	
	public static User login(HttpServletRequest req) throws DaoException {
		User user = getUser(req);
		UserDao userDao = DAO_FACTORY.getUserDao();
		user = userDao.findByLoginAndPassword(user.getEmail(), DigestUtils.sha1Hex(user.getPassword()));
		return user;
	}

	public static User updateUserData(HttpServletRequest req) throws DaoException {
		User user = getUser(req);
		UserDao userDao = DAO_FACTORY.getUserDao();
		userDao.update(user);
		user = userDao.find(user);
		return user;
	}

	public static User updateUserPassword(HttpServletRequest req) throws DaoException {
		User user = getUser(req);
		user.setPassword(DigestUtils.sha1Hex(user.getPassword()));
		UserDao userDao = DAO_FACTORY.getUserDao();
		userDao.update(user);
		user = userDao.find(user);
		return user;
	}
	
	private static User getUser(HttpServletRequest req) {
		User user = new User();
		user.setId(getId(req));
		user.setEmail(req.getParameter(REQ_PARAM_EMAIL));
		user.setPassword(req.getParameter(REQ_PARAM_PASSWORD));
		user.setFirstName(req.getParameter(REQ_PARAM_FIRST_NAME));
		user.setLastName(req.getParameter(REQ_PARAM_LAST_NAME));
		user.setPhoneNumber(req.getParameter(REQ_PARAM_PHONE_NUMBER));
		user.setPassportNumber(req.getParameter(REQ_PARAM_PASSPORT_NUMBER));
		user.setBlocked(Boolean.getBoolean(req.getParameter(REQ_PARAM_BLOCKED)));
		user.setRole(getRole(req));
		return user;
	}
	
	private static int getId(HttpServletRequest req) {
		String stringId = req.getParameter(REQ_PARAM_ID);
		if (stringId != null && stringId.matches(ONLY_DIGITS_REGEX)) {
			return Integer.parseInt(stringId);
		}
		return 0;
	}
	
	private static UserRole getRole(HttpServletRequest req) {
		String role = req.getParameter(REQ_PARAM_ROLE);
		if (role != null) {
			return UserRole.valueOf(role.toUpperCase());
		}
		return null;
	}

	public static User findUserByPhone(String phoneNumber) throws DaoException {
		UserDao userDao = DAO_FACTORY.getUserDao();
		return userDao.findByPhone(phoneNumber);
	}

	public static List<User> findUsersByRole(UserRole userRole) throws DaoException {
		UserDao userDao = DAO_FACTORY.getUserDao();
		return userDao.findByRole(userRole);
	}

	public static void deleteUser(String userId) throws ServiceException, DaoException {
		if (userId == null || !userId.matches(ONLY_DIGITS_REGEX)) {
			logAndThrowException(ERROR_CANNOT_DELETE_USER_WRONG_ID);
		}
		UserDao userDao = DAO_FACTORY.getUserDao();
		User user = new User();
		user.setId(Integer.parseInt(userId));
		userDao.remove(user);
	}

	
	private static void logAndThrowException(String message) throws ServiceException {
		LOG.error(message);
		throw new ServiceException(message);
	}

	public static List<User> findFinedUsers() throws DaoException {
		UserDao userDao = DAO_FACTORY.getUserDao();
		return userDao.findByFine();
	}

	public static void blockUser(HttpServletRequest req) throws DaoException, ServiceException {
		String userId = req.getParameter(REQ_PARAM_ID);
		if (userId == null || !userId.matches(ONLY_DIGITS_REGEX)) {
			logAndThrowException(ERROR_CANNOT_BLOCK_USER_WRONG_ID);
		}
		UserDao userDao = DAO_FACTORY.getUserDao();
		User user = new User();
		user.setId(Integer.parseInt(userId));
		user.setBlocked(Boolean.parseBoolean(req.getParameter(REQ_PARAM_BLOCKED)));
		userDao.block(user);
	}
}
