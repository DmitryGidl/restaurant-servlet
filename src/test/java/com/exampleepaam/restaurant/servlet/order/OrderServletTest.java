package com.exampleepaam.restaurant.servlet.order;

import com.exampleepaam.restaurant.constant.PathConstants;
import com.exampleepaam.restaurant.mapper.OrderMapper;
import com.exampleepaam.restaurant.model.dto.OrderCreationDto;
import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.model.entity.Order;
import com.exampleepaam.restaurant.model.entity.User;
import com.exampleepaam.restaurant.service.DishService;
import com.exampleepaam.restaurant.service.OrderService;
import com.exampleepaam.restaurant.service.ServiceFactory;
import com.exampleepaam.restaurant.service.UserService;
import com.exampleepaam.restaurant.testdata.TestData;
import com.exampleepaam.restaurant.util.ControllerUtil;
import com.exampleepaam.restaurant.util.RequestUtils;
import com.exampleepaam.restaurant.validator.OrderValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.exampleepaam.restaurant.constant.PathConstants.USER_ORDER_HISTORY_PATH;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.ORDER_ADDRESS_PARAM;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.USER_PARAM;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServletTest {
    @Spy
    OrderServlet orderServlet;
    @Mock
    UserService userService;
    @Mock
    DishService dishService;
    @Mock
    ServiceFactory serviceManager;
    @Mock
    OrderService orderService;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    private static MockedStatic<ServiceFactory> serviceManagerDummy;
    private static MockedStatic<ControllerUtil> controllerUtilDummy;
    private static MockedStatic<RequestUtils> requestUtilsDummy;
    private static MockedStatic<OrderValidator> orderValidatorDummy;
    private static MockedStatic<OrderMapper> orderMapperDummy;

    @BeforeEach
    void setUp() {
        serviceManagerDummy = Mockito.mockStatic(ServiceFactory.class);
        requestUtilsDummy = Mockito.mockStatic(RequestUtils.class);
        controllerUtilDummy = Mockito.mockStatic(ControllerUtil.class);
        orderValidatorDummy = Mockito.mockStatic(OrderValidator.class);
        orderMapperDummy = Mockito.mockStatic(OrderMapper.class);

        serviceManagerDummy.when(ServiceFactory::getInstance).thenReturn(serviceManager);
        when(serviceManager.getUserService()).thenReturn(userService);
        when(serviceManager.getOrderService()).thenReturn(orderService);
        when(serviceManager.getDishService()).thenReturn(dishService);

    }

    @AfterEach
    void close() {
        serviceManagerDummy.close();
        requestUtilsDummy.close();
        controllerUtilDummy.close();
        orderValidatorDummy.close();
        orderMapperDummy.close();
    }

    @Test
    void whenGetThenSendRedirect() throws ServletException, IOException {
        orderServlet.init();
        orderServlet.doGet(request, response);
        verify(response, times(1)).sendRedirect(PathConstants.USER_MENU_PATH);
    }

    @Test
    void whenPostThenSaveOrder() throws IOException, ServletException {

        Map<Long, Integer> dishIdQuantityMap = TestData.getDishIdQuantityMap();
        Map<Dish, Integer> dishQuantityMap = TestData.getDishIntegerMap();
        Order order = TestData.getOrder1();
        String address = "TestAddress";
        User user = TestData.getUser1();

        controllerUtilDummy.when(() -> ControllerUtil.getOrderMap(request)).thenReturn(dishIdQuantityMap);

        OrderCreationDto orderCreationExpected = new OrderCreationDto(address, dishIdQuantityMap);
        requestUtilsDummy.when(() -> RequestUtils.getSessionAttribute(request, USER_PARAM, User.class))
                .thenReturn(user);
        orderValidatorDummy.when(() -> OrderValidator.validateOrderDto(orderCreationExpected)).thenReturn(new HashMap<>());
        when(request.getParameter(ORDER_ADDRESS_PARAM)).thenReturn(address);
        when(userService.findById(user.getId())).thenReturn(user);
        when(dishService.fetchDishesToMap(dishIdQuantityMap)).thenReturn(dishQuantityMap);
        orderMapperDummy.when(() -> OrderMapper.toOrder(orderCreationExpected, user, dishQuantityMap)).thenReturn(order);

        orderServlet.init();
        orderServlet.doPost(request, response);

        verify(orderService, times(1)).saveOrder(order);
        verify(response, times(1)).sendRedirect(USER_ORDER_HISTORY_PATH);
    }
}
