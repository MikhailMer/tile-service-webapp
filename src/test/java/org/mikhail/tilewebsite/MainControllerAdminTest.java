package org.mikhail.tilewebsite;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MainControllerAdminTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    private final String adminPageUrl = "/admin/orders";
    private final String deleteOrderUrl = "/admin/orders/delete/";

    @Test
    public void adminPage_AnonymousUser_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get(adminPageUrl))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void adminPage_RegularUser_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get(adminPageUrl))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void adminPage_AdminUser_ShouldOpenSuccessfully() throws Exception {
        mockMvc.perform(get(adminPageUrl))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-orders"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteOrder_AdminUser_ShouldDeleteAndRedirect() throws Exception {
        long testId = 1L;

        mockMvc.perform(post(deleteOrderUrl + testId)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(adminPageUrl));
    }

}
