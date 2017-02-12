package com.silverbars.service;

import com.silverbars.dao.OrderDAOI;
import com.silverbars.exception.InvalidInputException;
import com.silverbars.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Created by Vijay on 2/10/2017.
 */
@Component
public class OrderService implements OrderServiceI {

    @Autowired
    OrderDAOI orderDAO;

    public OrderService(OrderDAOI orderDAO)  {
        this.orderDAO = orderDAO;
    }


    @Override
    public Long createOrder(OrderInput orderInput) throws InvalidInputException {
        if(!validInput(orderInput))  {
            throw new InvalidInputException("Invalid Order Input");
        }
        System.out.println("OrderService called with : "+orderInput);
        return orderDAO.createOrder(orderInput);
    }

    private boolean validInput(OrderInput orderInput) {
        if(orderInput != null)  {
            if(orderInput.getOrderType() == null)  {
                return false;
            }

            if(orderInput.getQuantity() == 0)  {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteOrder(long orderId) {
        return orderDAO.deleteOrder(orderId);
    }

    @Override
    public Collection<Position> calculateCurrentPositions() {
        return calculateCurrentPositions(null);
    }

    @Override
    public List<String> getLiveBoard(String orderType) {
        Collection<Position> positions = calculateCurrentPositions(orderType);
        return positions.stream().map(p -> p.getPositionString()).collect(toList());
    }

    @Override
    public Collection<Position> calculateCurrentPositions(String orderType) {
        Map<String, Position> positions = new HashMap<>();

        Collection<Order> orders = orderDAO.fetchAllOrders(orderType);
        System.out.println(orders);
        for (Order order : orders) {
            if(order.getOrderStatus() != OrderStatus.CANCELLED)  {
                String key = order.getOrderType() + "-" + order.getPricePerKg();
                if(positions.containsKey(key))  {
                    Position position = positions.get(key);
                    position.addToQty(order.getQuantity());
                } else  {
                    Position position = new Position();
                    position.setCumulativeQty(order.getQuantity());
                    position.setPricePerKg(order.getPricePerKg());
                    position.setOrderType(order.getOrderType().toString());
                    positions.put(key, position);
                }
            }
        }

        return reOrderCollection(positions.values());
    }

    @Override
    public Order fetchOrder(long orderId) {
        return orderDAO.fetchOrder(orderId);
    }

    private Collection<Position> reOrderCollection(Collection<Position> values) {

        List<Position> buyPositions = new ArrayList<>();
        List<Position> sellPositions = new ArrayList<>();

        for (Position value : values) {
            if(value.getOrderType().equals(OrderType.BUY.toString()))  {
                buyPositions.add(value);
            } else {
                sellPositions.add(value);
            }
        }

        Collections.sort(buyPositions, (x, y) -> x.getPricePerKg() < y.getPricePerKg() ? 1 : -1);
        Collections.sort(sellPositions, (x, y) -> x.getPricePerKg() > y.getPricePerKg() ? 1 : -1);

        List<Position> result = new ArrayList<>(buyPositions);
        result.addAll(sellPositions);
        return result;
    }
}
