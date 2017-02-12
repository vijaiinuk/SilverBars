package com.silverbars.vo;

public class OrderInputBuilder {
    private String userId;
    private double quantity;
    private double pricePerKg;
    private String orderType;

    public OrderInputBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public OrderInputBuilder quantity(double quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderInputBuilder pricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
        return this;
    }

    public OrderInputBuilder orderType(String orderType) {
        this.orderType = orderType;
        return this;
    }

    public OrderInput createOrderInput() {
        return new OrderInput(userId, quantity, pricePerKg, orderType);
    }
}