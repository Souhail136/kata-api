package com.kata.shopping.domain.exception;

import com.kata.shopping.domain.model.valueobject.ProductId;

/**
 * Exception levée lorsqu'un produit référencé dans un panier est introuvable dans le catalogue.
 */
public class ProductNotFoundException extends RuntimeException {

    private final ProductId productId;

    public ProductNotFoundException(ProductId productId) {
        super("Produit introuvable dans le catalogue : id=" + productId.value());
        this.productId = productId;
    }

    public ProductId getProductId() {
        return productId;
    }
}
