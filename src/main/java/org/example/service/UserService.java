package org.example.service;

import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;
import org.example.exception.ServiceException;
import org.example.util.Config;

/**
 * The {@code UserService} class is used to provide interaction between controllers and {@code UserDao}
 * 
 * @author Vyacheslav Fedchenko
 */

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
	private static final String ONLY_DIGITS_REGEX = "^\\d+$";
	private static final String EMAIL_REGEX = "^[\\w-]+@[\\w-]+\\.[A-Za-z]+$";
	private static final String FIRST_NAME_REGEX = "^[\\wА-яІіЇїЄє'Ґґ -]+$";
	private static final String LAST_NAME_REGEX = "^[\\wА-яІіЇїЄє'Ґґ-]+$";
	private static final String PHONE_NUMBER_REGEX = "^\\d{12}$";
	private static final String PASSPORT_NUMBER_REGEX = "^\\w+$";
	private static final String SHA1_REGEX = "^[0-9a-f]{40}$";
	private static final String ERROR_CANNOT_DELETE_USER_WRONG_ID = "error.cannot.delete.user.wrong.id";
	private static final String ERROR_CANNOT_DELETE_USER_WRONG_ROLE = "error.cannot.delete.user.wrong.role";
	private static final String ERROR_CANNOT_BLOCK_USER_WRONG_ID = "error.cannot.block.user.wrong.id";
	private static final String ERROR_CANNOT_BLOCK_USER_WRONG_ROLE = "error.cannot.block.user.wrong.role";
	private static final String ERROR_WRONG_EMAIL = "error.wrong.email";
	private static final String ERROR_WRONG_PASSWORD = "error.wrong.password";
	private static final String ERROR_WRONG_USER_ROLE = "error.wrong.user.role";
	private static final String ERROR_WRONG_USER_DATA = "error.wrong.user.data";
	private static final String ERROR_WRONG_USER_ID = "error.wrong.user.id";
	private static final String ERROR_WRONG_PHONE_NUMBER = "error.wrong.phone.number";
	
	private final UserDao userDao;
	
    /**
     * Initializes a newly created {@code UserService} object with {@code UserDao} argument
     */
	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}
	
    /**
     * Initializes a newly created {@code UserService} object with {@code UserDao} 
     * produced by configured {@code DaoFactory}
     */
	public UserService() {
		this(Config.DAO_FACTORY.getUserDao());
	}

	/**
	 * Extracts from the {@code HttpServletRequest} new user's data and create a new user with this data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing new user's data
	 * @return a newly created {@code User}
	 * @throws DaoException if cannot create a {@code User}
	 * @throws ServiceException if cannot get user's data from the {@code HttpServletRequest} or user's data is wrong
	 */
	public User register(HttpServletRequest req) throws DaoException, ServiceException {
		User user = getUser(req);
		checkUserRole(user.getRole());
		checkUserData(user);
		user = userDao.create(user);
		return user;
	}

	/**
	 * Extracts user's email and user's password from the {@code HttpServletRequest} 
	 * and find the user with such data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing user's login and user's password
	 * @return the {@code User} with such email and password or <i>null</i> if cannot find the user with such data
	 * @throws DaoException if cannot get the {@code User} from the {@code UserDao}
	 * @throws ServiceException if cannot get user's email or user's password from the {@code HttpServletRequest}
	 */
	public User login(HttpServletRequest req) throws DaoException, ServiceException {
		User user = getUser(req);
		user = userDao.findByLoginAndPassword(user.getEmail(), user.getPassword());
		return user;
	}

	/**
	 * Obtains new data of the user with selected ID from the {@code HttpServletRequest} 
	 * and saves it to the {@code UserDao}
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing user's data
	 * @return the {@code User} with updated data
	 * @throws DaoException if cannot update data for the user
	 * @throws ServiceException if cannot get user's data from the {@code HttpServletRequest}
	 */
	public User updateUserData(HttpServletRequest req) throws DaoException, ServiceException {
		User user = getUser(req);
		checkUserId(user);
		checkUserData(user);
		userDao.update(user);
		return userDao.find(user);
	}

	/**
	 * Find the user with selected phone number
	 * 
	 * @param phoneNumber
	 * 		  The {@code String} containing user's phone number
	 * @return the {@code User} with selected phone number or <i>null</i> if user with such phone number does not exist
	 * @throws DaoException if cannot get the user from the {@code UserDao}
	 * @throws ServiceException if cannot get user's phone number from the {@code HttpServletRequest}
	 */
	public User findUserByPhone(String phoneNumber) throws DaoException, ServiceException {
		checkPhoneNumber(phoneNumber);
		return userDao.findByPhone(phoneNumber);
	}

	/**
	 * Returns the list of users with selected {@code UserRole}
	 * 
	 * @param userRole
	 * 		  The {@code UserRole}
	 * @return the list of users with selected {@code UserRole}
	 * @throws DaoException if cannot get the list of users
	 * @throws ServiceException if <i>userRole</i> is wrong
	 */
	public List<User> findUsersByRole(UserRole userRole) throws DaoException, ServiceException {
		checkUserRole(userRole);
		return userDao.findByRole(userRole);
	}

	/**
	 * Extracts librarian's ID from the {@code HttpServletRequest} and deletes the librarian with selected ID
	 * 
	 * @param userId
	 * 		  The {@code HttpServletRequest} containing ID of the selected librarian
	 * @throws ServiceException if ID is wrong or if the user with selected ID is not a librarian
	 * @throws DaoException if cannot find or remove librarian with such ID
	 */
