package com.silverbars.vo;

/**
 * Created by Vijay on 2/10/2017.
 */
public class Position {
    private double cumulativeQty;
    private double pricePerKg;
    private String orderType;

    public Position() {
    }

    public Position(double cumulativeQty, double pricePerKg, String orderType) {
        this.cumulativeQty = cumulativeQty;
        this.pricePerKg = pricePerKg;
        this.orderType = orderType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public double getCumulativeQty() {
        return cumulativeQty;
    }

    public void setCumulativeQty(double cumulativeQty) {
        this.cumulativeQty = cumulativeQty;
    }

    public double addToQty(double qty)  {
        cumulativeQty += qty;
        return  cumulativeQty;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public String getPositionString()  {
        return orderType+": "+cumulativeQty+" kg for £"+pricePerKg;
    }
    @Override
    public String toString() {
        return "Position{" +
                "cumulativeQty=" + cumulativeQty +
                ", pricePerKg=" + pricePerKg +
                ", orderType='" + orderType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (Double.compare(position.cumulativeQty, cumulativeQty) != 0) return false;
        if (Double.compare(position.pricePerKg, pricePerKg) != 0) return false;
        return orderType != null ? orderType.equals(position.orderType) : position.orderType == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(cumulativeQty);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(pricePerKg);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (orderType != null ? orderType.hashCode() : 0);
        return result;
    }
}
