package com.exampleepaam.restaurant.servlet.order;

import com.exampleepaam.restaurant.constant.PathConstants;
import com.exampleepaam.restaurant.model.dto.OrderCreationDto;
import com.exampleepaam.restaurant.model.entity.Dish;
import com.exampleepaam.restaurant.service.DishService;
import com.exampleepaam.restaurant.service.ServiceFactory;
import com.exampleepaam.restaurant.util.RequestUtils;
import com.exampleepaam.restaurant.mapper.OrderMapper;
import com.exampleepaam.restaurant.model.entity.Order;
import com.exampleepaam.restaurant.model.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.exampleepaam.restaurant.service.OrderService;
import com.exampleepaam.restaurant.service.UserService;
import com.exampleepaam.restaurant.util.ControllerUtil;
import com.exampleepaam.restaurant.validator.OrderValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import static com.exampleepaam.restaurant.constant.ErrorAttributeConstants.ORDER_INSUFFICIENT_FUNDS;
import static com.exampleepaam.restaurant.constant.RequestParamConstants.*;
import static com.exampleepaam.restaurant.constant.ViewPathConstants.*;
import static com.exampleepaam.restaurant.util.ControllerUtil.passErrorMapToView;

/**
 * Order servlet
 * Handles saving of a new order
 */
@WebServlet(PathConstants.USER_ORDER_POST_PATH)
public class OrderServlet extends HttpServlet {
    static final Logger logger = LoggerFactory.getLogger(OrderServlet.class);

    private static final String ERROR_ATTRIBUTE_ORDER = "errorOrder";

    UserService userService;
    OrderService orderService;
    DishService dishService;

    @Override
    public void init() {
        userService = ServiceFactory.getInstance().getUserService();
        orderService = ServiceFactory.getInstance().getOrderService();
        dishService = ServiceFactory.getInstance().getDishService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(PathConstants.USER_MENU_PATH);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Retrieve map of dishId - quantity from request
        Map<Long, Integer> dishIdQuantityMap = ControllerUtil.getOrderMap(req);

        User userInSession = RequestUtils.getSessionAttribute(req, USER_PARAM, User.class);
        if (userInSession == null) {
            resp.sendRedirect(PathConstants.ERROR_HANDLER_PATH);
            return;
        }

        String address = req.getParameter(ORDER_ADDRESS_PARAM);
        OrderCreationDto orderCreationDto = new OrderCreationDto(address, dishIdQuantityMap);

        Map<String, String> viewAttributes = OrderValidator.validateOrderDto(orderCreationDto);


        if (!viewAttributes.isEmpty()) {
            passErrorMapToView(MENU_VIEW, req, resp, viewAttributes);
            return;
        }

        User updatedUser = userService.findById(userInSession.getId());
        if(updatedUser == null) {
            resp.sendRedirect(PathConstants.ERROR_HANDLER_PATH);
            logger.warn("User in a session {} is absent in DB", userInSession);
            return;
        }

        Map<Dish, Integer> dishQuantityMap = dishService.fetchDishesToMap(dishIdQuantityMap);
        Order order = OrderMapper.toOrder(orderCreationDto, updatedUser, dishQuantityMap);

        // Check if user has enough money
        BigDecimal userBalance = updatedUser.getBalanceUAH();
        BigDecimal orderCost = order.getTotalPrice();
        if (userBalance.compareTo(orderCost) < 0) {
            viewAttributes.put(ERROR_ATTRIBUTE_ORDER, ORDER_INSUFFICIENT_FUNDS);
            passErrorMapToView(MENU_VIEW, req, resp, viewAttributes);
            return;
        }

        orderService.saveOrder(order);
        logger.info("Order {} is saved", order);
        resp.sendRedirect(PathConstants.USER_ORDER_HISTORY_PATH);

    }
}

