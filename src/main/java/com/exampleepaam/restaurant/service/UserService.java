package com.exampleepaam.restaurant.service;

import com.exampleepaam.restaurant.dao.DaoFactory;
import com.exampleepaam.restaurant.dao.UserDao;
import com.exampleepaam.restaurant.model.entity.User;

import java.math.BigDecimal;

import static com.exampleepaam.restaurant.exception.ExceptionManager.getUserAlreadyExistsException;

/**
 * Service for User entity
 */
public class UserService {

    /**
     * Saves a user
     *
     * @param user User to be saved
     * @return persisted id
     */
    public long save(User user) {
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            String email = user.getEmail();
            if (userDao.findByEmail(user.getEmail()) != null) {
                throw getUserAlreadyExistsException(email);
            }
            return userDao.save(user);
        }
    }

    /**
     * Finds a user by id
     *
     * @param id user's id
     * @return a User or null if no user is found
     */
    public User findById(long id) {
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            return userDao.findById(id);
        }
    }

    /**
     * Finds user by email
     *
     * @param email user's email
     * @return a User or null if nothing is found
     */
    public User findByEmail(String email) {
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            return userDao.findByEmail(email);
        }
    }

    /**
     * Updates a user
     *
     * @param user User to be updated
     */
    public void update(User user) {
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            userDao.update(user);
        }
    }

    /**
     * Adds UAH to the user's balance
     *
     * @param user         User to whom the balance will be replenished
     * @param balanceToAdd how much UAH to replenish
     */
    public void addUserBalance(User user, BigDecimal balanceToAdd) {
        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            User fetchedUser = userDao.findById(user.getId());
            BigDecimal oldBalance = fetchedUser.getBalanceUAH();
            BigDecimal newBalance = oldBalance.add(balanceToAdd);
            user.setBalanceUAH(newBalance);
            userDao.update(user);
        }
    }

    /**
     * Gets user balance
     *
     * @param id user whose balance to return
     * @return the User's balance
     */
    public BigDecimal getUserBalance(long id) {

        try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
            return userDao.getUserBalanceById(id);
        }
    }
}
