package com.kata.shopping.application.dto;

import java.util.List;

/**
 * Commande applicative représentant un panier soumis pour facturation.
 * Reçue depuis un adaptateur primaire (ex. REST), validée avant entrée dans le domaine.
 */
public record CartCommand(List<CartItemCommand> items) {

    public record CartItemCommand(int productId, int quantity) {}
}
