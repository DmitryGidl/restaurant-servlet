package com.exampleepaam.restaurant.dao.impl;

import com.exampleepaam.restaurant.dao.OrderDao;
import com.exampleepaam.restaurant.dao.mapper.OrderDaoMapper;
import com.exampleepaam.restaurant.dao.mapper.OrderItemDaoMapper;
import com.exampleepaam.restaurant.dao.mapper.UserDaoMapper;
import com.exampleepaam.restaurant.mapper.PagedMapper;
import com.exampleepaam.restaurant.model.entity.Order;
import com.exampleepaam.restaurant.model.entity.OrderItem;
import com.exampleepaam.restaurant.model.entity.Status;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Order DAO
 */
public class JDBCOrderDao implements OrderDao {
    static final Logger logger = LoggerFactory.getLogger(JDBCOrderDao.class);

    private final Connection connection;
    private final OrderDaoMapper orderDaoMapper;
    private final UserDaoMapper userDaoMapper;
    private final OrderItemDaoMapper orderItemDaoMapper;


    public JDBCOrderDao(Connection connection, OrderDaoMapper orderDaoMapper, UserDaoMapper userDaoMapper,
                        OrderItemDaoMapper orderItemDaoMapper) {
        this.connection = connection;
        this.orderDaoMapper = orderDaoMapper;
        this.userDaoMapper = userDaoMapper;
        this.orderItemDaoMapper = orderItemDaoMapper;
    }

    public static final String DEFAULT_SORT_FIELD = "id";
    public static final String DEFAULT_SORT_DIR = "DESC";

    /**
     * Returns an order fetched by id
     *
     * @param id Order id
     * @return an Order or null if no order is found by the given id
     */
    @Override
    public Order findById(long id) {
        String query = "SELECT * FROM orders LEFT JOIN users ON orders.user_id=users.id WHERE orders.id=?";
        Order order = null;
        User user;
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            int k = 0;
            ps.setLong(++k, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    order = orderDaoMapper.extractFromResultSet(rs);
                    user = userDaoMapper.extractFromResultSet(rs);
                    long userId = rs.getLong(8);
                    user.setId(userId);
                    List<OrderItem> orderItems = fetchOrderItems(order.getId());
                    order.setOrderItems(orderItems);
                    order.setUser(user);
                }
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }

