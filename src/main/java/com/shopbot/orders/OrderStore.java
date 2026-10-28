package com.shopbot.orders;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** In-memory fake order database. */
@Component
public class OrderStore {

    public record Order(long id, String item, String status) {
    }

    private final ConcurrentMap<Long, Order> orders = new ConcurrentHashMap<>();

    public OrderStore() {
        reset();
    }

    public void reset() {
        orders.clear();
        orders.put(1042L, new Order(1042, "Wireless Mouse", "PROCESSING"));
        orders.put(1043L, new Order(1043, "Mechanical Keyboard", "SHIPPED"));
        orders.put(1044L, new Order(1044, "USB-C Hub", "DELIVERED"));
        orders.put(1045L, new Order(1045, "Laptop Stand", "PROCESSING"));
    }

    public Optional<Order> find(long id) {
        return Optional.ofNullable(orders.get(id));
    }

    public void setStatus(long id, String status) {
        orders.computeIfPresent(id, (k, o) -> new Order(o.id(), o.item(), status));
    }
}