package com.exampleepaam.restaurant.service;

import com.exampleepaam.restaurant.dao.DaoFactory;
import com.exampleepaam.restaurant.dao.OrderDao;
import com.exampleepaam.restaurant.model.entity.Order;
import com.exampleepaam.restaurant.model.entity.Status;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.exampleepaam.restaurant.testdata.TestData;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class OrderServiceTest {

    @Spy
    OrderService orderService;
    @Mock
    static DaoFactory daoFactory;
    @Mock
    static OrderDao orderDao;

    static MockedStatic<DaoFactory> daoFactoryDummy;

    @BeforeEach
    void setUp() {
        daoFactoryDummy = Mockito.mockStatic(DaoFactory.class);
        daoFactoryDummy.when(DaoFactory::getInstance).thenReturn(daoFactory);
        when(daoFactory.createOrderDao()).thenReturn(orderDao);
    }

    @AfterEach
    void close() {
        daoFactoryDummy.close();
    }

    @Test
    void findPaginated_statusActive_returnPaged() {
        List<Status> activeStatusList = Arrays.asList(Status.PENDING,
                Status.COOKING, Status.DELIVERING);
        Paged<Order> orderPagedExpected = TestData.getOrderPaged();
        int currentPage = 1;
        int pageSize = 24;
        String sortField = "status";
        String sortDir = "asc";
        String filterStatus = "active";
        when(orderDao.findPaginatedWhereStatusIn(currentPage, pageSize, sortField, sortDir, activeStatusList))
                .thenReturn(orderPagedExpected);
        Paged<Order> orderPagedActual = orderService.findPaginated(currentPage, pageSize, sortField, sortDir, filterStatus);
        Assertions.assertEquals(orderPagedExpected, orderPagedActual);
        verify(orderDao).close();
    }

    @Test
    void save_success() {
        Order order = TestData.getOrder1();
        orderService.saveOrder(order);
        verify(orderDao).saveOrderAndCharge(order);
    }

    @Test
    void setStatusDeclinedAndRefund_success() {
        Order order = TestData.getOrder1();
        User user = order.getUser();
        BigDecimal orderCost = order.getTotalPrice();
        BigDecimal balanceWithRefund = user.getBalanceUAH().add(orderCost);


        orderService.setStatusDeclinedAndRefund(order);

        verify(orderDao).updateStatusAndBalance(order, user, balanceWithRefund);
    }

    @Test
    void setStatusNext_success() {
        Order order = TestData.getOrder2();
        Status status = order.getStatus();
        Status nextStatus = Status.values()[status.ordinal() + 1];
        orderService.setStatusNext(order);
        order.setStatus(nextStatus);
        verify(orderDao).update(order);

    }

    @Test
    void findPaginatedByUser_statusActive_success() {
        List<Status> activeStatusList = Arrays.asList(Status.PENDING,
                Status.COOKING, Status.DELIVERING);
        int currentPage = 5;
        int recordsPerPage = 15;
        String sortField = "status";
        String sortDir = "desc";
        String filterStatus = "active";
        Paged<Order> orderPagedExpected = TestData.getOrderPaged();
        User user = TestData.getUser1();
        when(orderDao.findPaginatedByUserWhereStatusIn(currentPage, recordsPerPage,
                sortField, sortDir, activeStatusList, user)).thenReturn(orderPagedExpected);
        Paged<Order> orderPagedActual = orderService.findPaginatedByUser(currentPage, recordsPerPage,
                sortField, sortDir, filterStatus, user);
        Assertions.assertEquals(orderPagedExpected, orderPagedActual);
    }
    @Test
    void findPaginatedByUser_statusAll_success() {
        int currentPage = 5;
        int recordsPerPage = 15;
        String sortField = "status";
        String sortDir = "desc";
        String filterStatus = "all";
        Paged<Order> orderPagedExpected = TestData.getOrderPaged();
        User user = TestData.getUser1();
        when(orderDao.findPaginatedByUser(currentPage, recordsPerPage,
                sortField, sortDir, user)).thenReturn(orderPagedExpected);
        Paged<Order> orderPagedActual = orderService.findPaginatedByUser(currentPage, recordsPerPage,
                sortField, sortDir, filterStatus, user);
        Assertions.assertEquals(orderPagedExpected, orderPagedActual);
    }
    @Test
    void findPaginatedByUser_statusEnum_success() {
        int currentPage = 5;
        int recordsPerPage = 15;
        String sortField = "status";
        String sortDir = "desc";
        String filterStatus = "COOKING";
        Paged<Order> orderPagedExpected = TestData.getOrderPaged();
        User user = TestData.getUser1();
        when(orderDao.findPaginatedByUserFilterByStatus(currentPage, recordsPerPage,
                sortField, sortDir, filterStatus, user)).thenReturn(orderPagedExpected);
        Paged<Order> orderPagedActual = orderService.findPaginatedByUser(currentPage, recordsPerPage,
                sortField, sortDir, filterStatus, user);
        Assertions.assertEquals(orderPagedExpected, orderPagedActual);
    }
}
