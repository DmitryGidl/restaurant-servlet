package com.exampleepaam.restaurant.testdata;

import com.exampleepaam.restaurant.model.dto.*;
import com.exampleepaam.restaurant.model.entity.*;
import com.exampleepaam.restaurant.model.entity.paging.Paged;
import com.exampleepaam.restaurant.model.entity.paging.Paging;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class TestData {
    public static final String UPLOAD_DIR = "/testDir";
    public static UserCreationDto getUserCreation1() {
        return new UserCreationDto("David", "David@gmail.com",
                "Dw2DaswdWW", "Dw2DaswdWW");
    }

    public static User getUser1() {
        return new User(1, "David", "David@gmail.com",
                "Dw2DaswdWW", BigDecimal.valueOf(500), Role.USER, new ArrayList<>(), true);
    }
    public static User getAdminUser1() {
        return new User(1, "DavidAdmin", "DavidAdmin@gmail.com",
                "Dw2ddXwdWW", BigDecimal.valueOf(530), Role.ADMIN, new ArrayList<>(), true);
    }

    // Dish
    public static Dish getDish1() {
        return new Dish(1, "TestName1", "TestDescription1", Category.BURGERS,
                BigDecimal.valueOf(23), "/somePath1");
    }

    public static Dish getDish2() {
        return new Dish(2, "TestName2", "TestDescription2", Category.DRINKS,
                BigDecimal.valueOf(25), "/somePath2");
    }

    public static List<Dish> getDishList() {
        return Arrays.asList(getDish1(), getDish2());
    }

    // DishResponseDto

    public static DishResponseDto getDishResponseDto1() {
        return new DishResponseDto(1, "TestName1", "TestDescription1", CategoryDto.BURGERS,
                BigDecimal.valueOf(23), "/somePath1");
    }

    public static DishResponseDto getDishResponseDto2() {
        return new DishResponseDto(2, "TestName2", "TestDescription2", CategoryDto.DRINKS,
                BigDecimal.valueOf(25), "/somePath2");
    }

    public static List<DishResponseDto> getDishResponseList() {
        return Arrays.asList(getDishResponseDto1(), getDishResponseDto2());
    }

    // Paged dish
    public static Paged<Dish> getDishPaged() {
        return new Paged<>(getDishList(), Paging.of(3, 1, 10));
    }

    // Paged responseDto
    public static Paged<DishResponseDto> getDishDtoPaged() {
        return new Paged<>(getDishResponseList(), Paging.of(3, 1, 10));
    }

    // DishQuantityMap
    public static Map<Long, Integer> getDishIdQuantityMap() {
        Map<Long, Integer> dishQuantityMap = new HashMap<>();
        dishQuantityMap.put(1L, 2);
        dishQuantityMap.put(2L, 4);
        return dishQuantityMap;
    }

    // OrderResponse
    public static OrderResponseDto getOrderResponse1() {
        OrderedItemResponseDto orderedItemResponseDto1 =
                new OrderedItemResponseDto("DishName1", 1);
        OrderedItemResponseDto orderedItemResponseDto2 =
                new OrderedItemResponseDto("DishName2", 4);

        return new OrderResponseDto(1, OrderResponseDto.StatusDto.COOKING,
                "testAddress", LocalDateTime.of(2022, Month.FEBRUARY, 20, 6, 30),
                LocalDateTime.of(2022, Month.FEBRUARY, 20, 6, 30), BigDecimal.valueOf(231), Arrays.asList(
                orderedItemResponseDto1, orderedItemResponseDto2));
    }

    public static OrderResponseDto getOrderResponse2() {
        OrderedItemResponseDto orderedItemResponseDto1 =
                new OrderedItemResponseDto("DishName3", 2);
        OrderedItemResponseDto orderedItemResponseDto2 =
                new OrderedItemResponseDto("DishName4", 5);

        return new OrderResponseDto(2, OrderResponseDto.StatusDto.DELIVERING,
                "testAddress", LocalDateTime.of(2022, Month.FEBRUARY, 20, 6, 30),
                LocalDateTime.of(2022, Month.FEBRUARY, 20, 6, 30), BigDecimal.valueOf(50), Arrays.asList(
                orderedItemResponseDto1, orderedItemResponseDto2));
    }

    public static Paged<OrderResponseDto> getOrderResponsePaged() {
        return new Paged<>(Arrays.asList(getOrderResponse1(), getOrderResponse2()),
                Paging.of(3, 2, 10));
    }


    // Order
    public static Order getOrder1() {

        OrderItem orderItem1 =
                new OrderItem("DishName1", 1);
        OrderItem OrderItem2 =
                new OrderItem("DishName2", 4);
        User user = getUser1();
        Order order1 = new Order(1, Status.COOKING, BigDecimal.valueOf(231),
                "testAddress", user);
        order1.addOrderItem(orderItem1);
        order1.addOrderItem(OrderItem2);
        return order1;
    }

    public static Order getOrder2() {

        OrderItem orderItem1 =
                new OrderItem("DishName3", 2);
        OrderItem OrderItem2 =
                new OrderItem("DishName4", 5);
        User user = getUser1();
        Order order1 = new Order(2, Status.DELIVERING, BigDecimal.valueOf(50),
                "testAddress", user);
        order1.addOrderItem(orderItem1);
        order1.addOrderItem(OrderItem2);
        return order1;
    }
    // OrderList
    public static List<Order> getOrderList() {
        return Arrays.asList(getOrder1(), getOrder2());
    }

    // OrderCreationDto
    public static OrderCreationDto getOrderCreation1() {
        Map<Long, Integer> orders = new HashMap<>();
        orders.put(1L, 3);
        orders.put(2L, 1);
        return new OrderCreationDto("testAddress", orders);
    }
    // DishQuantityMap
    public static Map<Dish, Integer> getDishIntegerMap() {
        Map<Dish,Integer> dishIntegerMap = new HashMap<>();
        dishIntegerMap.put(getDish1(), 3);
        dishIntegerMap.put(getDish2(), 1);
        return dishIntegerMap;
    }

    public static Paged<Order> getOrderPaged() {
        return new Paged<>(Arrays.asList(getOrder1(), getOrder2()), Paging.of(3, 2, 10));
    }



}
