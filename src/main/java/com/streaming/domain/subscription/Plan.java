package com.streaming.domain.subscription;

import com.streaming.shared.domain.Money;

public class Plan {

    private Long id;
    private final String name;
    private final Money price;
    private final int durationDays;

    private Plan(Long id, String name, Money price, int durationDays) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (price == null) {
            throw new IllegalArgumentException("price must not be null");
        }
        if (durationDays <= 0) {
            throw new IllegalArgumentException("durationDays must be positive");
        }
        this.id = id;
        this.name = name.trim();
        this.price = price;
        this.durationDays = durationDays;
    }

    public static Plan create(String name, Money price, int durationDays) {
        return new Plan(null, name, price, durationDays);
    }

    public static Plan reconstitute(Long id, String name, Money price, int durationDays) {
        return new Plan(id, name, price, durationDays);
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Money price() {
        return price;
    }

    public int durationDays() {
        return durationDays;
    }
}
