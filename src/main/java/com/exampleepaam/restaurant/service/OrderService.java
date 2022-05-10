package com.exampleepaam.restaurant.service;

import com.exampleepaam.restaurant.dao.DaoFactory;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import com.exampleepaam.restaurant.dao.OrderDao;
import com.exampleepaam.restaurant.model.entity.Order;
import com.exampleepaam.restaurant.model.entity.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.exampleepaam.restaurant.exception.ExceptionManager.getUnauthorizedActionException;

/**
 * Service for Order entity
 */
public class OrderService {

    // List with all Statuses that are considered 'active'
    private static final List<Status> ACTIVE_STATUS_LIST = Arrays.asList(Status.PENDING,
            Status.COOKING, Status.DELIVERING);

    /**
     * Retusn a Paged object with a sorted list of orders
     *
     * @param currentPage  current page
     * @param pageSize     number of rows per page
     * @param sortField    sort column for rows
     * @param sortDir      sort direction for rows
     * @param filterStatus filter status, 'all' value makes it ignored,
     *                     'active' returns orders where status is in active list
     * @return a Paged with a list of orders or a paged with an empty list if no orders were found
     */
    public Paged<Order> findPaginated(int currentPage, int pageSize,
                                      String sortField, String sortDir, String filterStatus) {
        try (OrderDao orderDao = DaoFactory.getInstance().createOrderDao()) {
            Paged<Order> orders;

            if (filterStatus.equals("all")) {
                orders = orderDao.findPaginated(currentPage, pageSize, sortField, sortDir);
            } else if (filterStatus.equals("active")) {
                orders = orderDao.findPaginatedWhereStatusIn(currentPage, pageSize, sortField, sortDir, ACTIVE_STATUS_LIST);
            } else {
                orders = orderDao.findPaginatedFilterByStatus(currentPage, pageSize, sortField, sortDir, filterStatus);
            }
            return orders;
        }
    }

    /**
     * Saves order and charge it's total cost from user's balance
     *
     * @param order an Order to be saved
     */
    public void saveOrder(Order order) {
        try (OrderDao orderDao = DaoFactory.getInstance().createOrderDao()) {
            orderDao.saveOrderAndCharge(order);
        }
    }

    /**
     * Returns an Order fetched by id
     *
     * @param id Order id
     * @return an Order or null if the order is not found by id
     */
    public Order findById(long id) {
        try (OrderDao orderDao = DaoFactory.getInstance().createOrderDao()) {
            return orderDao.findById(id);
        }
    }


    /**
     * Sets order status to 'DECLINED' and refunds orders' total cost to user balance
     *
     * @param order Order status of which to be changed
     */
    public void setStatusDeclinedAndRefund(Order order) {
        try (OrderDao orderDao = DaoFactory.getInstance().createOrderDao()) {
            User user = order.getUser();
            BigDecimal orderCost = order.getTotalPrice();
            Status orderStatus = order.getStatus();
            if (!orderStatus.equals(Status.DECLINED)) {
                BigDecimal balanceWithRefund = user.getBalanceUAH().add(orderCost);
                order.setUpdateDateTime(LocalDateTime.now());
                order.setStatus(Status.DECLINED);
                orderDao.updateStatusAndBalance(order, user, balanceWithRefund);
            }
        }
    }

    /**
     * Sets order status to next in status ordinal
     *
     * @param order Order status of which to be changed
     */
    public void setStatusNext(Order order) {
        try (OrderDao orderDao = DaoFactory.getInstance().createOrderDao()) {

            Status status = order.getStatus();
            if (status.equals(Status.DECLINED) || status.equals(Status.COMPLETED)) {
                throw getUnauthorizedActionException("Completed and Declined statuses cannot be changed");
            }
            Status nextStatus = Status.values()[status.ordinal() + 1];
            order.setStatus(nextStatus);
            order.setUpdateDateTime(LocalDateTime.now());
            orderDao.update(order);
        }
    }

    /**
     * Returns a Paged object with a sorted list of orders filtered by status and user
     *
     * @param currentPage  current page
     * @param pageSize     number of rows per page
     * @param sortField    sort column for rows
     * @param sortDir      sort direction for rows
     * @param filterStatus filter status, 'all' value makes it ignored,
     *                     'active' returns orders where status is in active list
     * @param user         current user for filtering
     * @return a Paged with a sorted list of Orders or a paged with empty list if no orders were found
     */
    public Paged<Order> findPaginatedByUser(int currentPage, int pageSize,
                                            String sortField, String sortDir, String filterStatus, User user) {
        try (OrderDao orderDao = DaoFactory.getInstance().createOrderDao()) {
            Paged<Order> orders;

            if (filterStatus.equals("all")) {
                orders = orderDao.findPaginatedByUser(currentPage, pageSize, sortField,
                        sortDir, user);
            } else if (filterStatus.equals("active")) {
                orders = orderDao.findPaginatedByUserWhereStatusIn(currentPage, pageSize,
                        sortField, sortDir, ACTIVE_STATUS_LIST, user);
            } else {
                orders = orderDao.findPaginatedByUserFilterByStatus(currentPage,
                        pageSize, sortField, sortDir, filterStatus, user);
            }
            return orders;
        }
    }

}
