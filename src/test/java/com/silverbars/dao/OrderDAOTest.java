package com.silverbars.dao;

import com.silverbars.vo.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

/**
 * Created by Vijay on 2/12/2017.
 */
public class OrderDAOTest {

    public static final String BUY = "BUY";
    public static final String SELL = "SELL";
    private OrderDAO orderDAO;

    private ConcurrentHashMap<Long, Order> map;

    private AtomicLong idCounter;

    @Before
    public void setup()  {
        orderDAO = new OrderDAO();
        map = new ConcurrentHashMap<>();
        idCounter = new AtomicLong();
        orderDAO.setOrdersCache(map);
        orderDAO.setIdCounter(idCounter);
    }

    @Test
    public void createOrder_creates_order_with_orderId_status() throws Exception {
        OrderInput orderInput = new OrderInputBuilder().orderType(BUY).pricePerKg(100).quantity(1000).createOrderInput();
        assertEquals(1, orderDAO.createOrder(orderInput).longValue());
        Order order = map.get(1L);
        assertEquals(1, order.getOrderId());
        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
    }

    @Test
    public void fetchOrder_returns_order_with_orderId_status() throws Exception {
        OrderInput orderInput = new OrderInputBuilder().orderType(BUY).pricePerKg(100).quantity(1000).createOrderInput();
        orderDAO.createOrder(orderInput);
        Order order = map.get(1L);
        assertEquals(order, orderDAO.fetchOrder(1L));
    }

    @Test
    public void fetchOrder_returns_null_when_no_order() throws Exception {
        assertNull(orderDAO.fetchOrder(1L));
    }

    @Test
    public void deleteOrder_setsStatus_Cancelled() throws Exception {
        OrderInput orderInput = new OrderInputBuilder().orderType(BUY).pricePerKg(100).quantity(1000).createOrderInput();
        orderDAO.createOrder(orderInput).longValue();

        assertTrue(orderDAO.deleteOrder(1L));

        Order deletedOrder = map.get(1L);

        assertEquals(OrderStatus.CANCELLED, deletedOrder.getOrderStatus());

    }

    @Test
    public void deleteOrder_returns_false_onMissingOrder() throws Exception {
        OrderInput orderInput = new OrderInputBuilder().orderType(BUY).pricePerKg(100).quantity(1000).createOrderInput();
        orderDAO.createOrder(orderInput).longValue();

        assertFalse(orderDAO.deleteOrder(100L));
    }

    @Test
    public void fetchAllOrders_returns_emptyCollection_OnNoOrders() throws Exception {
        assertEquals(0, orderDAO.fetchAllOrders().size());
    }

    @Test
    public void fetchAllOrders_returns_collection_OnOrders() throws Exception {
        OrderInput orderInput1 = new OrderInputBuilder().orderType(BUY).pricePerKg(100).quantity(1000).createOrderInput();
        orderDAO.createOrder(orderInput1);

        OrderInput orderInput2 = new OrderInputBuilder().orderType(BUY).pricePerKg(100).quantity(1000).createOrderInput();
        orderDAO.createOrder(orderInput2);

        OrderInput orderInput3 = new OrderInputBuilder().orderType(SELL).pricePerKg(100).quantity(1000).createOrderInput();
        orderDAO.createOrder(orderInput3);

        assertEquals(3, orderDAO.fetchAllOrders().size());
    }

    @Test
    public void removeAllOrders_clears_map() throws Exception {

        OrderInput orderInput1 = new OrderInputBuilder().orderType(BUY).pricePerKg(100).quantity(1000).createOrderInput();
        orderDAO.createOrder(orderInput1);

        OrderInput orderInput2 = new OrderInputBuilder().orderType(BUY).pricePerKg(100).quantity(1000).createOrderInput();
        orderDAO.createOrder(orderInput2);

        orderDAO.removeAllOrders();

        assertEquals(0, map.size());

    }

    @Test
    public void fetchAllOrders() throws Exception {

        OrderInput orderInput1 = new OrderInputBuilder().orderType(BUY).pricePerKg(100).quantity(1000).createOrderInput();
        orderDAO.createOrder(orderInput1);

        OrderInput orderInput2 = new OrderInputBuilder().orderType(BUY).pricePerKg(100).quantity(2000).createOrderInput();
        orderDAO.createOrder(orderInput2);

        OrderInput orderInput3 = new OrderInputBuilder().orderType(BUY).pricePerKg(200).quantity(1000).createOrderInput();
        orderDAO.createOrder(orderInput3);

        OrderInput orderInput4 = new OrderInputBuilder().orderType(SELL).pricePerKg(100).quantity(1500).createOrderInput();
        orderDAO.createOrder(orderInput4);

        OrderInput orderInput5 = new OrderInputBuilder().orderType(SELL).pricePerKg(150).quantity(500).createOrderInput();
        orderDAO.createOrder(orderInput5);

        assertEquals(3, orderDAO.fetchAllOrders(BUY).size());

        assertEquals(2, orderDAO.fetchAllOrders(SELL).size());

        assertEquals(5, orderDAO.fetchAllOrders(null).size());

    }

}