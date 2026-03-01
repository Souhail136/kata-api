package com.kata.shopping.application.usecase;

import com.kata.shopping.application.dto.CartCommand;
import com.kata.shopping.application.dto.InvoiceView;
import com.kata.shopping.application.port.in.ProcessCartUseCase;
import com.kata.shopping.domain.model.*;
import com.kata.shopping.domain.model.valueobject.ProductId;
import com.kata.shopping.domain.model.valueobject.Quantity;
import com.kata.shopping.domain.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessCartService implements ProcessCartUseCase {

    private final InvoiceService invoiceService;

    public ProcessCartService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public InvoiceView process(CartCommand command) {
        Cart cart = toCart(command);
        Invoice invoice = invoiceService.generateInvoice(cart);
        return toView(invoice);
    }

    private Cart toCart(CartCommand command) {
        List<CartItem> items = command.items().stream()
                .map(i -> new CartItem(ProductId.of(i.productId()), Quantity.of(i.quantity())))
                .toList();
        return Cart.of(items);
    }

    private InvoiceView toView(Invoice invoice) {
        List<InvoiceView.InvoiceLineView> lignes = invoice.getLines().stream()
                .map(this::toLineView)
                .toList();

        return new InvoiceView(
                lignes,
                invoice.getTotalHT().value(),
                invoice.getTotalTaxes().value(),
                invoice.getTotalTTC().value()
        );
    }

    private InvoiceView.InvoiceLineView toLineView(InvoiceLine line) {
        return new InvoiceView.InvoiceLineView(
                line.product().getId().value(),
                line.product().getLibelle(),
                line.quantity().value(),
                line.product().getPrixHT().value(),
                line.taxBreakdown().taxeTVA().value(),
                line.taxBreakdown().taxeImportation().value(),
                line.taxBreakdown().prixTTC().value(),
                line.totalTTC().value()
        );
    }
}
