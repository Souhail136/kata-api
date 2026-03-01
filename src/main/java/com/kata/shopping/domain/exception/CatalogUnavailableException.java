package com.kata.shopping.domain.exception;

/**
 * Exception levée lorsque le service de catalogue externe est inaccessible.
 */
public class CatalogUnavailableException extends RuntimeException {

    public CatalogUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
