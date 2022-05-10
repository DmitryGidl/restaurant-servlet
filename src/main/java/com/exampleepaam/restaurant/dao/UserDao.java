package com.exampleepaam.restaurant.dao;

import com.exampleepaam.restaurant.model.entity.User;

import java.math.BigDecimal;

/**
 * Interface for User DAO
 */
public interface UserDao extends GenericDao<User> {

    User findByEmail(String email);

    BigDecimal getUserBalanceById(long id);
}
