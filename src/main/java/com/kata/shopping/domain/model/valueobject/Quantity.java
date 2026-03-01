package com.kata.shopping.domain.model.valueobject;

/**
 * Value Object représentant une quantité commandée.
 * Toujours strictement positive.
 */
public record Quantity(int value) {

    public Quantity {
        if (value <= 0) {
            throw new IllegalArgumentException("La quantité doit être strictement positive, reçu : " + value);
        }
    }

    public static Quantity of(int value) {
        return new Quantity(value);
    }
}
