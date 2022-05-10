package com.exampleepaam.restaurant.dao.impl;

import com.exampleepaam.restaurant.dao.UserDao;
import com.exampleepaam.restaurant.dao.mapper.UserDaoMapper;
import com.exampleepaam.restaurant.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User DAO
 */
public class JDBCUserDao implements UserDao {
    static final Logger logger = LoggerFactory.getLogger(JDBCUserDao.class);


    private final Connection connection;
    private final UserDaoMapper userDaoMapper;


    public JDBCUserDao(Connection connection, UserDaoMapper userDaoMapper) {
        this.connection = connection;
        this.userDaoMapper = userDaoMapper;
    }

    /**
     * Saves a user
     *
     * @param user User to be saved
     * @return persisted id
     */
    @Override
    public long save(User user) {
        String query = "INSERT INTO users (balanceuah, email, enabled, name, password, role) " +
                "VALUES(?, ?, ?, ?, ?, ?) " +
                "RETURNING users.id";
        long persistedId = 0;
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            int k = 0;
            ps.setBigDecimal(++k, BigDecimal.ZERO);
            ps.setString(++k, user.getEmail());
            ps.setBoolean(++k, true);
            ps.setString(++k, user.getName());
            ps.setString(++k, user.getPassword());
            ps.setString(++k, user.getRole().toString());
            ps.execute();
            try (ResultSet result = ps.getResultSet()) {
                if (result.next()) {
                    persistedId = result.getLong(1);
                }
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return persistedId;
    }

    /**
     * Updates a user
     *
     * @param user User to be updated
     */
    public void update(User user) {
        String sql = "UPDATE users SET balanceuah=?, email=?, enabled=?, name=?, password=?, role=?" +
                "WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int k = 0;
            ps.setBigDecimal(++k, user.getBalanceUAH());
            ps.setString(++k, user.getEmail());
            ps.setBoolean(++k, user.isEnabled());
            ps.setString(++k, user.getName());
            ps.setString(++k, user.getPassword());
            ps.setString(++k, user.getRole().toString());
            ps.setLong(++k, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Deleting a user is not supported
     *
     * @param id User id to be deleted
     * @throws UnsupportedOperationException Throws always
     */
    @Override
    public void delete(long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Closes a DB connection
     */
    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Finds a user by id
     *
     * @param id User's id
     * @return User or null if no user were found by the given id
     */
    @Override
    public User findById(long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    user = userDaoMapper.extractFromResultSet(resultSet);
                }

            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return user;
    }

    /**
     * Fetches user's balance by id
     *
     * @param id User's id
     * @return Fetched BigDecimal balance or zero if nothing is fetched by the given id
     */
    @Override
    public BigDecimal getUserBalanceById(long id) {
        String sql = "SELECT balanceuah FROM users WHERE id=?";
        BigDecimal result = BigDecimal.ZERO;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getBigDecimal("balanceuah");
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return result;
    }

    /**
     * Fetches user by email
     *
     * @param email User's email
     * @return User or null if no user is found by the given email
     */
    @Override
    public User findByEmail(String email) {

        String query = "SELECT * FROM users WHERE email=?";
        User user = null;
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    user = userDaoMapper.extractFromResultSet(resultSet);
                }

            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return user;

    }
}
