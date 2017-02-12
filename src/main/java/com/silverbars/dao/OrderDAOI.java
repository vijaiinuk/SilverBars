package com.silverbars.dao;

import com.silverbars.vo.Order;
import com.silverbars.vo.OrderInput;

import java.util.Collection;

/**
 * Created by Vijay on 2/11/2017.
 */
public interface OrderDAOI {
    Long createOrder(OrderInput orderInput);

    boolean deleteOrder(long orderId);

    Collection<Order> fetchAllOrders();

    void removeAllOrders();

    Collection<Order> fetchAllOrders(String orderType);

    Order fetchOrder(long orderId);
}
