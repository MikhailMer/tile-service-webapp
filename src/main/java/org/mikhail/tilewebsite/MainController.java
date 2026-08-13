package org.mikhail.tilewebsite;

import jakarta.validation.Valid;
import org.mikhail.tilewebsite.config.TurnstileConfig;
import org.mikhail.tilewebsite.dto.OrderDTO;
import org.mikhail.tilewebsite.dto.OrderZoneDTO;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MainController.class);

    private final OrderRepository orderRepository;
    private final TurnstileValidationService validationService;
    private final TurnstileConfig turnstileConfig;

    public MainController(OrderRepository orderRepository,
                          TurnstileValidationService validationService,
                          TurnstileConfig turnstileConfig) {
        this.orderRepository = orderRepository;
        this.validationService = validationService;
        this.turnstileConfig = turnstileConfig;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("turnstileSiteKey", turnstileConfig.getSiteKey());
        return "index";
    }

    @PostMapping("/order/submit")
    @ResponseBody
    public ResponseEntity<String> submitOrder(@Valid @RequestBody OrderDTO orderDTO) {

        if (orderDTO.getMiddleName() != null && !orderDTO.getMiddleName().trim().isEmpty()) {
            log.warn("[SPAM DETECTED] HoneyPot triggered by a bot! Input value: '{}'. Request silently dropped.", orderDTO.getMiddleName());
            return ResponseEntity.ok().build();
        }

        if (!validationService.isTokenValid(orderDTO.getTurnstileToken())) {
            log.warn("[SPAM DETECTED] Cloudflare Turnstile validation failed for order attempt from: '{}'", orderDTO.getName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CAPTCHA not passed. Access blocked..");
        }

        log.info("[ORDER] New project request submission attempt from: '{}', Phone: '{}'",
                orderDTO.getName(), orderDTO.getPhone());

        try {
            Order order = new Order(orderDTO.getName(), orderDTO.getPhone());

            if (orderDTO.getZones() != null) {
                for (OrderZoneDTO zoneDTO : orderDTO.getZones()) {
                    OrderZone zone = new OrderZone();
                    zone.setType(zoneDTO.getType());
                    zone.setArea(zoneDTO.getArea());
                    zone.setRemoval(zoneDTO.getRemoval());

                    zone.setOrder(order);

                    order.getZones().add(zone);
                }
            }
            orderRepository.save(order);

            log.info("[ORDER] Project order successfully saved for '{}'. Total zones registered: {}",
                    order.getName(), order.getZones().size());

            return ResponseEntity.ok("Order with zones saved successfully");
        } catch (Exception e) {
            log.error("[ORDER] Failed to save project request from '{}'. Error details: {}",
                    orderDTO.getName(), e.getMessage());
            return ResponseEntity.badRequest().body("Error saving order: " + e.getMessage());
        }
    }

    @GetMapping("/admin/orders")
    public String showAdminPanel(Model model) {
        List<Order> orders = orderRepository.findAllWithZones();

        model.addAttribute("orders", orders);

        return "admin-orders";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderRepository.findById(id).ifPresentOrElse(
                order -> {
                    log.info("[ADMIN] Security Audit: Order ID: {} for customer '{}' (Phone: '{}') is being PERMANENTLY DELETED.",
                        id, order.getName(), order.getPhone());
                    orderRepository.delete(order);
                },
                () -> log.warn("[ADMIN] Delete attempt failed: Order with ID: {} not found in the database.", id)
            );

        return "redirect:/admin/orders";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("turnstileSiteKey", turnstileConfig.getSiteKey());
        return "login";
    }

}
