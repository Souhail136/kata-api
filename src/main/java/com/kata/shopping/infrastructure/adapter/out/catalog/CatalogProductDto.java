package com.kata.shopping.infrastructure.adapter.out.catalog;

import com.kata.shopping.domain.model.ProductType;

import java.math.BigDecimal;

/**
 * DTO de désérialisation de la réponse de l'API catalogue externe.
 * Isolé dans l'infrastructure — le domaine n'en a pas connaissance.
 */
public record CatalogProductDto(
        Integer id,
        String libelle,
        boolean importation,
        ProductType type,
        BigDecimal prixHT
) {}
