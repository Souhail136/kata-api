package com.kata.shopping.application.port.in;

import com.kata.shopping.application.dto.ProductView;
import com.kata.shopping.domain.model.valueobject.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * Port primaire (input) : cas d'usage de consultation du catalogue.
 */
public interface GetProductsUseCase {

    List<ProductView> getAllProducts();

    Optional<ProductView> getProductById(ProductId productId);
}
