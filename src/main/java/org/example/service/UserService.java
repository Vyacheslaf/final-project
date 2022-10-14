package org.example.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.Config;
import org.example.dao.DaoFactory;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;

public class UserService {

	private static final String REQ_PARAM_EMAIL = "email";
	private static final String REQ_PARAM_PASSWORD = "password";
	private static final String REQ_PARAM_FIRST_NAME = "firstname";
	private static final String REQ_PARAM_LAST_NAME = "lastname";
	private static final String REQ_PARAM_PHONE_NUMBER = "phone";
	private static final String REQ_PARAM_PASSPORT_NUMBER = "passport";
	private static final String REQ_PARAM_ROLE = "role";
	private static final String REQ_PARAM_ID = "id";
	private static final String REQ_PARAM_BLOCKED = "blocked";

	public static User register(HttpServletRequest req) throws DaoException {
		User user = getUser(req);
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		UserDao userDao = daoFactory.getUserDao();
		user = userDao.create(user);
		return user;
	}
	
	public static User login(HttpServletRequest req) throws DaoException {
		User user = getUser(req);
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		UserDao userDao = daoFactory.getUserDao();
		user = userDao.findByLoginAndPassword(user.getEmail(), user.getPassword());
		return user;
	}

	public static User updateUserData(HttpServletRequest req) throws DaoException {
		User user = getUser(req);
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		UserDao userDao = daoFactory.getUserDao();
		userDao.update(user);
		user = userDao.find(user);
		return user;
	}

	public static User updateUserPassword(HttpServletRequest req) throws DaoException {
		User user = getUser(req);
		user.setPassword(DigestUtils.sha1Hex(user.getPassword()));
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		UserDao userDao = daoFactory.getUserDao();
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
		if (stringId != null && stringId.matches("\\d+")) {
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
		DaoFactory daoFactory = DaoFactory.getDaoFactory(Config.DAO_NAME);
		UserDao userDao = daoFactory.getUserDao();
		return userDao.findByPhone(phoneNumber);
	}

}
