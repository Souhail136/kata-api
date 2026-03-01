package com.kata.shopping.domain.service;

import com.kata.shopping.domain.exception.ProductNotFoundException;
import com.kata.shopping.domain.model.*;
import com.kata.shopping.domain.repository.ProductCatalog;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service domaine responsable de la génération d'une facture à partir d'un panier.
 * Orchestre la résolution des produits et le calcul des taxes, sans connaissance
 * des détails techniques (HTTP, persistence, etc.).
 */
@Service
public class InvoiceService {

    private final ProductCatalog productCatalog;
    private final TaxPolicy taxPolicy;

    public InvoiceService(ProductCatalog productCatalog, TaxPolicy taxPolicy) {
        this.productCatalog = productCatalog;
        this.taxPolicy = taxPolicy;
    }

    public Invoice generateInvoice(Cart cart) {
        List<InvoiceLine> lines = cart.getItems().stream()
                .map(this::resolveInvoiceLine)
                .toList();

        return Invoice.of(lines);
    }

    private InvoiceLine resolveInvoiceLine(CartItem item) {
        Product product = productCatalog.findById(item.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(item.getProductId()));

        TaxBreakdown breakdown = taxPolicy.compute(product);
        return new InvoiceLine(product, item.getQuantity(), breakdown);
    }
}
