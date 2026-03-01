package com.kata.shopping.domain.model.valueobject;

/**
 * Value Object représentant l'identifiant d'un produit.
 */
public record ProductId(int value) {

    public ProductId {
        if (value <= 0) {
            throw new IllegalArgumentException("L'identifiant produit doit être positif, reçu : " + value);
        }
    }

    public static ProductId of(int value) {
        return new ProductId(value);
    }
}
