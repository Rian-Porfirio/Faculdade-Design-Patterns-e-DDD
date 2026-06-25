package com.streaming.domain.billing;

public class Card {

    private Long id;
    private final CardNumber number;
    private final String holder;
    private final String expiration;
    private final String cvv;
    private CardStatus status;

    private Card(Long id, CardNumber number, String holder, String expiration,
                 String cvv, CardStatus status) {
        if (holder == null || holder.isBlank()) {
            throw new IllegalArgumentException("holder must not be blank");
        }
        if (expiration == null || !expiration.matches("\\d{2}/\\d{4}")) {
            throw new IllegalArgumentException("expiration must follow MM/yyyy");
        }
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            throw new IllegalArgumentException("cvv must contain 3 or 4 digits");
        }
        this.id = id;
        this.number = number;
        this.holder = holder.trim();
        this.expiration = expiration;
        this.cvv = cvv;
        this.status = status;
    }

    public static Card create(CardNumber number, String holder, String expiration, String cvv) {
        return new Card(null, number, holder, expiration, cvv, CardStatus.ACTIVE);
    }

    public static Card reconstitute(Long id, CardNumber number, String holder, String expiration,
                                    String cvv, CardStatus status) {
        return new Card(id, number, holder, expiration, cvv, status);
    }

    public void activate() {
        this.status = CardStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = CardStatus.INACTIVE;
    }

    public void block() {
        this.status = CardStatus.BLOCKED;
    }

    public boolean isActive() {
        return status == CardStatus.ACTIVE;
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public CardNumber number() {
        return number;
    }

    public String holder() {
        return holder;
    }

    public String expiration() {
        return expiration;
    }

    public String cvv() {
        return cvv;
    }

    public CardStatus status() {
        return status;
    }
}
