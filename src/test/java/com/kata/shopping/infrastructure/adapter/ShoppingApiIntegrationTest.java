package com.kata.shopping.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ShoppingApiIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    // -------------------------------------------------------------------------
    // GET /api/v1/produits
    // -------------------------------------------------------------------------

    @Test
    void getAllProducts_shouldReturn12Products() throws Exception {
        mockMvc.perform(get("/api/v1/produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(12))
                .andExpect(jsonPath("$[0].prixTTC").exists());
    }

    @Test
    void getProductById_shouldReturnProductWithTTC() throws Exception {
        mockMvc.perform(get("/api/v1/produits/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.libelle").value("Le Petit Chaperon rouge"))
                // prixHT=8.90 | TVA 10% = 0.90 | prixTTC = 9.80
                .andExpect(jsonPath("$.prixTTC").value(9.80));
    }

    @Test
    void getProductById_unknownId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/produits/9999"))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // POST /api/v1/panier/facture
    // -------------------------------------------------------------------------

    @Test
    void calculateInvoice_shouldReturnCorrectTotals() throws Exception {
        // Produit 5 : Le Petit Prince, livre, 8.90 HT → TVA 10% = 0.90 → TTC 9.80
        // Produit 1 : Pommes Bio, nourriture, 1.50 HT → TVA 0% → TTC 1.50 × 2 = 3.00
        String body = """
                {
                  "items": [
                    { "productId": 5, "quantity": 1 },
                    { "productId": 1, "quantity": 2 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/panier/facture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lignes.length()").value(2))
                .andExpect(jsonPath("$.totalHT").value(11.90))
                .andExpect(jsonPath("$.totalTTC").value(12.80))
                .andExpect(jsonPath("$.totalTaxes").value(0.90));
    }

    @Test
    void calculateInvoice_importedOther_shouldCombineTVAAndImportTax() throws Exception {
        // Produit 8 : Parfum importé, autre, 35.00 HT
        // TVA 20% = 7.00 | import 5% = 1.75 | TTC = 43.75
        String body = """
                {
                  "items": [
                    { "productId": 8, "quantity": 1 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/panier/facture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lignes[0].taxeTVA").value(7.00))
                .andExpect(jsonPath("$.lignes[0].taxeImportation").value(1.75))
                .andExpect(jsonPath("$.lignes[0].prixUnitaireTTC").value(43.75));
    }

    @Test
    void calculateInvoice_emptyCart_shouldReturn422() throws Exception {
        String body = """
                { "items": [] }
                """;

        mockMvc.perform(post("/api/v1/panier/facture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void calculateInvoice_unknownProduct_shouldReturn404() throws Exception {
        String body = """
                {
                  "items": [
                    { "productId": 9999, "quantity": 1 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/panier/facture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void calculateInvoice_invalidQuantity_shouldReturn422() throws Exception {
        String body = """
                {
                  "items": [
                    { "productId": 1, "quantity": -1 }
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/panier/facture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnprocessableEntity());
    }
}
