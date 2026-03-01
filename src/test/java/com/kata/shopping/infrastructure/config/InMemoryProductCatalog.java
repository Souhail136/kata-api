package com.kata.shopping.infrastructure.config;

import com.kata.shopping.domain.model.Product;
import com.kata.shopping.domain.model.ProductType;
import com.kata.shopping.domain.model.valueobject.Money;
import com.kata.shopping.domain.model.valueobject.ProductId;
import com.kata.shopping.domain.repository.ProductCatalog;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implémentation in-memory du port {@link ProductCatalog} activée uniquement
 * sous le profil {@code test}.
 *
 * <p>Avantages :
 * <ul>
 *   <li>Aucun appel HTTP — les tests sont 100% autonomes et rapides</li>
 *   <li>Données de test déterministes et explicites</li>
 *   <li>Remplace {@code ProductCatalogAdapter} grâce à {@code @Primary}</li>
 * </ul>
 *
 * <p>Le catalogue reflète exactement le mock HTTP pour garantir la cohérence
 * des assertions dans {@code ShoppingApiIntegrationTest}.
 */
@Component
@Primary
@Profile("test")
class InMemoryProductCatalog implements ProductCatalog {

    private final Map<ProductId, Product> catalog;

    InMemoryProductCatalog() {
        List<Product> products = List.of(
                product(1, "Pommes Bio", false, ProductType.NOURRITURE, "1.50"),
                product(2, "Pain de campagne", false, ProductType.NOURRITURE, "2.80"),
                product(3, "Doliprane 1000mg", false, ProductType.MEDICAMENTS, "3.20"),
                product(4, "Vitamine C", true, ProductType.MEDICAMENTS, "5.00"),
                product(5, "Le Petit Chaperon rouge", false, ProductType.LIVRES, "8.90"),
                product(6, "Harry Potter", true, ProductType.LIVRES, "12.50"),
                product(7, "Casque audio", false, ProductType.AUTRES, "49.99"),
                product(8, "Parfum", true, ProductType.AUTRES, "35.00"),
                product(9, "Chocolat en poudre", true, ProductType.NOURRITURE, "4.50"),
                product(10, "Rame de papier", false, ProductType.AUTRES, "1.20"),
                product(11, "Huile d'olive", true, ProductType.NOURRITURE, "7.80"),
                product(12, "Dictionnaire Larousse", false, ProductType.LIVRES, "22.00")
        );

        this.catalog = products.stream()
                .collect(Collectors.toUnmodifiableMap(Product::getId, Function.identity()));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(catalog.values());
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return Optional.ofNullable(catalog.get(productId));
    }

    private static Product product(int id, String libelle, boolean importe,
                                   ProductType type, String prixHT) {
        return new Product(ProductId.of(id), libelle, importe, type, Money.of(prixHT));
    }
}
