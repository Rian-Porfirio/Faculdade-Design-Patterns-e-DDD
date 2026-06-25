package com.streaming.interfaces;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("TransactionController (integration, end-to-end antifraud)")
class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private String charge(String cardId, String amount, String merchant) {
        return "{\"cardId\":" + cardId + ",\"amount\":" + amount
                + ",\"currency\":\"BRL\",\"merchant\":\"" + merchant + "\"}";
    }

    @Test
    @DisplayName("authorizes a charge on an active card")
    void authorizesActiveCard() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(charge("1", "19.90", "STREAMING")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("AUTHORIZED")))
                .andExpect(jsonPath("$.cardId", is(1)))
                .andExpect(jsonPath("$.currency", is("BRL")));
    }

    @Test
    @DisplayName("rejects a charge on an inactive card with inactive-card")
    void rejectsInactiveCard() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(charge("2", "19.90", "STREAMING")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Business Rule Violation")))
                .andExpect(jsonPath("$.message", is("inactive-card")))
                .andExpect(jsonPath("$.path", is("/transactions")));
    }

    @Test
    @DisplayName("rejects the third identical charge with doubled-transaction")
    void rejectsDoubledTransaction() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(charge("1", "9.90", "DOUBLE")))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(charge("1", "9.90", "DOUBLE")))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(charge("1", "9.90", "DOUBLE")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("doubled-transaction")));
    }

    @Test
    @DisplayName("rejects more than three quick charges with high-frequency-small-interval")
    void rejectsHighFrequency() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(charge("1", "1.00", "FREQ"))).andExpect(status().isCreated());
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(charge("1", "2.00", "FREQ"))).andExpect(status().isCreated());
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(charge("1", "3.00", "FREQ"))).andExpect(status().isCreated());
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(charge("1", "4.00", "FREQ")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("high-frequency-small-interval")));
    }

    @Test
    @DisplayName("returns 404 when charging a missing card")
    void rejectsMissingCard() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(charge("999", "19.90", "STREAMING")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Resource Not Found")));
    }
}
