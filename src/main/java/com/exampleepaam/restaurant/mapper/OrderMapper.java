package com.exampleepaam.restaurant.mapper;

import com.exampleepaam.restaurant.model.dto.OrderCreationDto;
import com.exampleepaam.restaurant.model.dto.OrderResponseDto;
import com.exampleepaam.restaurant.model.dto.OrderedItemResponseDto;
import com.exampleepaam.restaurant.model.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper class for Order and OrderDTOs
 */
public class OrderMapper {


    private OrderMapper() {
    }

    /**
     * Maps orderCreationDto to Order
     *
     * @param orderCreationDto OrderCreationDTO to be mapped
     * @param user             User to whom the order will be linked
     * @param dishQuantityMap  Map of dishes and quantity of times they were ordered
     * @return Order
     */
    public static Order toOrder(OrderCreationDto orderCreationDto, User user, Map<Dish, Integer> dishQuantityMap) {

        BigDecimal totalPrice = getOrderTotalPrice(dishQuantityMap);
        List<OrderItem> orderItems = getOrderItems(dishQuantityMap);
        Order order = new Order(0, Status.PENDING, totalPrice, orderCreationDto.getAddress(), user);
        orderItems.forEach(order::addOrderItem);
        return order;
    }

    /**
     * Helper method to get total price of all ordered dishes
     *
     * @param dishQuantityMap Map of dishes and quantity of times they were ordered
     * @return BigDecimal total price
     */
    private static BigDecimal getOrderTotalPrice(Map<Dish, Integer> dishQuantityMap) {
        return dishQuantityMap.entrySet().stream()
                .map(d -> d.getKey().getPrice().multiply(new BigDecimal(d.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Helper method that maps Dish-Quantity map to a list of OrderItems
     *
     * @param dishQuantityMap Map of Dishes and Quantity of times they were ordered
     * @return List of OrderItems
     */
    private static List<OrderItem> getOrderItems(Map<Dish, Integer> dishQuantityMap) {
        return dishQuantityMap.entrySet().stream()
                .map(entry -> new OrderItem(entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Helper method that maps Order to OrderResponseDTO
     *
     * @param order Order to be mapped
     * @return OrderResponseDto
     */
    public static OrderResponseDto toOrderResponseDto(Order order) {
        List<OrderedItemResponseDto> orderedItemResponseDtos = order.getOrderItems().stream()
                .map(orderItem ->
                        new OrderedItemResponseDto(orderItem.getDishName(), orderItem.getQuantity()))
                .collect(Collectors.toList());
        return new OrderResponseDto(order.getId(), OrderResponseDto.StatusDto.valueOf(order.getStatus().name()), order.getAddress(),
                order.getCreationDateTime(), order.getUpdateDateTime(),
                order.getTotalPrice(), order.getUser().getName(), orderedItemResponseDtos);
    }

    /**
     * Helper method that maps Order list to OrderResponseDTO list
     *
     * @param orderList Order list to be mapped
     * @return OrderResponseDto list
     */
    public static List<OrderResponseDto> toOrderResponseDtoList(List<Order> orderList) {
        return orderList.stream().map(OrderMapper::toOrderResponseDto).collect(Collectors.toList());
    }


}
