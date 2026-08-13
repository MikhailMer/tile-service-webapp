package org.mikhail.tilewebsite;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name", nullable = false)
    private String name;

    @Column(name = "client_phone", nullable = false)
    private String phone;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderZone> zones = new ArrayList<>();

    public Order() {
    }

    public Order(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public void addZone(OrderZone zone) {
        zones.add(zone);
        zone.setOrder(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<OrderZone> getZones() { return zones; }

    public void setZones(List<OrderZone> zones) { this.zones = zones; }
}
