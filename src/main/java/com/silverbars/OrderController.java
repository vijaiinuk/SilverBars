package com.silverbars;

import com.silverbars.exception.InvalidInputException;
import com.silverbars.vo.Order;
import com.silverbars.vo.OrderInput;
import com.silverbars.service.OrderServiceI;
import com.silverbars.vo.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by Vijay on 2/10/2017.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderServiceI orderService;

    @Autowired
    public OrderController(OrderServiceI orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public Long createOrder(@RequestBody OrderInput orderInput) throws InvalidInputException {
        return orderService.createOrder(orderInput);
    }

    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.DELETE)
    public boolean deleteOrder(@PathVariable long orderId)  {
        return orderService.deleteOrder(orderId);
    }

    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
    public Order fetchOrder(@PathVariable long orderId)  {
        return orderService.fetchOrder(orderId);
    }

    @RequestMapping(value = "/position/list/{orderType}", method = RequestMethod.GET)
    public Collection<Position> getPositions(@PathVariable String orderType)  {
        return orderService.calculateCurrentPositions(orderType);
    }

    @RequestMapping(value = "/liveBoard/{orderType}", method = RequestMethod.GET)
    public List<String> getLiveBoard(@PathVariable String orderType)  {
        return orderService.getLiveBoard(orderType);
    }

}