package com.silverbars.dao;

import com.silverbars.vo.Order;
import com.silverbars.vo.OrderBuilder;
import com.silverbars.vo.OrderInput;
import com.silverbars.vo.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by Vijay on 2/11/2017.
 */
@Component
public class OrderDAO implements OrderDAOI {

    private ConcurrentHashMap<Long, Order> ordersCache = new ConcurrentHashMap<>();

    private AtomicLong idCounter = new AtomicLong();

    public ConcurrentHashMap<Long, Order> getOrdersCache() {
        return ordersCache;
    }

    public void setOrdersCache(ConcurrentHashMap<Long, Order> ordersCache) {
        this.ordersCache = ordersCache;
    }

    public AtomicLong getIdCounter() {
        return idCounter;
    }

    public void setIdCounter(AtomicLong idCounter) {
        this.idCounter = idCounter;
    }

    @Override
    public Long createOrder(OrderInput orderInput) {
        Order order = new OrderBuilder().setInput(orderInput).createOrder();
        order.setOrderId(idCounter.incrementAndGet());
        order.setOrderStatus(OrderStatus.CREATED);
        ordersCache.putIfAbsent(order.getOrderId(), order);
        return order.getOrderId();
    }

    @Override
    public boolean deleteOrder(long orderId) {
        if(ordersCache.containsKey(orderId))  {
            ordersCache.get(orderId).setOrderStatus(OrderStatus.CANCELLED);
            return true;
        }
        return false;
    }


    @Override
    public Collection<Order> fetchAllOrders() {
        return ordersCache.values();
    }

    @Override
    public void removeAllOrders() {
        ordersCache.clear();
    }

    @Override
    public Collection<Order> fetchAllOrders(String orderType) {
        if(orderType != null)
            return ordersCache.values().stream().filter(o -> o.getOrderType().toString().equalsIgnoreCase(orderType)).collect(toList());

        return ordersCache.values();
    }

    @Override
    public Order fetchOrder(long orderId) {
        return ordersCache.get(new Long(orderId));
    }
}
