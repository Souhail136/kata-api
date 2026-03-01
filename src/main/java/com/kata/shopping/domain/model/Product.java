package com.kata.shopping.domain.model;

import com.kata.shopping.domain.model.valueobject.Money;
import com.kata.shopping.domain.model.valueobject.ProductId;

import java.util.Objects;

/**
 * Agrégat racine représentant un produit du catalogue.
 * Contient uniquement les informations de description — le calcul du TTC
 * est délégué au service domaine {@link com.kata.shopping.domain.service.TaxPolicy}.
 */
public final class Product {

    private final ProductId id;
    private final String libelle;
    private final boolean importe;
    private final ProductType type;
    private final Money prixHT;

    public Product(ProductId id, String libelle, boolean importe, ProductType type, Money prixHT) {
        this.id = Objects.requireNonNull(id, "L'identifiant produit est obligatoire");
        this.libelle = validateLibelle(libelle);
        this.type = Objects.requireNonNull(type, "Le type de produit est obligatoire");
        this.prixHT = Objects.requireNonNull(prixHT, "Le prix HT est obligatoire");
        this.importe = importe;
    }

    private static String validateLibelle(String libelle) {
        if (libelle == null || libelle.isBlank()) {
            throw new IllegalArgumentException("Le libellé du produit ne peut pas être vide");
        }
        return libelle.trim();
    }

    public ProductId getId() { return id; }
    public String getLibelle() { return libelle; }
    public boolean isImporte() { return importe; }
    public ProductType getType() { return type; }
    public Money getPrixHT() { return prixHT; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{id=%s, libelle='%s', type=%s, importe=%s, prixHT=%s}"
                .formatted(id.value(), libelle, type, importe, prixHT);
    }
}
