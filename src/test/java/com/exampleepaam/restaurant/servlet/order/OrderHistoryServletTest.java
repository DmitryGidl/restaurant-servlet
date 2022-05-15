package com.exampleepaam.restaurant.servlet.order;

import com.exampleepaam.restaurant.mapper.OrderMapper;
import com.exampleepaam.restaurant.model.dto.OrderResponseDto;
import com.exampleepaam.restaurant.model.dto.OrderedItemResponseDto;
import com.exampleepaam.restaurant.model.entity.Order;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.exampleepaam.restaurant.service.OrderService;
import com.exampleepaam.restaurant.service.ServiceFactory;
import com.exampleepaam.restaurant.testdata.TestData;
import com.exampleepaam.restaurant.util.RequestUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.ORDER_HISTORY_VIEW;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderHistoryServletTest {
    @Spy
    private OrderHistoryServlet orderHistoryServlet;
    @Mock
    private ServiceFactory serviceManager;
    @Mock
    private OrderService orderService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher dispatcher;

    private static MockedStatic<ServiceFactory> serviceManagerDummy;
    private static MockedStatic<RequestUtils> requestUtilsDummy;
    private static MockedStatic<OrderMapper> orderMapperDummy;

    @BeforeEach
    void setUp() {
        serviceManagerDummy = Mockito.mockStatic(ServiceFactory.class);
        requestUtilsDummy = Mockito.mockStatic(RequestUtils.class);
        orderMapperDummy = Mockito.mockStatic(OrderMapper.class);
    }

    @AfterEach
    void close() {
        serviceManagerDummy.close();
        requestUtilsDummy.close();
        orderMapperDummy.close();
    }


    @Test
    void whenGetReturnOrderHistory() throws ServletException, IOException {

        serviceManagerDummy.when(ServiceFactory::getInstance).thenReturn(serviceManager);
        when(serviceManager.getOrderService()).thenReturn(orderService);
        List<Order> orderList = TestData.getOrderList();

        List<OrderResponseDto> orderResponseDtos = orderList.stream().map(order ->
                new OrderResponseDto(order.getId(), OrderResponseDto.StatusDto.valueOf(order.getStatus().toString()),
                        order.getAddress(), order.getCreationDateTime(), order.getUpdateDateTime(), order.getTotalPrice(),
                        order.getUser().getName(), order.getOrderItems().stream()
                                .map(orderItem -> new OrderedItemResponseDto(orderItem.getDishName(),
                                orderItem.getQuantity())).collect(Collectors.toList()))).collect(Collectors.toList());

        orderMapperDummy.when(() -> OrderMapper.toOrderResponseDtoList(orderList)).thenReturn(orderResponseDtos);


        String sortField = "status";
        String sortDir = "asc";
        String filterStatus = "all";
        String pageSize = "10";
        String currentPage = "1";
        String reverseSortDir = "desc";

        User user = TestData.getUser1();
        requestUtilsDummy.when(() -> RequestUtils.getSessionAttribute(request, USER_PARAM, User.class)).thenReturn(user);
        Paged<Order> orderPaged = TestData.getOrderPaged();
        when(request.getParameter(SORT_FIELD_PARAM)).thenReturn(sortField);
        when(request.getParameter(SORT_DIR_PARAM)).thenReturn(sortDir);
        when(request.getParameter(FILTER_STATUS_PARAM)).thenReturn(filterStatus);
        when(request.getParameter(PAGE_SIZE_PARAM)).thenReturn(pageSize);
        when(request.getParameter(CURRENT_PAGE_PARAM)).thenReturn(currentPage);
        when(orderService.findPaginatedByUser(
                Integer.parseInt(currentPage), Integer.parseInt(pageSize),
                sortField, sortDir, filterStatus, user)).thenReturn(orderPaged);
        when(request.getRequestDispatcher(ORDER_HISTORY_VIEW)).thenReturn(dispatcher);
        orderHistoryServlet.init();
        orderHistoryServlet.doGet(request, response);
        Paged<OrderResponseDto> orderResponsePagedExpected = TestData.getOrderResponsePaged();
        verify(request, times(1)).setAttribute(ORDER_PAGED_ATTRIBUTE, orderResponsePagedExpected);


        verify(request, times(1)).setAttribute(SORT_FIELD_PARAM, sortField);
        verify(request, times(1)).setAttribute(REVERSE_SORT_DIR_ATTRIBUTE, reverseSortDir);
        verify(request, times(1)).setAttribute(SORT_DIR_PARAM, sortDir);
        verify(request, times(1)).setAttribute(FILTER_STATUS_PARAM, filterStatus);
        verify(request, times(1)).setAttribute(PAGE_SIZE_PARAM, pageSize);
        verify(request, times(1)).setAttribute(CURRENT_PAGE_PARAM, currentPage);
        verify(dispatcher, times(1)).forward(request, response);

    }
}
