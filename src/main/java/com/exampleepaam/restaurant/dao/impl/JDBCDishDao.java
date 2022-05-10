package com.exampleepaam.restaurant.dao.impl;

import com.exampleepaam.restaurant.dao.DishDao;
import com.exampleepaam.restaurant.dao.mapper.DishDaoMapper;
import com.exampleepaam.restaurant.mapper.PagedMapper;
import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Dish DAO
 */
public class JDBCDishDao implements DishDao {
    static final Logger logger = LoggerFactory.getLogger(JDBCDishDao.class);

    private final Connection connection;
    private final DishDaoMapper dishDaoMapper;

    public JDBCDishDao(Connection connection, DishDaoMapper dishDaoMapper) {
        this.connection = connection;
        this.dishDaoMapper = dishDaoMapper;
    }

    public static final String DEFAULT_SORT_FIELD = "id";
    public static final String DEFAULT_SORT_DIR = "DESC";

    /**
     * Returns a sorted list of dishes
     *
     * @param sortField sort column for rows
     * @param sortDir   sort direction for rows
     * @return a sorted list of dishes or null if no dishes were found
     */
    public List<Dish> findAllDishesSorted(String sortField, String sortDir) {

        List<Dish> dishes = new ArrayList<>();
        String queryBuilder = String.format("SELECT * FROM Dish ORDER BY %s %s, %s %s",
                sortField, sortDir, DEFAULT_SORT_FIELD, DEFAULT_SORT_DIR);


        try (PreparedStatement ps = connection.prepareStatement(queryBuilder);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                dishes.add(dishDaoMapper.extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return dishes;
    }

    /**
     * Returns a sorted list of dishes filtered by category
     *
     * @param sortField      sort column for rows
     * @param sortDir        sort direction for rows
     * @param filterCategory filter category, 'all' value makes it ignored
     * @return a sorted list of dishes or null if no dishes were found
     */
    @Override
    public List<Dish> findAllDishesByCategorySorted(String sortField,
                                                    String sortDir, String filterCategory) {

        List<Dish> dishes = new ArrayList<>();
        String queryBuilder = String.format("SELECT * FROM dish WHERE category=? ORDER BY %s %s, %s %s",
                sortField, sortDir, DEFAULT_SORT_FIELD, DEFAULT_SORT_DIR);

        try (PreparedStatement ps = connection.prepareStatement(queryBuilder)) {
            ps.setString(1, filterCategory.toUpperCase(Locale.ENGLISH));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dishes.add(dishDaoMapper.extractFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return dishes;
    }

    /**
     * Returns a Paged object with a list of dishes
     *
     * @param currentPage current page
     * @param pageSize    number of rows per page
     * @param sortField   sort column for rows
     * @param sortDir     sort direction for rows
     * @return a Paged object with  alist of dishes or a paged with an empty list if no dishes were found
     */
    public Paged<Dish> findPaginated(int currentPage, int pageSize,
                                     String sortField, String sortDir) {
        List<Dish> dishes = new ArrayList<>();
        int start = currentPage * pageSize - pageSize;
        long totalRows = 0;
        String queryBuilder = String.format(
                "SELECT *, COUNT(id) OVER() AS total FROM dish ORDER BY %s %s, %s %s LIMIT ? OFFSET ?",
                sortField, sortDir, DEFAULT_SORT_FIELD, DEFAULT_SORT_DIR);
        try (PreparedStatement ps = connection.prepareStatement(queryBuilder)) {

            int k = 0;
            ps.setInt(++k, pageSize);
            ps.setInt(++k, start);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totalRows = rs.getLong("total");
                    dishes.add(dishDaoMapper.extractFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return PagedMapper.toPaged(dishes, currentPage, totalRows, pageSize);
    }

    /**
     * Returns a Paged object with a sorted list of Dishes filtered by category
     *
     * @param currentPage    current page
     * @param pageSize       number of rows per page
     * @param sortField      sort column for rows
     * @param sortDir        sort direction for rows
     * @param filterCategory filter category
     * @return a Paged object with  alist of dishes or a paged with an empty list if no dishes were found
     */
    public Paged<Dish> findPaginatedByCategory(int currentPage, int pageSize,
                                               String sortField, String sortDir, String filterCategory) {

        List<Dish> dishes = new ArrayList<>();
        int start = currentPage * pageSize - pageSize;
        String queryBuilder;
        long totalRows = 0;
        queryBuilder = String.format(
                "SELECT *, COUNT(id) OVER() AS total FROM dish WHERE category='%s' ORDER BY %s %s, %s %s LIMIT ? OFFSET ?",
                filterCategory.toUpperCase(Locale.ROOT), sortField, sortDir, DEFAULT_SORT_FIELD, DEFAULT_SORT_DIR);

        try (PreparedStatement ps = connection.prepareStatement(queryBuilder)) {

            int k = 0;
            ps.setInt(++k, pageSize);
            ps.setInt(++k, start);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totalRows = rs.getLong("total");
                    dishes.add(dishDaoMapper.extractFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }

        return PagedMapper.toPaged(dishes, currentPage, totalRows, pageSize);

    }

    /**
     * Deletes a dish
     *
     * @param id id of the dish to be deleted
     */
    @Override
    public void delete(long id) {
        String sql = "DELETE FROM dish WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Saves a dish
     *
     * @param dish Dish to be saved
     * @return persisted id
     */
    @Override
    public long save(Dish dish) {
        String sql = "INSERT INTO dish ( category, description, image_file_name, name, price) " +
                "VALUES ( ?, ?, ?, ?, ?) " +
                "RETURNING dish.id";
        long persistedId = 0;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int k = 0;
            ps.setString(++k, dish.getCategory().toString());
            ps.setString(++k, dish.getDescription());
            ps.setString(++k, dish.getImageFileName());
            ps.setString(++k, dish.getName());
            ps.setBigDecimal(++k, dish.getPrice());
            ps.execute();
            try (ResultSet result = ps.getResultSet()) {
                result.next();
                persistedId = result.getLong(1);
            }
            return persistedId;
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return persistedId;
    }

    /**
     * Updates a dish
     *
     * @param dish Current page
     */
    public void update(Dish dish) {

        String sql = "UPDATE dish SET category=?, description=?, image_file_name=?, name=?, price=?" +
                "WHERE id =?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int k = 0;
            ps.setString(++k, dish.getCategory().toString());
            ps.setString(++k, dish.getDescription());
            ps.setString(++k, dish.getImageFileName());
            ps.setString(++k, dish.getName());
            ps.setBigDecimal(++k, dish.getPrice());
            ps.setLong(++k, dish.getId());
            ps.execute();
        } catch (SQLException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Returns a dish fetched by id
     *
     * @param id Dish id to be fetched
     * @return a fetched Dish
     */
    @Override
    public Dish findById(long id) {

        Dish dish = null;
        String sql = "SELECT * FROM dish WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setLong(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                resultSet.next();
                dish = dishDaoMapper.extractFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return dish;
    }

    /**
     * Closes a connection to DB
     */
    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error(e.toString());
        }
    }
}





