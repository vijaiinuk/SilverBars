package com.silverbars.vo;

/**
 * Created by Vijay on 2/10/2017.
 */
public class Order extends OrderInput {

    private long orderId;
    private OrderStatus orderStatus;

    public Order() {
    }

    public Order(OrderInput input, long orderId, OrderStatus orderStatus) {
        this.setOrderType(input.getOrderType().toString());
        this.setPricePerKg(input.getPricePerKg());
        this.setQuantity(input.getQuantity());
        this.setUserId(input.getUserId());
        this.setOrderStatus(orderStatus);
        this.setOrderId(orderId);
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}

