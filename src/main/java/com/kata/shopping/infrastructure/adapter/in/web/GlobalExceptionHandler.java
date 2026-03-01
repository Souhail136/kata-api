package com.kata.shopping.infrastructure.adapter.in.web;

import com.kata.shopping.domain.exception.CatalogUnavailableException;
import com.kata.shopping.domain.exception.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Gestionnaire centralisé des exceptions.
 * Produit des réponses conformes RFC 7807 (Problem Details for HTTP APIs).
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductNotFoundException.class)
    ProblemDetail handleProductNotFound(ProductNotFoundException ex) {
        log.warn("Produit introuvable : {}", ex.getProductId());
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setType(URI.create("/errors/product-not-found"));
        pd.setTitle("Produit introuvable");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(CatalogUnavailableException.class)
    ProblemDetail handleCatalogUnavailable(CatalogUnavailableException ex) {
        log.error("Service catalogue indisponible", ex);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        pd.setType(URI.create("/errors/catalog-unavailable"));
        pd.setTitle("Catalogue indisponible");
        pd.setDetail("Le service catalogue est temporairement indisponible. Veuillez réessayer.");
        return pd;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Argument invalide : {}", ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setType(URI.create("/errors/invalid-argument"));
        pd.setTitle("Requête invalide");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> "'%s' : %s".formatted(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        log.warn("Erreur de validation : {}", detail);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setType(URI.create("/errors/validation-failed"));
        pd.setTitle("Erreur de validation");
        pd.setDetail(detail);
        return pd;
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnexpected(Exception ex) {
        log.error("Erreur inattendue", ex);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setType(URI.create("/errors/internal-error"));
        pd.setTitle("Erreur interne");
        pd.setDetail("Une erreur inattendue s'est produite.");
        return pd;
    }
}
