package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.servlet.http.HttpServletRequest;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.exception.DaoException;
import org.example.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

public class UserServiceTest {

	private static final String REQ_PARAM_EMAIL = "email";
	private static final String REQ_PARAM_PASSWORD = "password";
	private static final String REQ_PARAM_FIRST_NAME = "firstname";
	private static final String REQ_PARAM_LAST_NAME = "lastname";
	private static final String REQ_PARAM_PHONE_NUMBER = "phone";
	private static final String REQ_PARAM_PASSPORT_NUMBER = "passport";
	private static final String REQ_PARAM_ROLE = "role";
	private static final String ERROR_WRONG_EMAIL = "error.wrong.email";
	private static final String ERROR_WRONG_PASSWORD = "error.wrong.password";
	private static final String ERROR_WRONG_USER_ROLE = "error.wrong.user.role";
	private static final String ERROR_WRONG_USER_DATA = "error.wrong.user.data";
	
	private HttpServletRequest req;
	
	@BeforeEach
	public void prepareMockHttpServletRequest() {
		String email = "reader@gmail.com";
		String password = "111";
		String firstName = "Reader";
		String lastName = "Readerenko";
		String phoneNumber = "333333333333";
		String passportNumber = "AA111111";
		String role = "reader";
		req = Mockito.mock(HttpServletRequest.class);
		Mockito.when(req.getParameter(REQ_PARAM_EMAIL)).thenReturn(email);
		Mockito.when(req.getParameter(REQ_PARAM_PASSWORD)).thenReturn(password);
		Mockito.when(req.getParameter(REQ_PARAM_FIRST_NAME)).thenReturn(firstName);
		Mockito.when(req.getParameter(REQ_PARAM_LAST_NAME)).thenReturn(lastName);
		Mockito.when(req.getParameter(REQ_PARAM_PHONE_NUMBER)).thenReturn(phoneNumber);
		Mockito.when(req.getParameter(REQ_PARAM_PASSPORT_NUMBER)).thenReturn(passportNumber);
		Mockito.when(req.getParameter(REQ_PARAM_ROLE)).thenReturn(role);
	}
	
