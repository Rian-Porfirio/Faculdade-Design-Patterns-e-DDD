package com.streaming.interfaces;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Catalog, Cards and Subscription flows (integration)")
class CatalogAndSubscriptionIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("registers and lists songs")
    void songLifecycle() throws Exception {
        mockMvc.perform(post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Yesterday\",\"artist\":\"The Beatles\","
                                + "\"album\":\"Help!\",\"durationSeconds\":125}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Yesterday")));

        mockMvc.perform(get("/songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(6))));
    }

    @Test
    @DisplayName("creates a playlist and manages its songs")
    void playlistLifecycle() throws Exception {
        String location = mockMvc.perform(post("/playlists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"name\":\"Focus\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Focus")))
                .andReturn().getResponse().getContentAsString();

        Integer id = com.jayway.jsonpath.JsonPath.read(location, "$.id");

        mockMvc.perform(post("/playlists/" + id + "/songs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songIds", is(java.util.List.of(3))));

        mockMvc.perform(delete("/playlists/" + id + "/songs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songIds", hasSize(0)));

        mockMvc.perform(get("/playlists/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)));
    }

    @Test
    @DisplayName("registers a card and toggles its status")
    void cardLifecycle() throws Exception {
        String body = mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"4111111111111111\",\"holder\":\"Carol\","
                                + "\"expiration\":\"10/2031\",\"cvv\":\"321\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.number", is("**** **** **** 1111")))
                .andReturn().getResponse().getContentAsString();

        Integer id = com.jayway.jsonpath.JsonPath.read(body, "$.id");

        mockMvc.perform(patch("/cards/" + id + "/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("INACTIVE")));

        mockMvc.perform(patch("/cards/" + id + "/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    @DisplayName("subscribes a user to a plan, charging a valid card")
    void subscribeChargesCard() throws Exception {
        String body = mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":2,\"planId\":3,\"cardId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.userId", is(2)))
                .andExpect(jsonPath("$.planId", is(3)))
                .andReturn().getResponse().getContentAsString();

        Integer id = com.jayway.jsonpath.JsonPath.read(body, "$.id");

        mockMvc.perform(get("/subscriptions/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planId", is(3)));
    }

    @Test
    @DisplayName("rejects re-subscribing to the same active plan")
    void rejectsSameActivePlan() throws Exception {
        mockMvc.perform(post("/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"planId\":2,\"cardId\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("active-subscription-already-exists")));
    }

    @Test
    @DisplayName("fetches a seeded transaction by id")
    void fetchesSeededTransaction() throws Exception {
        mockMvc.perform(get("/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("AUTHORIZED")))
                .andExpect(jsonPath("$.merchant", is("STREAMING-SUBSCRIPTION")));
    }
}
