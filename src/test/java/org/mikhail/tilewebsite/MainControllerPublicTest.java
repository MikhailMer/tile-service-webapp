package org.mikhail.tilewebsite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mikhail.tilewebsite.dto.OrderDTO;
import org.mikhail.tilewebsite.dto.OrderZoneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerPublicTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private TurnstileValidationService validationService;

    @BeforeEach
    public void setUp() {
        lenient().when(validationService.isTokenValid("mock-token")).thenReturn(true);
    }

    @Test
    public void sendOrder_ValidData_ShouldSaveAndRedirect() throws Exception {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setName("Michael");
        orderDTO.setPhone("+1234567890");
        orderDTO.setMiddleName("");
        orderDTO.setTurnstileToken("mock-token");

        OrderZoneDTO zone = new OrderZoneDTO();
        zone.setType("Shower");
        zone.setArea(15);
        zone.setRemoval(true);
        orderDTO.setZones(List.of(zone));

        mockMvc.perform(post("/order/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Order with zones saved successfully"));

        verify(orderRepository, times(1)).save(any());
    }

    @Test
    public void sendOrder_BotTriggered_ShouldBlockAndNotSave() throws Exception {

        OrderDTO botDTO = new OrderDTO();
        botDTO.setName("SpamBot");
        botDTO.setPhone("+1111111111");
        botDTO.setMiddleName("Robot");
        botDTO.setTurnstileToken("mock-token");

        OrderZoneDTO zone = new OrderZoneDTO();
        zone.setType("Floor");
        zone.setArea(20);
        zone.setRemoval(true);
        botDTO.setZones(List.of(zone));


        mockMvc.perform(post("/order/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(botDTO))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(orderRepository, never()).save(any());
    }

    @Test
    public void sendOrder_InvalidData_ShouldReturnFormWithError() throws Exception {

        OrderDTO badDTO = new OrderDTO();
        badDTO.setName("");
        badDTO.setPhone("");
        badDTO.setMiddleName("");
        badDTO.setTurnstileToken("mock-token");

        mockMvc.perform(post("/order/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(orderRepository, never()).save(any());
    }

    @Test
    public void sendOrder_InvalidCaptcha_ShouldReturnBadRequest() throws Exception {
        when(validationService.isTokenValid("invalid-token")).thenReturn(false);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setName("Michael");
        orderDTO.setPhone("+1234567890");
        orderDTO.setTurnstileToken("invalid-token");

        OrderZoneDTO zone = new OrderZoneDTO();
        zone.setType("Shower");
        zone.setArea(15);
        zone.setRemoval(true);
        orderDTO.setZones(List.of(zone));

        mockMvc.perform(post("/order/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CAPTCHA not passed. Access blocked.."));

        verify(orderRepository, never()).save(any());
    }
}
