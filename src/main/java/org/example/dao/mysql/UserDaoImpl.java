package org.example.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;

/**
 * The {@code UserDaoImpl} class is used to realize operations with users' table in MySQL DBMS
 * 
 * @author Vyacheslav Fedchenko
 */

public class UserDaoImpl implements UserDao {
	private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);
	private static final String QUERY_INSERT_USER = "insert.user";
	private static final String QUERY_SELECT_USER_BY_EMAIL_AND_PASSWORD = "select.user.by.email.password";
	private static final String QUERY_UPDATE_USER = "update.user";
	private static final String QUERY_SELECT_USER_BY_ID = "select.user.by.id";
	private static final String QUERY_SELECT_USER_BY_PHONE = "select.reader.by.phone";
	private static final String QUERY_SELECT_USERS_BY_ROLE = "select.users.by.role";
	private static final String QUERY_DELETE_USER = "delete.user";
	private static final String QUERY_SELECT_FINED_USERS = "select.fined.users";
	private static final String QUERY_BLOCK_USER = "block.user";
	private static final int FINE_COLUMN_INDEX = 10;
	private static final String ERROR_USER_ALREADY_EXISTS = "error.user.already.exists";
	private static final String ERROR_CANNOT_CREATE_USER = "error.cannot.create.user";
	private static final String ERROR_CANNOT_GET_DATA = "error.cannot.get.data";
	private static final String ERROR_CANNOT_UPDATE_USER = "error.cannot.update.user";
	private static final String ERROR_CANNOT_DELETE_USER = "error.cannot.delete.user";
	private static final String ERROR_CANNOT_GET_USERS = "error.cannot.get.users";
	private static final String ERROR_CANNOT_BLOCK_USER = "error.cannot.block.user";

	@Override
	public User create(User user) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_INSERT_USER), 
										 PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(++k, user.getEmail());
			pstmt.setString(++k, user.getPassword());
			pstmt.setString(++k, user.getFirstName());
			pstmt.setString(++k, user.getLastName());
			pstmt.setString(++k, user.getPhoneNumber());
			pstmt.setString(++k, user.getPassportNumber());
			pstmt.setString(++k, user.getRole().toString().toLowerCase());
			k = pstmt.executeUpdate();
			if (k > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					user.setId(rs.getInt(1));
				}
			}
		} catch (SQLIntegrityConstraintViolationException ex){
			LOG.error(ex);
			throw new DaoException(ERROR_USER_ALREADY_EXISTS, ex);
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_CREATE_USER, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return user;
	}

	@Override
	public User find(User user) throws DaoException {
		int userId = user.getId();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_USER_BY_ID)); 
			int k = 0;
			pstmt.setInt(++k, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return getUser(rs);
			}
			return null;
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_DATA, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
	}

	@Override
	public void update(User user) throws DaoException {
		PreparedStatement pstmt = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_UPDATE_USER));
			pstmt.setString(++k, user.getEmail());
			pstmt.setString(++k, user.getPassword());
			pstmt.setString(++k, user.getFirstName());
			pstmt.setString(++k, user.getLastName());
			pstmt.setString(++k, user.getPhoneNumber());
			pstmt.setString(++k, user.getPassportNumber());
			pstmt.setInt(++k, user.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_UPDATE_USER, e);
		} finally {
			DbManager.closeResources(con, pstmt);
		}
	}

	@Override
	public void remove(User user) throws DaoException {
		PreparedStatement pstmt = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_DELETE_USER));
			pstmt.setInt(++k, user.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_DELETE_USER, e);
		} finally {
			DbManager.closeResources(con, pstmt);
		}
	}

	@Override
	public User findByLoginAndPassword(String email, String password) throws DaoException{
		if (email == null || password == null) {
			return null;
		}
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_USER_BY_EMAIL_AND_PASSWORD)); 
			int k = 0;
			pstmt.setString(++k, email);
			pstmt.setString(++k, password);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = getUser(rs);
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_DATA, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return user;
	}

	@Override
	public List<User> findByRole(UserRole userRole) throws DaoException {
		ArrayList<User> users = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_USERS_BY_ROLE)); 
			int k = 0;
			pstmt.setString(++k, userRole.toString().toLowerCase());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				users.add(getUser(rs));
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_USERS, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return users;
	}

	@Override
	public User findByPhone(String phoneNumber) throws DaoException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_SELECT_USER_BY_PHONE)); 
			int k = 0;
			pstmt.setString(++k, phoneNumber);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = getUser(rs);
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_DATA, e);
		} finally {
			DbManager.closeResources(con, pstmt, rs);
		}
		return user;
	}

	@Override
	public List<User> findByFine() throws DaoException {
		ArrayList<User> users = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(Queries.getInstance().getQuery(QUERY_SELECT_FINED_USERS));
			while (rs.next()) {
				users.add(getUser(rs));
			}
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_GET_USERS, e);
		} finally {
			DbManager.closeResources(con, stmt, rs);
		}
		return users;
	}
	
	@Override
	public void block(User user) throws DaoException {
		PreparedStatement pstmt = null;
		Connection con = DbManager.getInstance().getConnection();
		try {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getInstance().getQuery(QUERY_BLOCK_USER));
			pstmt.setInt(++k, user.isBlocked() ? 1 : 0);
			pstmt.setInt(++k, user.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			LOG.error(e);
			throw new DaoException(ERROR_CANNOT_BLOCK_USER, e);
		} finally {
			DbManager.closeResources(con, pstmt);
		}
	}

	/**
	 * Extracts the {@code User} object from the {@code ResultSet}
	 * 
	 * @param rs
	 * 		  {@code ResultSet} that contains {@code User}'s fields
	 * 
	 * @return {@code User} object extracted from the {@code ResultSet}
	 * 
	 * @throws SQLException
	 * 
	 * @see org.example.entity.User
	 */
	private static User getUser(ResultSet rs) throws SQLException {
		int k = 0;
		User user = new User();
		user.setId(rs.getInt(++k));
		if (user.getId() == 0) {
			return null;
		}
		user.setEmail(rs.getString(++k));
		user.setPassword(rs.getString(++k));
		user.setFirstName(rs.getString(++k));
		user.setLastName(rs.getString(++k));
		user.setPhoneNumber(rs.getString(++k));
		user.setPassportNumber(rs.getString(++k));
		user.setRole(UserRole.valueOf(rs.getString(++k).toUpperCase()));
		user.setBlocked(intToBoolean(rs.getInt(++k)));
		if (rs.getMetaData().getColumnCount() == FINE_COLUMN_INDEX) {
			user.setFine(rs.getInt(++k));
		}
		return user;
	}

	/**
	 * Converts int number to boolean
	 * @param num
	 * 		  any int value
	 * @return {@code false} only if {@code num} equals zero
	 */
	private static boolean intToBoolean(int num) {
		return num != 0 ? true : false;
	}
}