	@Test
	public void registerTest() throws DaoException, ServiceException {
		String email = "reader@gmail.com";
		String firstName = "Reader";
		String lastName = "Readerenko";
		String phoneNumber = "333333333333";
		String passportNumber = "AA111111";
		
		User expectedUser = new User();
		expectedUser.setEmail(email);
		expectedUser.setFirstName(firstName);
		expectedUser.setLastName(lastName);
		expectedUser.setPhoneNumber(phoneNumber);
		expectedUser.setPassportNumber(passportNumber);

		UserDao mockUserDao = Mockito.mock(UserDao.class);
		Mockito.when(mockUserDao.create(Mockito.any(User.class))).then(AdditionalAnswers.returnsFirstArg());
		UserService userService = new UserService(mockUserDao);
		
		User actualUser = userService.register(req);
		assertEquals(expectedUser, actualUser);
	}
	
	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"!reader@gmail.com",
						"reader#gmail.com",
						"reader@gmail,com",
						"a"},
			   nullValues = {"NULL"})
	public void wrongEmailTest(String email) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_EMAIL)).thenReturn(email);
		
		UserDao mockUserDao = Mockito.mock(UserDao.class);
		Mockito.when(mockUserDao.create(Mockito.any(User.class))).then(AdditionalAnswers.returnsFirstArg());
		UserService userService = new UserService(mockUserDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  userService.register(req));
		assertEquals(ERROR_WRONG_EMAIL, ex.getMessage());
	}

	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''"},
			   nullValues = {"NULL"})
	public void wrongPasswordTest(String password) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_PASSWORD)).thenReturn(password);
		
		UserDao mockUserDao = Mockito.mock(UserDao.class);
		Mockito.when(mockUserDao.create(Mockito.any(User.class))).then(AdditionalAnswers.returnsFirstArg());
		UserService userService = new UserService(mockUserDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  userService.register(req));
		assertEquals(ERROR_WRONG_PASSWORD, ex.getMessage());
	}
	
	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"admin",
						"?"},
			   nullValues = {"NULL"})
	public void wrongUserRoleTest(String role) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_ROLE)).thenReturn(role);
		
		UserDao mockUserDao = Mockito.mock(UserDao.class);
		Mockito.when(mockUserDao.create(Mockito.any(User.class))).then(AdditionalAnswers.returnsFirstArg());
		UserService userService = new UserService(mockUserDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  userService.register(req));
		assertEquals(ERROR_WRONG_USER_ROLE, ex.getMessage());
	}

	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"?"},
			   nullValues = {"NULL"})
	public void wrongFirstNameTest(String firstName) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_FIRST_NAME)).thenReturn(firstName);
		
		UserDao mockUserDao = Mockito.mock(UserDao.class);
		Mockito.when(mockUserDao.create(Mockito.any(User.class))).then(AdditionalAnswers.returnsFirstArg());
		UserService userService = new UserService(mockUserDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  userService.register(req));
		assertEquals(ERROR_WRONG_USER_DATA, ex.getMessage());
	}

	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"a a",
						"?"},
			   nullValues = {"NULL"})
	public void wrongLastNameTest(String lastName) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_LAST_NAME)).thenReturn(lastName);
		
		UserDao mockUserDao = Mockito.mock(UserDao.class);
		Mockito.when(mockUserDao.create(Mockito.any(User.class))).then(AdditionalAnswers.returnsFirstArg());
		UserService userService = new UserService(mockUserDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  userService.register(req));
		assertEquals(ERROR_WRONG_USER_DATA, ex.getMessage());
	}

	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"a",
						"3",
						"33333333333",
						"3333333333333"},
			   nullValues = {"NULL"})
	public void wrongPhoneNumberTest(String phoneNumber) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_PHONE_NUMBER)).thenReturn(phoneNumber);
		
		UserDao mockUserDao = Mockito.mock(UserDao.class);
		Mockito.when(mockUserDao.create(Mockito.any(User.class))).then(AdditionalAnswers.returnsFirstArg());
		UserService userService = new UserService(mockUserDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  userService.register(req));
		assertEquals(ERROR_WRONG_USER_DATA, ex.getMessage());
	}

	@ParameterizedTest
	@CsvSource(value = {"NULL", 
						"''",
						"a a",
						"?"},
			   nullValues = {"NULL"})
	public void wrongPassportNumberTest(String passportNumber) throws DaoException, ServiceException {
		Mockito.when(req.getParameter(REQ_PARAM_PASSPORT_NUMBER)).thenReturn(passportNumber);
		
		UserDao mockUserDao = Mockito.mock(UserDao.class);
		Mockito.when(mockUserDao.create(Mockito.any(User.class))).then(AdditionalAnswers.returnsFirstArg());
		UserService userService = new UserService(mockUserDao);
		
		ServiceException ex = assertThrows(ServiceException.class, () ->  userService.register(req));
		assertEquals(ERROR_WRONG_USER_DATA, ex.getMessage());
	}
/*	@Mock
	DaoFactory mockDaoFactory;
	
	@BeforeEach
	public void setMock() {
	    try {
	    	mockDaoFactory = Mockito.mock(MysqlDaoFactory.class);
	    	Field daoFactory = Config.class.getDeclaredField("DAO_FACTORY");
	    	daoFactory.setAccessible(true);
	    	Field modifiersField = Field.class.getDeclaredField("modifiers");
	    	modifiersField.setAccessible(true);
	    	modifiersField.setInt(daoFactory, daoFactory.getModifiers() & ~Modifier.FINAL);
	    	daoFactory.set(null, mockDaoFactory);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	
	@AfterEach
	public void resetConfig() throws Exception {
		Field daoFactory = Config.class.getDeclaredField("DAO_FACTORY");
		daoFactory.setAccessible(true);
    	Field modifiersField = Field.class.getDeclaredField("modifiers");
    	modifiersField.setAccessible(true);
    	modifiersField.setInt(daoFactory, daoFactory.getModifiers() | Modifier.FINAL);
		daoFactory.set(null, null);
	} 
	
	@Test
	public void loginTest() throws DaoException, ServiceException {
		String email = "admin@library.com";
		String password = "nimda";
		HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
		Mockito.when(req.getParameter(REQ_PARAM_EMAIL)).thenReturn(email);
		Mockito.when(req.getParameter(REQ_PARAM_PASSWORD)).thenReturn(password);
		
		User user = new User();
		user.setId(1);
		UserDao mockUserDao = Mockito.mock(UserDao.class);
		Mockito.when(mockUserDao.findByLoginAndPassword(email, DigestUtils.sha1Hex(password))).thenReturn(user);
		Mockito.when(mockDaoFactory.getUserDao()).thenReturn(mockUserDao);
		
		assertEquals(1, UserService.login(req).getId());
	}*/
}
