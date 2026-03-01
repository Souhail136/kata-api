package com.kata.shopping.application.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Vue lecture d'une facture exposée aux adaptateurs primaires.
 */
public record InvoiceView(
        List<InvoiceLineView> lignes,
        BigDecimal totalHT,
        BigDecimal totalTaxes,
        BigDecimal totalTTC
) {
    public record InvoiceLineView(
            int productId,
            String libelle,
            int quantite,
            BigDecimal prixUnitaireHT,
            BigDecimal taxeTVA,
            BigDecimal taxeImportation,
            BigDecimal prixUnitaireTTC,
            BigDecimal totalTTC
    ) {
    }
}
