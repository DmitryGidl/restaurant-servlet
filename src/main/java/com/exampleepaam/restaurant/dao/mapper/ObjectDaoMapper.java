package com.exampleepaam.restaurant.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface for DAO mappers
 */
public interface ObjectDaoMapper<T> {

    T extractFromResultSet(ResultSet rs) throws SQLException;

}
