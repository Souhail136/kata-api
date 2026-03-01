package com.kata.shopping.domain.service;

import com.kata.shopping.domain.exception.ProductNotFoundException;
import com.kata.shopping.domain.model.*;
import com.kata.shopping.domain.model.valueobject.Money;
import com.kata.shopping.domain.model.valueobject.ProductId;
import com.kata.shopping.domain.model.valueobject.Quantity;
import com.kata.shopping.domain.repository.ProductCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private ProductCatalog productCatalog;

    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        invoiceService = new InvoiceService(productCatalog, new TaxPolicy());
    }

    @Test
    void shouldGenerateInvoiceWithCorrectTotals() {
        // Livre, prixHT=12.50 | TVA 10% = 1.25 | PrixTTC = 13.75
        // Quantité 2 → totalTTC = 27.50, totalHT = 25.00, totalTaxes = 2.50
        Product livre = livre("12.50");
        when(productCatalog.findById(ProductId.of(1))).thenReturn(Optional.of(livre));

        Cart cart = Cart.of(List.of(new CartItem(ProductId.of(1), Quantity.of(2))));
        Invoice invoice = invoiceService.generateInvoice(cart);

        assertThat(invoice.getTotalHT()).isEqualTo(Money.of("25.00"));
        assertThat(invoice.getTotalTTC()).isEqualTo(Money.of("27.50"));
        assertThat(invoice.getTotalTaxes()).isEqualTo(Money.of("2.50"));
    }

    @Test
    void shouldGenerateMultiLineInvoice() {
        Product livre = livre("12.50");
        Product nourriture = nourriture("1.50");

        when(productCatalog.findById(ProductId.of(1))).thenReturn(Optional.of(livre));
        when(productCatalog.findById(ProductId.of(2))).thenReturn(Optional.of(nourriture));

        Cart cart = Cart.of(List.of(
                new CartItem(ProductId.of(1), Quantity.of(1)),
                new CartItem(ProductId.of(2), Quantity.of(3))
        ));

        Invoice invoice = invoiceService.generateInvoice(cart);

        assertThat(invoice.getLines()).hasSize(2);
        // nourriture : TVA 0%, prixTTC = 1.50 → totalTTC = 4.50
        // livre : prixTTC = 13.75 → totalTTC = 13.75
        assertThat(invoice.getTotalTTC()).isEqualTo(Money.of("18.25"));
    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenProductMissing() {
        when(productCatalog.findById(ProductId.of(99))).thenReturn(Optional.empty());

        Cart cart = Cart.of(List.of(new CartItem(ProductId.of(99), Quantity.of(1))));

        assertThatThrownBy(() -> invoiceService.generateInvoice(cart))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void cartInvariant_shouldRejectEmptyCart() {
        assertThatThrownBy(() -> Cart.of(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vide");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Product livre(String prixHT) {
        return new Product(ProductId.of(1), "Livre test", false, ProductType.LIVRES, Money.of(prixHT));
    }

    private Product nourriture(String prixHT) {
        return new Product(ProductId.of(2), "Nourriture test", false, ProductType.NOURRITURE, Money.of(prixHT));
    }
}
