package com.kata.shopping.application.usecase;

import com.kata.shopping.application.dto.ProductView;
import com.kata.shopping.application.port.in.GetProductsUseCase;
import com.kata.shopping.domain.model.Product;
import com.kata.shopping.domain.model.TaxBreakdown;
import com.kata.shopping.domain.model.valueobject.ProductId;
import com.kata.shopping.domain.repository.ProductCatalog;
import com.kata.shopping.domain.service.TaxPolicy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GetProductsService implements GetProductsUseCase {

    private final ProductCatalog productCatalog;
    private final TaxPolicy taxPolicy;

    public GetProductsService(ProductCatalog productCatalog, TaxPolicy taxPolicy) {
        this.productCatalog = productCatalog;
        this.taxPolicy = taxPolicy;
    }

    @Override
    public List<ProductView> getAllProducts() {
        return productCatalog.findAll().stream()
                .map(this::toView)
                .toList();
    }

    @Override
    public Optional<ProductView> getProductById(ProductId productId) {
        return productCatalog.findById(productId).map(this::toView);
    }

    private ProductView toView(Product product) {
        TaxBreakdown breakdown = taxPolicy.compute(product);
        return new ProductView(
                product.getId().value(),
                product.getLibelle(),
                product.isImporte(),
                product.getType(),
                product.getPrixHT().value(),
                breakdown.prixTTC().value()
        );
    }
}
