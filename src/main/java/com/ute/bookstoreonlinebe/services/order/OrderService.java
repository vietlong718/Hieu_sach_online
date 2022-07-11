package com.ute.bookstoreonlinebe.services.order;

import com.ute.bookstoreonlinebe.dtos.card.CartDto;
import com.ute.bookstoreonlinebe.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;

import java.security.Principal;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrder();

    Page<Order> getOrderPaging(String search, int page, int size, String sort, String column);

    Order getOrderById(String id);
    List<Order> getOrderByUserId(String id);

    Order createNewOrder(String userID, Principal principal, CartDto dto);

    Order disableOrder(String userID, Principal principal);

    Order callOffOrder(String userID, String orderID, Principal principal);

    Order changeStatusOrder(String id);

    Order changeStatusPayOfOrder(String id);

    Order changeShippingOrder(String id);

    Order changeDeliveredOrder(String id);

    List<Order> getListOrderByUserIdWithIf(String id, int status, Principal principal);

    List<Order> getOrderSuccessByUserId(String id);

    Order getOrderByIdForPayment(String id);

    Order save(Order order);
}
