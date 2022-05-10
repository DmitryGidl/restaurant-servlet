package com.exampleepaam.restaurant.dao;

import com.exampleepaam.restaurant.model.entity.Order;
import com.exampleepaam.restaurant.model.entity.Status;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.model.entity.paging.Paged;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface for Order DAO
 */
public interface OrderDao extends GenericDao<Order> {
    Paged<Order> findPaginated(int currentPage, int recordsPerPage,
                               String sortField, String sortDir);

    Paged<Order> findPaginatedFilterByStatus(int currentPage, int recordsPerPage,
                                             String sortField, String sortDir, String filterStatus);

    Paged<Order> findPaginatedWhereStatusIn(int currentPage, int recordsPerPage,
                                            String sortField, String sortDir,
                                            List<Status> filterStatusList);

    Paged<Order> findPaginatedByUser(int currentPage, int recordsPerPage,
                                     String sortField, String sortDir, User user);

    Paged<Order> findPaginatedByUserFilterByStatus(int currentPage, int recordsPerPage,
                                                   String sortField, String sortDir, String filterStatus, User user);

    Paged<Order> findPaginatedByUserWhereStatusIn(int currentPage, int recordsPerPage,
                                                  String sortField, String sortDir,
                                                  List<Status> filterStatusList, User user);

    void updateStatusAndBalance(Order order, User user, BigDecimal newBalance);


    void saveOrderAndCharge(Order order);
}
