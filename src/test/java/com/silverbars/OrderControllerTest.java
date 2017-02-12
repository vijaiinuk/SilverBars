package com.silverbars;

import com.silverbars.exception.InvalidInputException;
import com.silverbars.service.OrderServiceI;
import com.silverbars.vo.Order;
import com.silverbars.vo.OrderInput;
import com.silverbars.vo.OrderInputBuilder;
import com.silverbars.vo.Position;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Vijay on 2/11/2017.
 */
public class OrderControllerTest {

    private OrderServiceI orderService;

    private OrderController orderController;

    @Before
    public void setUp() throws Exception {
        orderService = Mockito.mock(OrderServiceI.class);
        orderController = new OrderController(orderService);
    }

    @Test
    public void createOrder_invokes_serviceMethod() throws InvalidInputException {
        OrderInput orderInput = new OrderInputBuilder().userId("userid").quantity(100).pricePerKg(200).orderType("BUY").createOrderInput();
        when(orderService.createOrder(orderInput)).thenReturn(100L);
        assertEquals(100, orderController.createOrder(orderInput).longValue());
        verify(orderService, times(1)).createOrder(orderInput);
    }

    @Test
    public void fetchOrder_invokes_serviceMethod() throws InvalidInputException {
        Order order = new Order();
        when(orderService.fetchOrder(101)).thenReturn(order);
        assertEquals(order, orderController.fetchOrder(101));
        verify(orderService, times(1)).fetchOrder(101);
    }

    @Test(expected = InvalidInputException.class)
    public void createOrder_throws_exceptionOnInvalidInput() throws InvalidInputException {
        OrderInput orderInput = new OrderInputBuilder().createOrderInput();
        when(orderService.createOrder(orderInput)).thenThrow(InvalidInputException.class);
        assertEquals(100, orderController.createOrder(orderInput).longValue());
    }

    @Test
    public void deleteOrder() throws Exception {
        when(orderService.deleteOrder(100)).thenReturn(true);
        assertEquals(true, orderController.deleteOrder(100));
        verify(orderService, times(1)).deleteOrder(100);
    }

    @Test
    public void deleteOrder_when_notDeleted_returnFalse() throws Exception {
        when(orderService.deleteOrder(0)).thenReturn(false);
        assertEquals(false, orderController.deleteOrder(0));
        verify(orderService, times(1)).deleteOrder(0);
    }

    @Test
    public void getPositions() throws Exception {
        when(orderService.calculateCurrentPositions("BUY")).thenReturn(new ArrayList<Position>());
        assertEquals(new ArrayList<Position>(), orderController.getPositions("BUY"));
        verify(orderService, times(1)).calculateCurrentPositions("BUY");
    }

}