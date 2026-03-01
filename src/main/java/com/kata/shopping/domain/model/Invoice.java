package com.kata.shopping.domain.model;

import com.kata.shopping.domain.model.valueobject.Money;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Agrégat Facture. Calcule ses totaux à partir de ses lignes.
 * Immuable — construit une seule fois par {@link com.kata.shopping.domain.service.InvoiceService}.
 */
public final class Invoice {

    private final List<InvoiceLine> lines;
    private final Money totalHT;
    private final Money totalTaxes;
    private final Money totalTTC;

    private Invoice(List<InvoiceLine> lines) {
        this.lines = Collections.unmodifiableList(lines);
        this.totalHT = lines.stream().map(InvoiceLine::totalHT).reduce(Money.ZERO, Money::add);
        this.totalTaxes = lines.stream().map(InvoiceLine::totalTaxes).reduce(Money.ZERO, Money::add);
        this.totalTTC = lines.stream().map(InvoiceLine::totalTTC).reduce(Money.ZERO, Money::add);
    }

    public static Invoice of(List<InvoiceLine> lines) {
        Objects.requireNonNull(lines, "Les lignes de facture sont obligatoires");
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Une facture doit contenir au moins une ligne");
        }
        return new Invoice(lines);
    }

    public List<InvoiceLine> getLines() { return lines; }
    public Money getTotalHT() { return totalHT; }
    public Money getTotalTaxes() { return totalTaxes; }
    public Money getTotalTTC() { return totalTTC; }
}
