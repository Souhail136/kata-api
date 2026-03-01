package com.kata.shopping.infrastructure.adapter.in.web;

import com.kata.shopping.application.dto.CartCommand;
import com.kata.shopping.application.dto.InvoiceView;
import com.kata.shopping.application.port.in.ProcessCartUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/panier")
@Tag(name = "Panier", description = "Traitement du panier et génération de la facture TTC")
class CartController {

    private final ProcessCartUseCase processCartUseCase;

    CartController(ProcessCartUseCase processCartUseCase) {
        this.processCartUseCase = processCartUseCase;
    }

    @PostMapping("/facture")
    @Operation(summary = "Soumet un panier et retourne la facture détaillée avec décomposition des taxes")
    ResponseEntity<InvoiceView> calculateInvoice(@Valid @RequestBody WebDtos.CartRequest request) {
        CartCommand command = toCommand(request);
        return ResponseEntity.ok(processCartUseCase.process(command));
    }

    private CartCommand toCommand(WebDtos.CartRequest request) {
        List<CartCommand.CartItemCommand> items = request.items().stream()
                .map(i -> new CartCommand.CartItemCommand(i.productId(), i.quantity()))
                .toList();
        return new CartCommand(items);
    }
}
