package com.kata.shopping.domain.repository;

import com.kata.shopping.domain.model.Product;
import com.kata.shopping.domain.model.valueobject.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Port secondaire (output) : contrat d'accès au catalogue produit.
 * Le domaine définit cette interface ; l'infrastructure la satisfait.
 */
public interface ProductCatalog {

    List<Product> findAll();

    Optional<Product> findById(ProductId productId);
}
