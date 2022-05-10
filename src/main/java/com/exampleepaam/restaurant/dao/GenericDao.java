package com.exampleepaam.restaurant.dao;

/**
 * Generic interface for DAO classes
 */
public interface GenericDao<T> extends AutoCloseable {
    long save(T entity);

    T findById(long id);

    void update(T entity);

    void delete(long id);

    void close();

}
