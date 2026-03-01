package com.kata.shopping.application.dto;

import com.kata.shopping.domain.model.ProductType;

import java.math.BigDecimal;

/**
 * Vue lecture d'un produit exposée aux adaptateurs primaires.
 * Inclut le prix TTC calculé.
 */
public record ProductView(
        int id,
        String libelle,
        boolean importe,
        ProductType type,
        BigDecimal prixHT,
        BigDecimal prixTTC
) {
}
