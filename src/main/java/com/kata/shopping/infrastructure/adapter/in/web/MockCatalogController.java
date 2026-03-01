package com.kata.shopping.infrastructure.adapter.in.web;

import com.kata.shopping.domain.model.ProductType;
import com.kata.shopping.infrastructure.adapter.out.catalog.CatalogProductDto;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Simule l'API externe du catalogue produit.
 * En production, cet endpoint serait remplacé par le vrai service externe
 * et cette classe supprimée du projet.
 */
@Hidden
@RestController
@RequestMapping("/mock/catalogue")
class MockCatalogController {

    @GetMapping("/produits")
    List<CatalogProductDto> getCatalog() {
        return List.of(
                new CatalogProductDto(1, "Pommes Bio", false, ProductType.NOURRITURE, new BigDecimal("1.50")),
                new CatalogProductDto(2, "Pain de campagne", false, ProductType.NOURRITURE, new BigDecimal("2.80")),
                new CatalogProductDto(3, "Doliprane 1000mg", false, ProductType.MEDICAMENTS, new BigDecimal("3.20")),
                new CatalogProductDto(4, "Vitamine C", true, ProductType.MEDICAMENTS, new BigDecimal("5.00")),
                new CatalogProductDto(5, "Le Petit Chaperon rouge", false, ProductType.LIVRES, new BigDecimal("8.90")),
                new CatalogProductDto(6, "Harry Potter", true, ProductType.LIVRES, new BigDecimal("12.50")),
                new CatalogProductDto(7, "Casque audio", false, ProductType.AUTRES, new BigDecimal("49.99")),
                new CatalogProductDto(8, "Parfum", true, ProductType.AUTRES, new BigDecimal("35.00")),
                new CatalogProductDto(9, "Chocolat en poudre", true, ProductType.NOURRITURE, new BigDecimal("4.50")),
                new CatalogProductDto(10, "Rame de apier", false, ProductType.AUTRES, new BigDecimal("1.20")),
                new CatalogProductDto(11, "Huile d'olive", true, ProductType.NOURRITURE, new BigDecimal("7.80")),
                new CatalogProductDto(12, "Dictionnaire Larousse", false, ProductType.LIVRES, new BigDecimal("22.00"))
        );
    }
}
