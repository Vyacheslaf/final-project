package org.example.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
//	private static final String SQL_SELECT_USER_BY_EMAIL_AND_PASSWORD 
//				= "SELECT id, first_name, last_name, role FROM user WHERE email = ? AND password = ?";
	private static final String QUERY_INSERT_USER = "insert.user";
	private static final String QUERY_SELECT_USER_BY_EMAIL_AND_PASSWORD 
											= "select.user.by.email.password";

	@Override
	public User create(User user) throws DaoException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try(Connection con = DbManager.getInstance().getConnection()) {
			con.setAutoCommit(true);
			int k = 0;
			pstmt = con.prepareStatement(Queries.getQuery(QUERY_INSERT_USER), 
									PreparedStatement.RETURN_GENERATED_KEYS);
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
		} catch (SQLException e) {
			e.printStackTrace();
			String message = "Cannot create user";
			LOG.error(message);
			throw new DaoException(message, e);
		} finally {
			DbManager.close(rs);
			DbManager.close(pstmt);
		}
		return user;
	}

	@Override
	public User find(User entity) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(User entity) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(User entity) throws DaoException {
		// TODO Auto-generated method stub
		
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
//			pstmt = con.prepareStatement(SQL_SELECT_USER_BY_EMAIL_AND_PASSWORD);
			int k = 0;
			pstmt.setString(++k, email);
			pstmt.setString(++k, DigestUtils.sha1Hex(password));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = getUser(rs);
			}
		} catch (SQLException e) {
			String message = "Cannot get data from database";
			LOG.error(message);
			throw new DaoException(message, e);
		} finally {
			close(rs);
			close(pstmt);
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
		user.setBlocked(Boolean.getBoolean(rs.getString(++k)));
		return user;
	}

	@Override
	public List<User> findByRole(UserRole userRole) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void close(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