//	public void deleteUser(String userId) throws ServiceException, DaoException {
	public void deleteLibrarian(HttpServletRequest req) throws ServiceException, DaoException {
		int userId = getUserId(req, ERROR_CANNOT_DELETE_USER_WRONG_ID);
//		checkUserId(userId, ERROR_CANNOT_DELETE_USER_WRONG_ID);
		User user = new User();
//		user.setId(Integer.parseInt(userId));
		user.setId(userId);
		user = userDao.find(user);
		if (!user.getRole().equals(UserRole.LIBRARIAN)) {
			logAndThrowException(ERROR_CANNOT_DELETE_USER_WRONG_ROLE);
		}
		userDao.remove(user);
	}

	/**
	 * Returns the list of blocked users or users which did not return the book on time
	 * 
	 * @return the list of blocked users or users which did not return the book on time
	 * @throws DaoException if cannot get the list of users
	 */
	public List<User> findFinedUsers() throws DaoException {
		return userDao.findByFine();
	}

	/**
	 * Extracts reader's ID from the {@code HttpServletRequest} and block the reader with selected ID
	 * if the {@code HttpServletRequest} contains parameter <i>blocked</i> that equals {@code true} 
	 * or unblock otherwise
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing reader's ID
	 * @throws DaoException if cannot block the reader
	 * @throws ServiceException if ID is wrong or if the user with selected ID is not a reader
	 */
	public void blockUser(HttpServletRequest req) throws DaoException, ServiceException {
//		String userId = req.getParameter(REQ_PARAM_ID);
//		checkUserId(userId, ERROR_CANNOT_BLOCK_USER_WRONG_ID);
		int userId = getUserId(req, ERROR_CANNOT_BLOCK_USER_WRONG_ID);
		User user = new User();
//		user.setId(Integer.parseInt(userId));
		user.setId(userId);
		user = userDao.find(user);
		if (!user.getRole().equals(UserRole.READER)) {
			logAndThrowException(ERROR_CANNOT_BLOCK_USER_WRONG_ROLE);
		}
		user.setBlocked(Boolean.parseBoolean(req.getParameter(REQ_PARAM_BLOCKED)));
		userDao.block(user);
	}

	/**
	 * Extracts user's data from the {@code HttpServletRequest} and returns the {@code User} with these data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest} containing user's data
	 * @return the {@code User} with extracted user's data
	 * @throws ServiceException if cannot get user's data from the {@code HttpServletRequest}
	 */
	private static User getUser(HttpServletRequest req) throws ServiceException {
		User user = new User();
		user.setId(getUserId(req));
		user.setEmail(getEmail(req));
		user.setPassword(getPassword(req));
		user.setFirstName(getFirstName(req));
		user.setLastName(getLastName(req));
		user.setPhoneNumber(getPhoneNumber(req));
		user.setPassportNumber(getPassportNumber(req));
		user.setBlocked(Boolean.getBoolean(req.getParameter(REQ_PARAM_BLOCKED)));
		user.setRole(getRole(req));
		return user;
	}
	
	/**
	 * Returns user's ID from the {@code HttpServletRequest} or <i>0</i> if it does not contain valid data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest}
	 * @return user's ID if the {@code HttpServletRequest} contains <i>id</i> parameter with valid data,
	 * 		   <i>0</i> otherwise
	 */
	private static int getUserId(HttpServletRequest req) {
		String stringId = req.getParameter(REQ_PARAM_ID);
		if (stringId != null && stringId.matches(ONLY_DIGITS_REGEX)) {
			return Integer.parseInt(stringId);
		}
		return 0;
	}
	
	/**
	 * Returns user's ID from the {@code HttpServletRequest} 
	 * or throws {@code ServiceException} if it does not contain valid data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest}
	 * @param errorMessage 
	 * 		  The {@code String} containing message for the {@code ServiceException}
	 * @return ID of the user
	 * @throws ServiceException if the {@code HttpServletRequest} does not contain valid user's ID
	 */
	private static int getUserId(HttpServletRequest req, String errorMessage) throws ServiceException {
		int userId = getUserId(req);
		if (userId == 0) {
			logAndThrowException(errorMessage);
		}
		return userId;
	}
	
	/**
	 * Returns user's email from the {@code HttpServletRequest} 
	 * or throws {@code ServiceException} if it does not contain valid data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest}
	 * @return the {@code String} containing an email of the user
	 * @throws ServiceException if the {@code HttpServletRequest} does not contain valid user's email
	 */
	private static String getEmail(HttpServletRequest req) throws ServiceException {
		String email = req.getParameter(REQ_PARAM_EMAIL);
		if (email == null ||!email.matches(EMAIL_REGEX)) {
			logAndThrowException(ERROR_WRONG_EMAIL);
		}
		return email;
	}
	
	/**
	 * Returns the SHA-1 digest of user's password from the {@code HttpServletRequest} 
	 * or throws {@code ServiceException} if it does not contain valid data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest}
	 * @return the SHA-1 digest of user's password as a hex string
	 * @throws ServiceException if the {@code HttpServletRequest} does not contain valid data
	 */
	private static String getPassword(HttpServletRequest req) throws ServiceException {
		String password = req.getParameter(REQ_PARAM_PASSWORD);
		if (password == null || password.equals("")) {
			logAndThrowException(ERROR_WRONG_PASSWORD);
		}
		if (password.matches(SHA1_REGEX)) {
			return password;
		}
		return DigestUtils.sha1Hex(password);
	}

	/**
	 * Returns user's first name from the {@code HttpServletRequest} 
	 * or {@code null} if it does not contain valid data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest}
	 * @return the {@code String} containing a first name of the user 
	 * 		   or {@code null} if the {@code HttpServletRequest} does not contain valid data
	 */
	private static String getFirstName(HttpServletRequest req) {
		String firstName = req.getParameter(REQ_PARAM_FIRST_NAME);
		if (firstName == null ||!firstName.matches(FIRST_NAME_REGEX)) {
			return null;
		}
		return firstName;
	}

	/**
	 * Returns user's last name from the {@code HttpServletRequest} 
	 * or {@code null} if it does not contain valid data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest}
	 * @return the {@code String} containing a last name of the user
	 * 		   or {@code null} if the {@code HttpServletRequest} does not contain valid data
	 */
	private static String getLastName(HttpServletRequest req) {
		String lastName = req.getParameter(REQ_PARAM_LAST_NAME);
		if (lastName == null ||!lastName.matches(LAST_NAME_REGEX)) {
			return null;
		}
		return lastName;
	}

	/**
	 * Returns user's phone number from the {@code HttpServletRequest} 
	 * or {@code null} if it does not contain valid data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest}
	 * @return the {@code String} containing a phone number of the user
	 * 		   or {@code null} if the {@code HttpServletRequest} does not contain valid data
	 */
	private static String getPhoneNumber(HttpServletRequest req) {
		String phoneNumber = req.getParameter(REQ_PARAM_PHONE_NUMBER);
		if (phoneNumber == null ||!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
			return null;
		}
		return phoneNumber;
	}

	/**
	 * Returns user's document number from the {@code HttpServletRequest} 
	 * or {@code null} if it does not contain valid data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest}
	 * @return the {@code String} containing a document number of the user
	 * 		   or {@code null} if the {@code HttpServletRequest} does not contain valid data
	 */
	private static String getPassportNumber(HttpServletRequest req) {
		String passportNumber = req.getParameter(REQ_PARAM_PASSPORT_NUMBER);
		if (passportNumber == null ||!passportNumber.matches(PASSPORT_NUMBER_REGEX)) {
			return null;
		}
		return passportNumber;
	}
	
	/**
	 * Returns user's role from the {@code HttpServletRequest} 
	 * or {@code null} if it does not contain valid data
	 * 
	 * @param req
	 * 		  The {@code HttpServletRequest}
	 * @return the {@code UserRole} of the user
	 * 		   or {@code null} if the {@code HttpServletRequest} does not contain valid data
	 */
	private static UserRole getRole(HttpServletRequest req) {
		String role = req.getParameter(REQ_PARAM_ROLE);
		if (role != null && Stream.of(UserRole.values()).anyMatch(e -> e.toString().equalsIgnoreCase(role))) {
			return UserRole.valueOf(role.toUpperCase());
		}
		return null;
	}
	
	/**
	 * Throws the {@code ServiceException} if the {@code UserRole} not equals to <i>READER</i> or <i>LIBRARIAN</i>
	 * 
	 * @param userRole
	 * 		  The {@code UserRole}
	 * @throws ServiceException if the {@code UserRole} not equals to <i>READER</i> or <i>LIBRARIAN</i>
	 */
	private static void checkUserRole(UserRole userRole) throws ServiceException {
		if (userRole == null || userRole.equals(UserRole.ADMIN)) {
			logAndThrowException(ERROR_WRONG_USER_ROLE);
		}
	}

	/**
	 * Throws the {@code ServiceException} if ID of the {@code User} equals to <i>0</i>
	 * 
	 * @param user
	 * 		  The {@code User}
	 * @throws ServiceException if ID of the {@code User} equals to <i>0</i>
	 */
	private static void checkUserId(User user) throws ServiceException {
		if (user.getId() == 0) {
			logAndThrowException(ERROR_WRONG_USER_ID);
		}
	}

