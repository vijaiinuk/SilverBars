package com.silverbars.service;

import com.silverbars.dao.OrderDAOI;
import com.silverbars.exception.InvalidInputException;
import com.silverbars.service.OrderService;
import com.silverbars.vo.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Or;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Vijay on 2/11/2017.
 */
public class OrderServiceTest {

    public static final String BUY = "BUY";
    public static final String SELL = "SELL";
    private OrderDAOI orderDAO;
    private OrderService orderService;

    @Before
    public void setUp() throws Exception {
        orderDAO = Mockito.mock(OrderDAOI.class);
        orderService = new OrderService(orderDAO);
    }

    @Test(expected = InvalidInputException.class)
    public void createOrder_invalidInput_throws_exception() throws Exception {
        OrderInput orderInput = new OrderInputBuilder().createOrderInput();
        orderService.createOrder(orderInput);
    }

    @Test(expected = InvalidInputException.class)
    public void createOrder_NullInput_throws_exception() throws Exception {
        OrderInput orderInput = null;
        orderService.createOrder(orderInput);
    }

    @Test
    public void createOrder_validInput_invokes_dao() throws Exception {
        OrderInput orderInput = new OrderInputBuilder().userId("userid").quantity(100).pricePerKg(200).orderType(BUY).createOrderInput();
        when(orderDAO.createOrder(orderInput)).thenReturn(200L);
        assertEquals(200, orderService.createOrder(orderInput).longValue());
        verify(orderDAO, times(1)).createOrder(orderInput);
    }

    @Test
    public void fetchOrder_nvokes_dao() throws Exception {
        Order order = new Order();
        when(orderDAO.fetchOrder(101)).thenReturn(order);
        assertEquals(order, orderService.fetchOrder(101));
        verify(orderDAO, times(1)).fetchOrder(101);
    }

    @Test
    public void deleteOrder_callsDAO_returns_true() throws Exception {
        when(orderDAO.deleteOrder(200)).thenReturn(true);
        assertEquals(true, orderService.deleteOrder(200));
        verify(orderDAO, times(1)).deleteOrder(200);
    }

    @Test
    public void deleteOrder_callsDAO_returns_false() throws Exception {
        when(orderDAO.deleteOrder(200)).thenReturn(false);
        assertEquals(false, orderService.deleteOrder(200));
        verify(orderDAO, times(1)).deleteOrder(200);
    }

    @Test
    public void calculateCurrentPositions_returns_emptyCollection_OnEmptyOrders() throws Exception {
        when(orderDAO.fetchAllOrders()).thenReturn(new ArrayList<Order>());
        assertEquals(0, orderService.calculateCurrentPositions().size());
        verify(orderDAO, times(1)).fetchAllOrders(null);
    }

    @Test
    public void calculateCurrentPositions_returns_positions_OnBUYOrders() throws Exception {
        Set<Order> orders = new HashSet<>();
        orders.add(getOrder(100, 200, BUY));
        orders.add(getOrder(100, 100, BUY));
        orders.add(getOrder(200, 200, BUY));

        Collection<Position> positions = new ArrayList<>();
        positions.add(new Position(200, 200, BUY));
        positions.add(new Position(300, 100, BUY));
        when(orderDAO.fetchAllOrders(BUY)).thenReturn(orders);
        Collection<Position> positionCollection = orderService.calculateCurrentPositions(BUY);
        assertEquals(2, positionCollection.size());
        assertArrayEquals(positions.toArray(), positionCollection.toArray());
        verify(orderDAO, times(1)).fetchAllOrders(BUY);
    }

    @Test
    public void calculateCurrentPositions_returns_positions_OnSELLOrders() throws Exception {
        Set<Order> orders = new HashSet<>();
        orders.add(getOrder(100, 200, SELL));
        orders.add(getOrder(100, 100, SELL));
        orders.add(getOrder(200, 200, SELL));

        Collection<Position> positions = new ArrayList<>();
        positions.add(new Position(300, 100, SELL));
        positions.add(new Position(200, 200, SELL));
        when(orderDAO.fetchAllOrders(SELL)).thenReturn(orders);
        Collection<Position> positionCollection = orderService.calculateCurrentPositions(SELL);
        assertEquals(2, positionCollection.size());
        assertArrayEquals(positions.toArray(), positionCollection.toArray());
        verify(orderDAO, times(1)).fetchAllOrders(SELL);
    }

    @Test
    public void calculateCurrentPositions_returns_both_buysell_positions_OnBUYSELLOrders() throws Exception {
        Set<Order> orders = new HashSet<>();
        orders.add(getOrder(100, 200, SELL));
        orders.add(getOrder(100, 100, SELL));
        orders.add(getOrder(200, 200, SELL));

        orders.add(getOrder(100, 200, BUY));
        orders.add(getOrder(100, 100, BUY));
        orders.add(getOrder(200, 200, BUY));

        Collection<Position> positions = new ArrayList<>();
        positions.add(new Position(200, 200, BUY));
        positions.add(new Position(300, 100, BUY));
        positions.add(new Position(300, 100, SELL));
        positions.add(new Position(200, 200, SELL));
        when(orderDAO.fetchAllOrders(null)).thenReturn(orders);
        Collection<Position> positionCollection = orderService.calculateCurrentPositions();
        assertEquals(4, positionCollection.size());
        assertArrayEquals(positions.toArray(), positionCollection.toArray());
        verify(orderDAO, times(1)).fetchAllOrders(null);
    }

    @Test
    public void calculateCurrentPositions_returns_buy_positions_OnBUYSELLOrders() throws Exception {
        Set<Order> orders = new HashSet<>();
        orders.add(getOrder(100, 200, BUY));
        orders.add(getOrder(100, 100, BUY));
        orders.add(getOrder(200, 200, BUY));

        Collection<Position> positions = new ArrayList<>();
        positions.add(new Position(200, 200, BUY));
        positions.add(new Position(300, 100, BUY));
        when(orderDAO.fetchAllOrders(BUY)).thenReturn(orders);
        Collection<Position> positionCollection = orderService.calculateCurrentPositions(BUY);
        assertEquals(2, positionCollection.size());
        assertArrayEquals(positions.toArray(), positionCollection.toArray());
        verify(orderDAO, times(1)).fetchAllOrders(BUY);
    }

    private Order getOrder(double price, double qty, String orderType) {
        OrderInput orderInput = new OrderInputBuilder().orderType(orderType).pricePerKg(price).quantity(qty).createOrderInput();
        return new OrderBuilder().setInput(orderInput).setOrderId(101).setOrderStatus(OrderStatus.NEW).createOrder();
    }

}