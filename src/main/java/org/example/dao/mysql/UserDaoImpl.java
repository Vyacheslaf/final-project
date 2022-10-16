package org.example.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.entity.UserRole;
import org.example.exception.DaoException;

public class UserDaoImpl implements UserDao {
	private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);
	private static final String QUERY_INSERT_USER = "insert.user";
	private static final String QUERY_SELECT_USER_BY_EMAIL_AND_PASSWORD = "select.user.by.email.password";
	private static final String QUERY_UPDATE_USER = "update.user";
	private static final String QUERY_SELECT_USER_BY_ID = "select.user.by.id";
	private static final String QUERY_SELECT_USER_BY_PHONE = "select.reader.by.phone";
	private static final String QUERY_SELECT_USERS_BY_ROLE = "select.users.by.role";
	private static final String QUERY_DELETE_USER = "delete.user";

	@Override
	public User create(User user) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			con.setAutoCommit(true);
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_INSERT_USER), PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(++k, user.getEmail());
			pstmt.setString(++k, DigestUtils.sha1Hex(user.getPassword()));
			pstmt.setString(++k, user.getFirstName());
			pstmt.setString(++k, user.getLastName());
			pstmt.setString(++k, user.getPhoneNumber());
			pstmt.setString(++k, user.getPassportNumber());
			pstmt.setString(++k, user.getRole().name().toLowerCase());
			k = pstmt.executeUpdate();
			if (k > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					user.setId(rs.getInt(1));
				}
			}
		} catch (SQLIntegrityConstraintViolationException ex){
			String message = "User with such data is already registered";
			LOG.error(ex);
			throw new DaoException(message, ex);
		} catch (SQLException e) {
			String message = "Cannot create user";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return user;
	}

	@Override
	public User find(User user) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_USER_BY_ID)); 
			int k = 0;
			pstmt.setInt(++k, user.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = getUser(rs);
			}
		} catch (SQLException e) {
			String message = "Cannot get data from database";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return user;
	}

	@Override
	public void update(User user) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			con.setAutoCommit(true);
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_UPDATE_USER));
			pstmt.setString(++k, user.getEmail());
			pstmt.setString(++k, user.getPassword());
			pstmt.setString(++k, user.getFirstName());
			pstmt.setString(++k, user.getLastName());
			pstmt.setString(++k, user.getPhoneNumber());
			pstmt.setString(++k, user.getPassportNumber());
//			pstmt.setInt(++k, booleanToInt(user.isBlocked()));
			pstmt.setInt(++k, user.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			String message = "Cannot update user";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
	}
	
/*	private static int booleanToInt(boolean blocked) {
		return blocked ? 1 : 0;
	}*/

	@Override
	public void remove(User user) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_DELETE_USER));
			pstmt.setInt(++k, user.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			String message = "Cannot delete user";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
	}

	@Override
	public User findByLoginAndPassword(String email, String password) 
															throws DaoException{
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_USER_BY_EMAIL_AND_PASSWORD)); 
			int k = 0;
			pstmt.setString(++k, email);
			pstmt.setString(++k, DigestUtils.sha1Hex(password));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = getUser(rs);
			}
		} catch (SQLException e) {
			String message = "Cannot get data from database";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return user;
	}

	private static User getUser(ResultSet rs) throws SQLException {
		int k = 0;
		User user = new User();
		user.setId(rs.getInt(++k));
		user.setEmail(rs.getString(++k));
		user.setPassword(rs.getString(++k));
		user.setFirstName(rs.getString(++k));
		user.setLastName(rs.getString(++k));
		user.setPhoneNumber(rs.getString(++k));
		user.setPassportNumber(rs.getString(++k));
		user.setRole(UserRole.valueOf(rs.getString(++k).toUpperCase()));
		user.setBlocked(intToBoolean(rs.getInt(++k)));
		return user;
	}

	private static boolean intToBoolean(int num) {
		return num != 0 ? true : false;
	}

	@Override
	public List<User> findByRole(UserRole userRole) throws DaoException {
		ArrayList<User> users = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_USERS_BY_ROLE)); 
			int k = 0;
			pstmt.setString(++k, userRole.toString().toLowerCase());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				users.add(getUser(rs));
			}
		} catch (SQLException e) {
			String message = "Cannot get users from database";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return users;
	}

	@Override
	public User findByPhone(String phoneNumber) throws DaoException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_SELECT_USER_BY_PHONE)); 
			int k = 0;
			pstmt.setString(++k, phoneNumber);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = getUser(rs);
			}
		} catch (SQLException e) {
			String message = "Cannot get data from database";
			LOG.error(e);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return user;
	}

}
