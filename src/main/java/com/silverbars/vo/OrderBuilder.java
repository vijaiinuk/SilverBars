package com.silverbars.vo;

public class OrderBuilder {
    private OrderInput input;
    private long orderId;
    private OrderStatus orderStatus;

    public OrderBuilder setOrderId(long orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderBuilder setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderBuilder setInput(OrderInput input) {
        this.input = input;
        return this;
    }

    public Order createOrder() {
        return new Order(input, orderId, orderStatus);
    }
}