package com.spinwheel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spinwheel.model.WheelConfig;
import com.spinwheel.model.WheelItem;
import com.spinwheel.repository.WheelConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WheelConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WheelConfigRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void createWheel_returnsCreatedWheel() throws Exception {
        WheelConfig wheel = new WheelConfig("Test Wheel", false, null);
        addItem(wheel, "Alice", 0);
        addItem(wheel, "Bob", 1);

        mockMvc.perform(post("/api/wheels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wheel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Wheel"))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].name").value("Alice"))
                .andExpect(jsonPath("$.items[1].name").value("Bob"));
    }

    @Test
    void getAll_returnsAllWheels() throws Exception {
        createTestWheel("Wheel A", 3);
        createTestWheel("Wheel B", 2);

        mockMvc.perform(get("/api/wheels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Wheel A", "Wheel B")));
    }

    @Test
    void getById_existingWheel_returnsWheel() throws Exception {
        WheelConfig saved = createTestWheel("Find Me", 4);

        mockMvc.perform(get("/api/wheels/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Find Me"))
                .andExpect(jsonPath("$.items", hasSize(4)));
    }

    @Test
    void getById_nonExistent_returns404() throws Exception {
        mockMvc.perform(get("/api/wheels/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateWheel_changesNameAndItems() throws Exception {
        WheelConfig saved = createTestWheel("Original", 2);

        WheelConfig updated = new WheelConfig("Updated", false, null);
        addItem(updated, "X", 0);
        addItem(updated, "Y", 1);
        addItem(updated, "Z", 2);

        mockMvc.perform(put("/api/wheels/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name").value("X"));
    }

    @Test
    void updateWheel_nonExistent_returns404() throws Exception {
        WheelConfig updated = new WheelConfig("Ghost", false, null);

        mockMvc.perform(put("/api/wheels/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWheel_removesWheel() throws Exception {
        WheelConfig saved = createTestWheel("Delete Me", 2);

        mockMvc.perform(delete("/api/wheels/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/wheels/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWheel_riggedFieldsPersist() throws Exception {
        WheelConfig wheel = new WheelConfig("Rigged Wheel", true, "Winner");
        addItem(wheel, "Winner", 0);
        addItem(wheel, "Loser", 1);

        String response = mockMvc.perform(post("/api/wheels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wheel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riggedEnabled").value(true))
                .andExpect(jsonPath("$.riggedItemName").value("Winner"))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/wheels/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riggedEnabled").value(true))
                .andExpect(jsonPath("$.riggedItemName").value("Winner"));
    }

    @Test
    void updateWheel_riggedFieldsUpdate() throws Exception {
        WheelConfig saved = createTestWheel("Not Rigged", 3);

        WheelConfig updated = new WheelConfig("Now Rigged", true, "Item 1");
        addItem(updated, "Item 0", 0);
        addItem(updated, "Item 1", 1);
        addItem(updated, "Item 2", 2);

        mockMvc.perform(put("/api/wheels/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.riggedEnabled").value(true))
                .andExpect(jsonPath("$.riggedItemName").value("Item 1"));
    }

    @Test
    void itemsReturnedInPositionOrder() throws Exception {
        WheelConfig wheel = new WheelConfig("Ordered", false, null);
        addItem(wheel, "Third", 2);
        addItem(wheel, "First", 0);
        addItem(wheel, "Second", 1);

        String response = mockMvc.perform(post("/api/wheels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wheel)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/wheels/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].name").value("First"))
                .andExpect(jsonPath("$.items[1].name").value("Second"))
                .andExpect(jsonPath("$.items[2].name").value("Third"));
    }

    @Test
    void createWheel_emptyItems_succeeds() throws Exception {
        WheelConfig wheel = new WheelConfig("Empty Wheel", false, null);

        mockMvc.perform(post("/api/wheels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wheel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    private WheelConfig createTestWheel(String name, int itemCount) {
        WheelConfig wheel = new WheelConfig(name, false, null);
        for (int i = 0; i < itemCount; i++) {
            WheelItem item = new WheelItem("Item " + i, i);
            item.setWheelConfig(wheel);
            wheel.getItems().add(item);
        }
        return repository.save(wheel);
    }

    private void addItem(WheelConfig wheel, String name, int position) {
        WheelItem item = new WheelItem(name, position);
        wheel.getItems().add(item);
    }
}
