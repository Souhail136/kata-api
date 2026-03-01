package com.kata.shopping.domain.service;

import com.kata.shopping.domain.model.Product;
import com.kata.shopping.domain.model.ProductType;
import com.kata.shopping.domain.model.TaxBreakdown;
import com.kata.shopping.domain.model.valueobject.Money;
import com.kata.shopping.domain.model.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaxPolicyTest {

    private TaxPolicy taxPolicy;

    @BeforeEach
    void setUp() {
        taxPolicy = new TaxPolicy();
    }

    // -------------------------------------------------------------------------
    // TVA
    // -------------------------------------------------------------------------

    @Nested
    class TVARules {

        @Test
        void nourritureNonImportee_shouldHaveZeroTVA() {
            Product p = product(ProductType.NOURRITURE, "1.50", false);
            assertThat(taxPolicy.compute(p).taxeTVA()).isEqualTo(Money.ZERO);
        }

        @Test
        void medicamentNonImporte_shouldHaveZeroTVA() {
            Product p = product(ProductType.MEDICAMENTS, "3.20", false);
            assertThat(taxPolicy.compute(p).taxeTVA()).isEqualTo(Money.ZERO);
        }

        @Test
        void livre_shouldApply10PercentTVARounded() {
            // prixHT=8.90 | TVA 10% = 0.89 → arrondi → 0.90
            Product p = product(ProductType.LIVRES, "8.90", false);
            assertThat(taxPolicy.compute(p).taxeTVA()).isEqualTo(Money.of("0.90"));
        }

        @Test
        void autre_shouldApply20PercentTVARounded() {
            // prixHT=49.99 | TVA 20% = 9.998 → arrondi → 10.00
            Product p = product(ProductType.AUTRES, "49.99", false);
            assertThat(taxPolicy.compute(p).taxeTVA()).isEqualTo(Money.of("10.00"));
        }
    }

    // -------------------------------------------------------------------------
    // Taxe d'importation
    // -------------------------------------------------------------------------

    @Nested
    class ImportationTaxRules {

        @Test
        void nonImporte_shouldHaveZeroImportTax() {
            Product p = product(ProductType.AUTRES, "35.00", false);
            assertThat(taxPolicy.compute(p).taxeImportation()).isEqualTo(Money.ZERO);
        }

        @Test
        void importe_shouldApply5PercentRounded() {
            // prixHT=35.00 | import 5% = 1.75 → arrondi → 1.75
            Product p = product(ProductType.AUTRES, "35.00", true);
            assertThat(taxPolicy.compute(p).taxeImportation()).isEqualTo(Money.of("1.75"));
        }

        @Test
        void nourritureImportee_shouldApplyImportTaxDespiteZeroTVA() {
            // La nourriture importée est exemptée de TVA mais PAS de taxe import
            // prixHT=4.50 | import 5% = 0.225 → arrondi → 0.25
            Product p = product(ProductType.NOURRITURE, "4.50", true);
            TaxBreakdown breakdown = taxPolicy.compute(p);
            assertThat(breakdown.taxeTVA()).isEqualTo(Money.ZERO);
            assertThat(breakdown.taxeImportation()).isEqualTo(Money.of("0.25"));
        }

        @Test
        void medicamentImporte_shouldApplyImportTaxDespiteZeroTVA() {
            // prixHT=5.00 | import 5% = 0.25 → arrondi → 0.25
            Product p = product(ProductType.MEDICAMENTS, "5.00", true);
            TaxBreakdown breakdown = taxPolicy.compute(p);
            assertThat(breakdown.taxeTVA()).isEqualTo(Money.ZERO);
            assertThat(breakdown.taxeImportation()).isEqualTo(Money.of("0.25"));
        }
    }

    // -------------------------------------------------------------------------
    // Prix TTC complet
    // -------------------------------------------------------------------------

    @Nested
    class PrixTTCCalculation {

        @Test
        void livreImporte_shouldCombineTVAAndImportTax() {
            // prixHT=12.50 | TVA 10% = 1.25 | import 5% = 0.625 → arrondi → 0.65
            // PrixTTC = 12.50 + 1.25 + 0.65 = 14.40
            Product p = product(ProductType.LIVRES, "12.50", true);
            assertThat(taxPolicy.compute(p).prixTTC()).isEqualTo(Money.of("14.40"));
        }

        @Test
        void autreNonImporte_shouldApplyOnlyTVA() {
            // prixHT=1.20 | TVA 20% = 0.24 → arrondi → 0.25
            // PrixTTC = 1.20 + 0.25 = 1.45
            Product p = product(ProductType.AUTRES, "1.20", false);
            assertThat(taxPolicy.compute(p).prixTTC()).isEqualTo(Money.of("1.45"));
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Product product(ProductType type, String prixHT, boolean importe) {
        return new Product(ProductId.of(1), "Test", importe, type, Money.of(prixHT));
    }
}