        return order;
    }

    /**
     * Updates an order
     *
     * @param order an Order to be updated
     */
    @Override
    public void update(Order order) {
        String sql = "UPDATE orders SET status=?, total_price=?, creation_date_time=?," +
                "update_date_time=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int k = 0;
            ps.setString(++k, order.getStatus().toString());
            ps.setBigDecimal(++k, order.getTotalPrice());
            ps.setTimestamp(++k, Timestamp.valueOf(order.getCreationDateTime()));
            ps.setTimestamp(++k, Timestamp.valueOf(order.getUpdateDateTime()));
            ps.setLong(++k, order.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Delete operation is not supported
     *
     * @param id Order id
     * @throws UnsupportedOperationException throws always
     */
    @Override
    public void delete(long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fetches a list of OrderItems by order id
     *
     * @param orderId Order id
     * @return a list of OrderItems
     */
    public List<OrderItem> fetchOrderItems(long orderId) {
        List<OrderItem> orderItems = new ArrayList<>();

        String sql = "SELECT * FROM order_item WHERE order_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orderItems.add(orderItemDaoMapper.extractFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return orderItems;
    }

    /**
     * Saves an order and charges a user for the order's total cost
     *
     * @param order an Order to be saved
     */
    public void saveOrderAndCharge(Order order) {
        String sqlCharge = "UPDATE users SET balanceuah=? WHERE id=?";
        try (PreparedStatement psBalanceCharge = connection.prepareStatement(sqlCharge)) {
            connection.setAutoCommit(false);
            save(order);

            BigDecimal orderCost = order.getTotalPrice();
            User user = order.getUser();
            BigDecimal currrentBalance = user.getBalanceUAH();
            long userId = user.getId();
            BigDecimal newBalance = currrentBalance.subtract(orderCost);
            psBalanceCharge.setBigDecimal(1, newBalance);
            psBalanceCharge.setLong(2, userId);
            psBalanceCharge.execute();

            connection.commit();

        } catch (SQLException e) {
            logger.error(e.toString());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error(ex.toString());

            }
            e.printStackTrace();
        }
    }

    /**
     * Returns Paged object with a sorted list of orders
     *
     * @param currentPage current page
     * @param pageSize    number of rows per page
     * @param sortField   sort column for rows
     * @param sortDir     sort direction for rows
     * @return a Paged object with a sorted list of orders or an empty list if nothing is found
     */
    public Paged<Order> findPaginated(int currentPage, int pageSize,
                                      String sortField, String sortDir) {
        List<Order> orders = new ArrayList<>();
        int start = currentPage * pageSize - pageSize;
        long totalRows = 0;
        String queryBuilder = String.format(
                "SELECT *, COUNT(id) OVER() AS total FROM orders ORDER BY %s %s, %s %s LIMIT ? OFFSET ?",
                sortField, sortDir, DEFAULT_SORT_FIELD, DEFAULT_SORT_DIR);
        try (PreparedStatement ps = connection.prepareStatement(queryBuilder)) {
            int k = 0;
            ps.setInt(++k, pageSize);
            ps.setInt(++k, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = orderDaoMapper.extractFromResultSet(rs);
                    totalRows = rs.getLong("total");
                    List<OrderItem> orderItems = fetchOrderItems(order.getId());
                    order.setOrderItems(orderItems);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return PagedMapper.toPaged(orders, currentPage, totalRows, pageSize);
    }

    /**
     * Returns a Paged object with a sorted list of orders filtered by status
     *
     * @param currentPage  Current page
     * @param pageSize     Number of rows per page
     * @param sortField    Sort column for rows
     * @param sortDir      Sort direction for rows
     * @param filterStatus Filter status
     * @return a Paged object with a sorted and filtered by status list of orders or an empty list if nothing is found
     */
    public Paged<Order> findPaginatedFilterByStatus(int currentPage, int pageSize,
                                                    String sortField, String sortDir, String filterStatus) {
        List<Order> orders = new ArrayList<>();
        int start = currentPage * pageSize - pageSize;
        long totalRows = 0;
        String queryBuilder = String.format(
                "SELECT *, COUNT(id) OVER() AS total FROM orders WHERE status=? ORDER BY %s %s, %s %s LIMIT ? OFFSET ?",
                sortField, sortDir, DEFAULT_SORT_FIELD, DEFAULT_SORT_DIR);
        try (PreparedStatement ps = connection.prepareStatement(queryBuilder)) {
            int k = 0;
            ps.setString(++k, filterStatus.toUpperCase());
            ps.setInt(++k, pageSize);
            ps.setInt(++k, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totalRows = rs.getLong("total");
                    Order order = orderDaoMapper.extractFromResultSet(rs);
                    List<OrderItem> orderItems = fetchOrderItems(order.getId());
                    order.setOrderItems(orderItems);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return PagedMapper.toPaged(orders, currentPage, totalRows, pageSize);
    }

    /**
     * Returns a Paged object with a sorted list of orders where order's status is in a list
     *
     * @param currentPage      current page
     * @param pageSize         number of rows per page
     * @param sortField        sort column for rows
     * @param sortDir          sort direction for rows
     * @param filterStatusList statuses list to filter
     * @return a Paged object with a sorted and filtered by a status list of orders or an empty list if nothing is found
     */
    public Paged<Order> findPaginatedWhereStatusIn(int currentPage, int pageSize,
                                                   String sortField, String sortDir,
                                                   List<Status> filterStatusList) {
        List<Order> orders = new ArrayList<>();
        int start = currentPage * pageSize - pageSize;
        long totalRows = 0;
        String queryBuilder = String.format(
                "SELECT *, COUNT(id) OVER() AS total FROM orders WHERE status = ANY (?) ORDER BY %s %s, %s %s LIMIT ? OFFSET ?",
                sortField, sortDir, DEFAULT_SORT_FIELD, DEFAULT_SORT_DIR);
        try (PreparedStatement ps = connection.prepareStatement(queryBuilder)) {
            Array activeArray = connection.createArrayOf("text", filterStatusList.toArray());

            int k = 0;
            ps.setArray(++k, activeArray);
            ps.setInt(++k, pageSize);
            ps.setInt(++k, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totalRows = rs.getLong("total");
                    Order order = orderDaoMapper.extractFromResultSet(rs);
                    List<OrderItem> orderItems = fetchOrderItems(order.getId());
                    order.setOrderItems(orderItems);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return PagedMapper.toPaged(orders, currentPage, totalRows, pageSize);
    }

    /**
     * Saves an order
     *
     * @param order Order to be saved
     * @return persisted id
     */
    @Override
    public long save(Order order) {
        String sql = "INSERT INTO orders (address, creation_date_time, status, total_price, update_date_time, user_id)\n" +
                "VALUES(?, ?, ?, ?, ?, ?)" +
                "RETURNING orders.id";
        long persistedId = 0;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int k = 0;
            ps.setString(++k, order.getAddress());
            ps.setTimestamp(++k, Timestamp.valueOf(order.getCreationDateTime()));
            ps.setString(++k, order.getStatus().toString());
            ps.setBigDecimal(++k, order.getTotalPrice());
            ps.setTimestamp(++k, Timestamp.valueOf(order.getUpdateDateTime()));
            ps.setLong(++k, order.getUser().getId());
            ps.execute();
            ResultSet result = ps.getResultSet();
            result.next();
            persistedId = result.getLong(1);
            result.close();
            order.setId(persistedId);
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem -> orderItem.setOrder(order));

            saveOrderItems(orderItems);
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return persistedId;
    }

    /**
     * Saves an order and updates the user's balance
     *
     * @param order      Order to be saved
     * @param user       User whose balance should be updated
     * @param newBalance new user's balance
     */
    public void updateStatusAndBalance(Order order, User user, BigDecimal newBalance) {
        long userId = user.getId();
        String refundSql = "UPDATE users SET balanceuah=? WHERE id=?";

        try (PreparedStatement psRefund = connection.prepareStatement(refundSql)) {
            connection.setAutoCommit(false);

            update(order);
            int k = 0;
            psRefund.setBigDecimal(++k, newBalance);
            psRefund.setLong(++k, userId);

            psRefund.execute();
            connection.commit();


        } catch (SQLException e) {
            logger.error(e.toString());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error(ex.toString());
            }
        }
    }

    /**
     * Helper method to save OrderItems
     *
     * @param orderItems list of orderItems
     */
    private void saveOrderItems(List<OrderItem> orderItems) {
        String sql = "INSERT INTO order_item (quantity, dish_name, order_id)\n" +
                "VALUES(?, ?, ? );";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (OrderItem orderItem : orderItems) {
                int k = 0;
                ps.setInt(++k, orderItem.getQuantity());
                ps.setString(++k, orderItem.getDishName());
                ps.setLong(++k, orderItem.getOrder().getId());
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Returns a paged object with a list of sorted and filtered by user orders
     *
     * @param currentPage current page
     * @param pageSize    number of rows per page
     * @param sortField   sort column for rows
     * @param sortDir     sort direction for rows
     * @param user        user by which to filter
     * @return Paged object with a sorted list of orders or an empty list if nothing is found
     */
    public Paged<Order> findPaginatedByUser(int currentPage, int pageSize,
                                            String sortField, String sortDir, User user) {
        long userId = user.getId();
        List<Order> orders = new ArrayList<>();
        int start = currentPage * pageSize - pageSize;
        long totalRows = 0;
        String queryBuilder = String.format(
                "SELECT *, COUNT(id) OVER() as total FROM orders WHERE user_id=? ORDER BY %s %s, %s %s LIMIT ? OFFSET ?",
                sortField, sortDir, DEFAULT_SORT_FIELD, DEFAULT_SORT_DIR);
        try (PreparedStatement ps = connection.prepareStatement(queryBuilder)) {
            int k = 0;
            ps.setLong(++k, userId);
            ps.setInt(++k, pageSize);
            ps.setInt(++k, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totalRows = rs.getLong("total");
                    Order order = orderDaoMapper.extractFromResultSet(rs);
                    List<OrderItem> orderItems = fetchOrderItems(order.getId());
                    order.setOrderItems(orderItems);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return PagedMapper.toPaged(orders, currentPage, totalRows, pageSize);
    }

    /**
     * Returns a Paged object with a sorted, filtered by user and status list of orders
     *
     * @param currentPage  current page
     * @param pageSize     number of rows per page
     * @param sortField    sort column for rows
     * @param sortDir      sort direction for rows
     * @param user         user by which to filter
     * @param filterStatus filter status
     * @return a Paged object with a sorted list of orders or an empty list if nothing is found
     */
    public Paged<Order> findPaginatedByUserFilterByStatus(int currentPage, int pageSize,
                                                          String sortField, String sortDir,
                                                          String filterStatus, User user) {
        long userId = user.getId();
        List<Order> orders = new ArrayList<>();
        int start = currentPage * pageSize - pageSize;
        long totalRows = 0;
        String queryBuilder = String.format(
                "SELECT *, COUNT(id) OVER() AS total FROM orders WHERE status=? AND user_id=? " +
                        "ORDER BY %s %s, %s %s LIMIT ? OFFSET ?",
                sortField, sortDir, DEFAULT_SORT_FIELD, DEFAULT_SORT_DIR);
        try (PreparedStatement ps = connection.prepareStatement(queryBuilder)) {
            int k = 0;
            ps.setString(++k, filterStatus.toUpperCase());
            ps.setLong(++k, userId);
            ps.setInt(++k, pageSize);
            ps.setInt(++k, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totalRows = rs.getLong("total");
                    Order order = orderDaoMapper.extractFromResultSet(rs);
                    List<OrderItem> orderItems = fetchOrderItems(order.getId());
                    order.setOrderItems(orderItems);
                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return PagedMapper.toPaged(orders, currentPage, totalRows, pageSize);

    }

    /**
     * Returns Paged with a list of sorted, filtered by user list of orders where order's status is in the list
     *
     * @param currentPage      current page
     * @param pageSize         number of rows per page
     * @param sortField        sort column for rows
     * @param sortDir          sort direction for rows
     * @param user             user by which to filter
     * @param filterStatusList status list to filter
     * @return a Paged object with a sorted list of orders or an empty list if nothing is found
     */
    public Paged<Order> findPaginatedByUserWhereStatusIn(int currentPage, int pageSize,
                                                         String sortField, String sortDir,
                                                         List<Status> filterStatusList, User user) {
        long userId = user.getId();
        List<Order> orders = new ArrayList<>();
        int start = currentPage * pageSize - pageSize;
        long totalRows = 0;
        String queryBuilder = String.format(
                "SELECT *, COUNT(id) OVER() AS total FROM orders " +
                        "WHERE status = ANY (?) AND user_id=? ORDER BY %s %s, %s %s LIMIT ? OFFSET ?",
                sortField, sortDir, DEFAULT_SORT_FIELD, DEFAULT_SORT_DIR);
        try (PreparedStatement ps = connection.prepareStatement(queryBuilder)) {
            Array activeArray = connection.createArrayOf("text", filterStatusList.toArray());

            int k = 0;
            ps.setArray(++k, activeArray);
            ps.setLong(++k, userId);
            ps.setInt(++k, pageSize);
            ps.setInt(++k, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totalRows = rs.getLong("total");
                    Order order = orderDaoMapper.extractFromResultSet(rs);
                    List<OrderItem> orderItems = fetchOrderItems(order.getId());
                    order.setOrderItems(orderItems);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
        return PagedMapper.toPaged(orders, currentPage, totalRows, pageSize);
    }


    /**
     * Closes a connection
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
