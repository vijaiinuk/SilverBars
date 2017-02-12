package com.silverbars.vo;

/**
 * Created by Vijay on 2/10/2017.
 */
public class OrderInput {

    private String userId;
    private double quantity;
    private double pricePerKg;
    private OrderType orderType;

    public OrderInput() {
    }

    public OrderInput(String userId, double quantity, double pricePerKg, String orderType) {
        this.userId = userId;
        this.quantity = quantity;
        this.pricePerKg = pricePerKg;
//        this.orderType = orderType;
        setOrderType(orderType);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        if(orderType != null)
            this.orderType = OrderType.valueOf(orderType);
    }

    @Override
    public String toString() {
        return "OrderInput{" +
                "userId='" + userId + '\'' +
                ", quantity=" + quantity +
                ", pricePerKg=" + pricePerKg +
                ", orderType=" + orderType +
                '}';
    }
}

