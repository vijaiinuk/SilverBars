package com.silverbars;

import com.silverbars.dao.OrderDAOI;
import com.silverbars.service.OrderServiceI;
import com.silverbars.vo.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import javax.xml.ws.Response;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Vijay on 2/10/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class OrderControllerRestTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private java.lang.String userName = "admin";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private OrderServiceI orderService;

    @Autowired
    private OrderDAOI orderDAO;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception  {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        orderDAO.removeAllOrders();
    }



    @Test
    public void createOrder() throws Exception {
        String jsonInput = json(new OrderInputBuilder().userId("vijay").quantity(100).pricePerKg(12).orderType("BUY").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput))
        .andExpect(status().isOk());

    }

    @Test
    public void fetchOrder() throws Exception {
        OrderInput orderInput = new OrderInputBuilder().userId("vijay").quantity(100).pricePerKg(12).orderType("BUY").createOrderInput();
        String jsonInput = json(orderInput);

        String result = mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput))
                .andReturn().getResponse().getContentAsString();

        Order order = new Order(orderInput, Long.valueOf(result), OrderStatus.CREATED);
        String jsonOutput = json(order);
        mockMvc.perform(get("/orders/order/"+result))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonOutput));

    }

    @Test
    public void fetchOrder_invalid_order_returns_emptyResult() throws Exception {
        mockMvc.perform(get("/orders/order/100"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

    }

    @Test
    public void deleteOrder()  throws Exception  {
        String jsonInput = json(new OrderInputBuilder().userId("vijay").quantity(100).pricePerKg(12).orderType("BUY").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput));

        mockMvc.perform(delete("/orders/order/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void fetchPositions()  throws Exception  {
        String jsonInput = json(new OrderInputBuilder().userId("vijay").quantity(100).pricePerKg(12).orderType("BUY").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput));

        String jsonInput2 = json(new OrderInputBuilder().userId("vijay").quantity(200).pricePerKg(12).orderType("BUY").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput2));

        String jsonInput3 = json(new OrderInputBuilder().userId("vijay").quantity(150).pricePerKg(25).orderType("SELL").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput3));

        List<Position> positions = new ArrayList<>();
        positions.add(new Position(300, 12, "BUY"));
        positions.add(new Position(150, 25, "SELL"));

        mockMvc.perform(get("/orders/position/list/BUY"))
                .andExpect(status().isOk())
                .andExpect(
                        content().string("[{\"cumulativeQty\":300.0,\"pricePerKg\":12.0,\"orderType\":\"BUY\",\"positionString\":\"BUY: 300.0 kg for £12.0\"}]"));
        mockMvc.perform(get("/orders/position/list/SELL"))
                .andExpect(status().isOk())
                .andExpect(
            content().string("[{\"cumulativeQty\":150.0,\"pricePerKg\":25.0,\"orderType\":\"SELL\",\"positionString\":\"SELL: 150.0 kg for £25.0\"}]"));
    }

    @Test
    public void getLiveBoard()  throws Exception {
        String jsonInput = json(new OrderInputBuilder().userId("vijay").quantity(100).pricePerKg(12).orderType("BUY").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput));

        String jsonInput2 = json(new OrderInputBuilder().userId("vijay").quantity(200).pricePerKg(12).orderType("BUY").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput2));

        String jsonInput3 = json(new OrderInputBuilder().userId("vijay").quantity(150).pricePerKg(25).orderType("SELL").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput3));

        mockMvc.perform(get("/orders/liveBoard/BUY"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"BUY: 300.0 kg for £12.0\"]"));

        mockMvc.perform(get("/orders/liveBoard/SELL"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"SELL: 150.0 kg for £25.0\"]"));

        //Add few more BUY orders and validate
        String jsonInput4 = json(new OrderInputBuilder().userId("vijay").quantity(200).pricePerKg(20).orderType("BUY").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput4));

        String jsonInput5 = json(new OrderInputBuilder().userId("vijay").quantity(150).pricePerKg(15).orderType("BUY").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput5));

        mockMvc.perform(get("/orders/liveBoard/BUY"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"BUY: 200.0 kg for £20.0\",\"BUY: 150.0 kg for £15.0\",\"BUY: 300.0 kg for £12.0\"]"));

        //Add few more SELL orders and validate
        String jsonInput6 = json(new OrderInputBuilder().userId("vijay").quantity(200).pricePerKg(20).orderType("SELL").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput6));

        String jsonInput7 = json(new OrderInputBuilder().userId("vijay").quantity(150).pricePerKg(15).orderType("SELL").createOrderInput());

        mockMvc.perform(post("/orders/order")
                .contentType(contentType)
                .content(jsonInput7));

        mockMvc.perform(get("/orders/liveBoard/SELL"))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"SELL: 150.0 kg for £15.0\",\"SELL: 200.0 kg for £20.0\",\"SELL: 150.0 kg for £25.0\"]"));

    }
    protected java.lang.String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}