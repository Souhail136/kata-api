package com.kata.shopping.infrastructure.adapter.in.web;

import com.kata.shopping.application.dto.ProductView;
import com.kata.shopping.application.port.in.GetProductsUseCase;
import com.kata.shopping.domain.model.valueobject.ProductId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produits")
@Tag(name = "Produits", description = "Consultation du catalogue produits avec prix TTC")
class ProductController {

    private final GetProductsUseCase getProductsUseCase;

    ProductController(GetProductsUseCase getProductsUseCase) {
        this.getProductsUseCase = getProductsUseCase;
    }

    @GetMapping
    @Operation(summary = "Liste l'ensemble des produits du catalogue avec leur prix TTC calculé")
    ResponseEntity<List<ProductView>> getAllProducts() {
        return ResponseEntity.ok(getProductsUseCase.getAllProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupère un produit par son identifiant avec son prix TTC calculé")
    ResponseEntity<ProductView> getProductById(
            @Parameter(description = "Identifiant du produit") @PathVariable int id) {
        return getProductsUseCase.getProductById(ProductId.of(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
