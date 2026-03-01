package com.kata.shopping.infrastructure.adapter.in.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * DTOs spécifiques à la couche REST.
 * Distincts des DTOs applicatifs pour permettre l'évolution indépendante du contrat HTTP.
 */
final class WebDtos {

    private WebDtos() {}

    record CartRequest(
            @NotEmpty(message = "Le panier doit contenir au moins un article")
            @Valid
            List<CartItemRequest> items
    ) {
        record CartItemRequest(
                @NotNull(message = "L'identifiant produit est obligatoire")
                @Positive(message = "L'identifiant produit doit être positif")
                Integer productId,

                @Positive(message = "La quantité doit être strictement positive")
                int quantity
        ) {}
    }
}
