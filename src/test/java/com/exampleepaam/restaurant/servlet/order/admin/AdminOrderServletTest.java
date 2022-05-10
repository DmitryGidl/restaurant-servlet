package com.exampleepaam.restaurant.servlet.order.admin;

import com.exampleepaam.restaurant.model.dto.OrderResponseDto;
import com.exampleepaam.restaurant.model.entity.Order;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import com.exampleepaam.restaurant.service.OrderService;
import com.exampleepaam.restaurant.service.ServiceManager;
import com.exampleepaam.restaurant.testdata.TestData;
import com.exampleepaam.restaurant.util.RequestUtils;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

import static com.exampleepaam.restaurant.constant.PathConstants.ADMIN_ORDER_PATH;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.ORDERS_VIEW_PATH;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminOrderServletTest {

    @Spy
    private AdminOrderServlet adminOrderServlet;
    @Mock
    private OrderService orderService;
    @Mock
    private ServiceManager serviceManager;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;

    private static MockedStatic<ServiceManager> serviceManagerDummy;
    private static MockedStatic<RequestUtils> requestUtilsDummy;

    @BeforeEach
    void setUp() {
        serviceManagerDummy = Mockito.mockStatic(ServiceManager.class);
        requestUtilsDummy = Mockito.mockStatic(RequestUtils.class);
        serviceManagerDummy.when(ServiceManager::getInstance).thenReturn(serviceManager);
        when(serviceManager.getOrderService()).thenReturn(orderService);


    }

    private static final String ACTION_PARAMETER = "action";
    private static final String STATUS_ACTION_NEXT = "next";
    private static final String STATUS_ACTION_DECLINE = "decline";

    @AfterEach
    void close() {
        serviceManagerDummy.close();
        requestUtilsDummy.close();
    }

    @Test
    void whenGetThenReturnOrders() throws ServletException, IOException {
        String sortField = "category";
        String sortDir = "asc";
        String filterStatus = "all";
        String pageSize = "10";
        String currentPage = "1";
        String reverseSortDir = "desc";

        when(request.getParameter(SORT_FIELD_PARAM)).thenReturn(sortField);
        when(request.getParameter(SORT_DIR_PARAM)).thenReturn(sortDir);
        when(request.getParameter(FILTER_STATUS_PARAM)).thenReturn(filterStatus);
        when(request.getParameter(PAGE_SIZE_PARAM)).thenReturn(pageSize);
        when(request.getParameter(CURRENT_PAGE_PARAM)).thenReturn(currentPage);
        when(request.getRequestDispatcher(ORDERS_VIEW_PATH)).thenReturn(dispatcher);

        Paged<Order> orderPaged = TestData.getOrderPaged();
        Paged<OrderResponseDto> orderResponsePaged = TestData.getOrderResponsePaged();
        when(orderService.findPaginated(Integer.parseInt(currentPage),
                Integer.parseInt(pageSize), sortField, sortDir, filterStatus)).thenReturn(orderPaged);
        adminOrderServlet.init();
        adminOrderServlet.doGet(request, response);
        verify(request).setAttribute(ORDER_PAGED_ATTRIBUTE, orderResponsePaged);
        verify(request).setAttribute(SORT_FIELD_PARAM, sortField);
        verify(request).setAttribute(REVERSE_SORT_DIR_ATTRIBUTE, reverseSortDir);
        verify(request).setAttribute(SORT_DIR_PARAM, sortDir);
        verify(request).setAttribute(FILTER_STATUS_PARAM, filterStatus);
        verify(request).setAttribute(PAGE_SIZE_PARAM, pageSize);
        verify(request).setAttribute(CURRENT_PAGE_PARAM, currentPage);
        verify(dispatcher).forward(request, response);

    }

    @Test
    void whenPostThenUpdateStatus() throws IOException, URISyntaxException {
        String sortField = "category";
        String sortDir = "asc";
        String filterStatus = "all";
        String pageSize = "10";
        String currentPage = "1";
        String reverseSortDir = "desc";
        String orderId = "1";
        Order order = TestData.getOrder1();
        when(request.getParameter(SORT_FIELD_PARAM)).thenReturn(sortField);
        when(request.getParameter(SORT_DIR_PARAM)).thenReturn(sortDir);
        when(request.getParameter(FILTER_STATUS_PARAM)).thenReturn(filterStatus);
        when(request.getParameter(PAGE_SIZE_PARAM)).thenReturn(pageSize);
        when(request.getParameter(CURRENT_PAGE_PARAM)).thenReturn(currentPage);

        when(request.getParameter(ACTION_PARAMETER)).thenReturn(STATUS_ACTION_NEXT);
        when(request.getParameter(ID_PARAM)).thenReturn(orderId);
        when(orderService.findById(Long.parseLong(orderId))).thenReturn(order);

        adminOrderServlet.init();
        adminOrderServlet.doPost(request, response);

        verify(orderService, times(1)).setStatusNext(order);

        URIBuilder expectedBuilder = new URIBuilder(ADMIN_ORDER_PATH)
                .addParameter(SORT_FIELD_PARAM, sortField)
                .addParameter(SORT_DIR_PARAM, sortDir)
                .addParameter(FILTER_STATUS_PARAM, filterStatus)
                .addParameter(PAGE_SIZE_PARAM, pageSize)
                .addParameter(CURRENT_PAGE_PARAM, currentPage);
        verify(response).sendRedirect(expectedBuilder.toString());
    }
}
