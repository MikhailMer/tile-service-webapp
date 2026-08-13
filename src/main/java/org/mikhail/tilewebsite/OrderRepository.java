package org.mikhail.tilewebsite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.zones ORDER BY o.id DESC")
    List<Order> findAllWithZones();
}

