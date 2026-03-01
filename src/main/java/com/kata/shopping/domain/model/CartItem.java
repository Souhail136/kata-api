package com.kata.shopping.domain.model;

import com.kata.shopping.domain.model.valueobject.ProductId;
import com.kata.shopping.domain.model.valueobject.Quantity;

import java.util.Objects;

/**
 * Représente une ligne d'un panier : un produit et sa quantité commandée.
 */
public final class CartItem {

    private final ProductId productId;
    private final Quantity quantity;

    public CartItem(ProductId productId, Quantity quantity) {
        this.productId = Objects.requireNonNull(productId, "L'identifiant produit est obligatoire");
        this.quantity = Objects.requireNonNull(quantity, "La quantité est obligatoire");
    }

    public ProductId getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
