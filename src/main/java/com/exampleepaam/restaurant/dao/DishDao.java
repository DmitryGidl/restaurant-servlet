package com.exampleepaam.restaurant.dao;

import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.model.entity.paging.Paged;

import java.util.List;

/**
 * Interface for Dish DAO
 */
public interface DishDao extends GenericDao<Dish> {
    List<Dish> findAllDishesSorted(String sortField, String sortDirection);

    List<Dish> findAllDishesByCategorySorted(String sortField, String sortDir, String filterCategory);

    Paged<Dish> findPaginatedByCategory(int currentPage, int pageSize,
                                        String sortField, String sortDir, String filterCategory);

    Paged<Dish> findPaginated(int currentPage, int pageSize,
                              String sortField, String sortDir);

}
