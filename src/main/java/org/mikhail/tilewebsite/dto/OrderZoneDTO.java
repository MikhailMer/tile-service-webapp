package org.mikhail.tilewebsite.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderZoneDTO {

    @NotBlank(message = "The zone type cannot be empty.")
    private String type;

    @NotNull(message = "The area must be specified.")
    @Min(value = 1, message = "The project area must be at least 1 sq ft")
    private Integer area;

    private Boolean removal;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Boolean getRemoval() {
        return removal;
    }

    public void setRemoval(Boolean removal) {
        this.removal = removal;
    }
}