/*	private static void checkUserId(String userId, String errorMessage) throws ServiceException {
		if (userId == null ||!userId.matches(ONLY_DIGITS_REGEX) || Integer.parseInt(userId) == 0) {
			logAndThrowException(errorMessage);
		}
	}*/
	
	/**
	 * Throws the {@code ServiceException} if the {@code String} does not match to a phone number
	 * 
	 * @param phoneNumber
	 * 		  The {@code String} containing a phone number
	 * @throws ServiceException if the {@code String} does not match to a phone number
	 */
	private static void checkPhoneNumber(String phoneNumber) throws ServiceException {
		if (phoneNumber == null ||!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
			logAndThrowException(ERROR_WRONG_PHONE_NUMBER);
		}
	}
	
	/**
	 * Throws the {@code ServiceException} if the {@code User} does not contain any of following data:
	 * the first name, the last name, the phone number or the document number
	 * 
	 * @param user
	 * 		  The {@code User}
	 * @throws ServiceException if the {@code User} does not contain any of following data:
	 * 		   the first name, the last name, the phone number or the document number
	 */
	private static void checkUserData(User user) throws ServiceException {
		if (user.getFirstName() == null 
				|| user.getLastName() == null 
				|| user.getPhoneNumber() == null 
				|| user.getPassportNumber() == null) {
			logAndThrowException(ERROR_WRONG_USER_DATA);
		}
	}

	/**
	 * Sends an error message to the log and throws the {@code ServiceException} with this message
	 * 
	 * @param message
	 * 		  an error message for the log and the {@code ServiceException}
	 * @throws ServiceException
	 */
	private static void logAndThrowException(String message) throws ServiceException {
		LOG.error(message);
		throw new ServiceException(message);
	}
}
