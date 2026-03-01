package com.kata.shopping.infrastructure.adapter.out.catalog;

import com.kata.shopping.domain.model.Product;
import com.kata.shopping.domain.model.valueobject.ProductId;
import com.kata.shopping.domain.repository.ProductCatalog;
import com.kata.shopping.infrastructure.adapter.out.catalog.mapper.CatalogProductMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Adaptateur secondaire (output) : implémente le port domaine {@link ProductCatalog}.
 * Charge le catalogue une seule fois et construit un index par identifiant
 * pour éviter de parcourir la liste entière à chaque recherche unitaire.
 *
 * <p>Note : pour une production réelle, ce cache en mémoire serait remplacé par
 * un cache distribué (Redis) avec TTL configurable.
 */
@Component
public class ProductCatalogAdapter implements ProductCatalog {

    private final CatalogApiClient catalogApiClient;
    private final CatalogProductMapper mapper;

    // Cache lazy-loaded — chargé une seule fois au premier appel
    private volatile Map<ProductId, Product> catalogIndex;
    private final Object lock = new Object();

    public ProductCatalogAdapter(CatalogApiClient catalogApiClient, CatalogProductMapper mapper) {
        this.catalogApiClient = catalogApiClient;
        this.mapper = mapper;
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(getIndex().values());
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return Optional.ofNullable(getIndex().get(productId));
    }

    private Map<ProductId, Product> getIndex() {
        if (catalogIndex == null) {
            synchronized (lock) {
                if (catalogIndex == null) {
                    catalogIndex = catalogApiClient.fetchCatalog()
                            .stream()
                            .map(mapper::toDomain)
                            .collect(Collectors.toUnmodifiableMap(Product::getId, Function.identity()));
                }
            }
        }
        return catalogIndex;
    }
}
