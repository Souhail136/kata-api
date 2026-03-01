package com.kata.shopping.domain.model;

import com.kata.shopping.domain.model.valueobject.Money;
import com.kata.shopping.domain.model.valueobject.Quantity;

/**
 * Ligne de facture : produit résolu + taxes calculées + quantité.
 */
public record InvoiceLine(
        Product product,
        Quantity quantity,
        TaxBreakdown taxBreakdown
) {
    public Money totalHT() {
        return product.getPrixHT().multiply(quantity.value());
    }

    public Money totalTaxes() {
        return taxBreakdown.totalTaxes().multiply(quantity.value());
    }

    public Money totalTTC() {
        return taxBreakdown.prixTTC().multiply(quantity.value());
    }
}
