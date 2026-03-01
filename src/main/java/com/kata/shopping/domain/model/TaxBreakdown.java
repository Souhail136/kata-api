package com.kata.shopping.domain.model;

import com.kata.shopping.domain.model.valueobject.Money;

import java.util.Objects;

/**
 * Value Object représentant la décomposition fiscale calculée pour un produit.
 * Produit de {@link com.kata.shopping.domain.service.TaxPolicy}.
 */
public record TaxBreakdown(
        Money taxeTVA,
        Money taxeImportation,
        Money prixTTC
) {
    public TaxBreakdown {
        Objects.requireNonNull(taxeTVA, "taxeTVA obligatoire");
        Objects.requireNonNull(taxeImportation, "taxeImportation obligatoire");
        Objects.requireNonNull(prixTTC, "prixTTC obligatoire");
    }

    public Money totalTaxes() {
        return taxeTVA.add(taxeImportation);
    }
}
