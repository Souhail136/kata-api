package com.kata.shopping.domain.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Agrégat Panier.
 * Invariant : doit contenir au moins un article.
 */
public final class Cart {

    private final List<CartItem> items;

    private Cart(List<CartItem> items) {
        this.items = Collections.unmodifiableList(items);
    }

    public static Cart of(List<CartItem> items) {
        Objects.requireNonNull(items, "La liste des articles ne peut pas être null");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Un panier ne peut pas être vide");
        }
        return new Cart(List.copyOf(items));
    }

    public List<CartItem> getItems() {
        return items;
    }
}
