package com.kata.shopping.infrastructure.adapter.out.catalog.mapper;

import com.kata.shopping.domain.model.Product;
import com.kata.shopping.domain.model.valueobject.Money;
import com.kata.shopping.domain.model.valueobject.ProductId;
import com.kata.shopping.infrastructure.adapter.out.catalog.CatalogProductDto;
import org.springframework.stereotype.Component;

/**
 * Mapper infrastructure → domaine pour les produits du catalogue externe.
 */
@Component
public class CatalogProductMapper {

    public Product toDomain(CatalogProductDto dto) {
        return new Product(
                ProductId.of(dto.id()),
                dto.libelle(),
                dto.importation(),
                dto.type(),
                Money.of(dto.prixHT())
        );
    }
}
