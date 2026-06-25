package com.streaming.domain.billing;

import java.util.Objects;

public final class CardNumber {

    private static final int MIN_LENGTH = 13;
    private static final int MAX_LENGTH = 19;
    private static final int VISIBLE_DIGITS = 4;

    private final String value;

    private CardNumber(String rawValue) {
        if (rawValue == null) {
            throw new IllegalArgumentException("card number must not be null");
        }
        String digits = rawValue.replaceAll("\\s+", "");
        if (!digits.matches("\\d{" + MIN_LENGTH + "," + MAX_LENGTH + "}")) {
            throw new IllegalArgumentException("card number must contain "
                    + MIN_LENGTH + " to " + MAX_LENGTH + " digits");
        }
        this.value = digits;
    }

    public static CardNumber of(String rawValue) {
        return new CardNumber(rawValue);
    }

    public String value() {
        return value;
    }

    public String masked() {
        String last = value.substring(value.length() - VISIBLE_DIGITS);
        return "**** **** **** " + last;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof CardNumber that && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return masked();
    }
}
