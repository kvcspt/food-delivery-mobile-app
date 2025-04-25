package hu.kvcspt.mobilalkfejl_kotprog.models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Order {
    private Cart cart;
    private String user;
    private Date createdAt;
    private double total;

    public Order() {
    }

    public Order(Cart cart, String user, double total) {
        this.cart = cart;
        this.user = user;
        this.total = total;
        this.createdAt = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    public Cart getCart() {
        return cart;
    }

    public String getUser() {
        return user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public double getTotal() {
        return total;
    }
}
