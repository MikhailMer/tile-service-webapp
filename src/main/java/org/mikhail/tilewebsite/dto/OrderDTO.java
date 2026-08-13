package org.mikhail.tilewebsite.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class OrderDTO {

    @NotBlank(message = "The name cannot be empty.")
    @Size(min = 2, max = 50, message = "The name must be between 2 and 50 characters.")
    private String name;

    private String middleName;

    @NotBlank(message = "The phone number cannot be empty.")
    @Pattern(regexp = "^\\+?[1]?[-.\\s]?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$", message = "Invalid US phone number format")
    private String phone;

    @NotEmpty(message = "At least one area must be selected in the order.")
    @Valid
    private List<OrderZoneDTO> zones;



    private String turnstileToken;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<OrderZoneDTO> getZones() {
        return zones;
    }

    public void setZones(List<OrderZoneDTO> zones) {
        this.zones = zones;
    }

    public String getTurnstileToken() {
        return turnstileToken;
    }

    public void setTurnstileToken(String turnstileToken) {
        this.turnstileToken = turnstileToken;
    }
}
