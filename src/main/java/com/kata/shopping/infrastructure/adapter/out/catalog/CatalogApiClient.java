package com.kata.shopping.infrastructure.adapter.out.catalog;

import com.kata.shopping.domain.exception.CatalogUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Client HTTP bas niveau vers l'API catalogue externe.
 * Responsabilité unique : exécuter l'appel HTTP et retourner les DTOs bruts.
 * La gestion des erreurs réseau est encapsulée ici.
 */
@Component
public class CatalogApiClient {

    private static final Logger log = LoggerFactory.getLogger(CatalogApiClient.class);

    private static final ParameterizedTypeReference<List<CatalogProductDto>> PRODUCT_LIST_TYPE =
            new ParameterizedTypeReference<>() {};

    private final RestTemplate restTemplate;
    private final String catalogUrl;

    public CatalogApiClient(RestTemplate restTemplate,
                            @Value("${catalog.api.url}") String catalogUrl) {
        this.restTemplate = restTemplate;
        this.catalogUrl = catalogUrl;
    }

    public List<CatalogProductDto> fetchCatalog() {
        log.debug("Appel API catalogue : GET {}", catalogUrl);
        try {
            var response = restTemplate.exchange(catalogUrl, HttpMethod.GET, null, PRODUCT_LIST_TYPE);
            List<CatalogProductDto> body = response.getBody();
            log.debug("Catalogue reçu : {} produits", body != null ? body.size() : 0);
            return body != null ? body : List.of();
        } catch (RestClientException ex) {
            throw new CatalogUnavailableException(
                    "Impossible de joindre l'API catalogue : " + catalogUrl, ex);
        }
    }
}
