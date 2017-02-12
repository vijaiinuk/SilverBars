package com.silverbars.service;

import com.silverbars.exception.InvalidInputException;
import com.silverbars.vo.Order;
import com.silverbars.vo.OrderInput;
import com.silverbars.vo.Position;

import java.util.Collection;
import java.util.List;

/**
 * Created by Vijay on 2/11/2017.
 */
public interface OrderServiceI {
    Long createOrder(OrderInput orderInput) throws InvalidInputException;

    boolean deleteOrder(long orderId);

    Collection<Position> calculateCurrentPositions();

    List<String> getLiveBoard(String orderType);

    Collection<Position> calculateCurrentPositions(String orderType);

    Order fetchOrder(long orderId);
}
