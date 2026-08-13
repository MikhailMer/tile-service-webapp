package org.mikhail.tilewebsite;

import jakarta.persistence.*;

@Entity
@Table(name = "order_zones")
public class OrderZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zone_type", nullable = false)
    private String type;

    @Column(name = "area", nullable = false)
    private Integer area;

    @Column(name = "removal_needed", nullable = false)
    private Boolean removal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderZone() {
    }

    public OrderZone(String type, Integer area, Boolean removal, Order order) {
        this.type = type;
        this.area = area;
        this.removal = removal;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
