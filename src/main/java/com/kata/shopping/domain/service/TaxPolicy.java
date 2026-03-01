package com.kata.shopping.domain.service;

import com.kata.shopping.domain.model.Product;
import com.kata.shopping.domain.model.ProductType;
import com.kata.shopping.domain.model.TaxBreakdown;
import com.kata.shopping.domain.model.valueobject.Money;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service domaine encapsulant la politique fiscale.
 *
 * <p>Règles appliquées :
 * <ul>
 *   <li>TVA 0%  — nourriture, médicaments</li>
 *   <li>TVA 10% — livres</li>
 *   <li>TVA 20% — autres produits</li>
 *   <li>Taxe importation 5% — tout produit importé, sans exception</li>
 *   <li>Chaque taxe arrondie indépendamment aux 5 centimes supérieurs</li>
 * </ul>
 *
 * <p>Formule : {@code Pttc = Pht + arrondi(Pht × tva/100) + arrondi(Pht × ti/100)}
 */
@Service
public class TaxPolicy {

    private static final BigDecimal RATE_TVA_ZERO = BigDecimal.ZERO;
    private static final BigDecimal RATE_TVA_REDUITE = new BigDecimal("10");
    private static final BigDecimal RATE_TVA_NORMALE = new BigDecimal("20");
    private static final BigDecimal RATE_IMPORTATION = new BigDecimal("5");

    /**
     * Calcule la décomposition fiscale complète pour un produit.
     */
    public TaxBreakdown compute(Product product) {
        Money taxeTVA = computeTaxeTVA(product);
        Money taxeImportation = computeTaxeImportation(product);
        Money prixTTC = product.getPrixHT().add(taxeTVA).add(taxeImportation);

        return new TaxBreakdown(taxeTVA, taxeImportation, prixTTC);
    }

    private Money computeTaxeTVA(Product product) {
        BigDecimal rate = tvaRateFor(product.getType());
        return product.getPrixHT().multiplyByRate(rate).roundToNearestFiveCents();
    }

    private Money computeTaxeImportation(Product product) {
        if (!product.isImporte()) return Money.ZERO;
        return product.getPrixHT().multiplyByRate(RATE_IMPORTATION).roundToNearestFiveCents();
    }

    private BigDecimal tvaRateFor(ProductType type) {
        return switch (type) {
            case NOURRITURE, MEDICAMENTS -> RATE_TVA_ZERO;
            case LIVRES -> RATE_TVA_REDUITE;
            case AUTRES -> RATE_TVA_NORMALE;
        };
    }
}
