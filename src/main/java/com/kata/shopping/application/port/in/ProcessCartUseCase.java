package com.kata.shopping.application.port.in;

import com.kata.shopping.application.dto.CartCommand;
import com.kata.shopping.application.dto.InvoiceView;

/**
 * Port primaire (input) : cas d'usage de traitement d'un panier.
 */
public interface ProcessCartUseCase {

    InvoiceView process(CartCommand command);
}
