package com.spinwheel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spinwheel.model.Theme;
import com.spinwheel.repository.ThemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ThemeRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void createTheme_returnsCreatedTheme() throws Exception {
        Theme theme = new Theme("Test Theme",
                Arrays.asList("#FF0000", "#00FF00", "#0000FF"),
                "#111111", "#222222", "#333333");

        mockMvc.perform(post("/api/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(theme)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Theme"))
                .andExpect(jsonPath("$.sliceColors", hasSize(3)))
                .andExpect(jsonPath("$.backgroundStart").value("#111111"))
                .andExpect(jsonPath("$.backgroundEnd").value("#222222"))
                .andExpect(jsonPath("$.accentColor").value("#333333"));
    }

    @Test
    void getAll_returnsAllThemes() throws Exception {
        createTestTheme("Theme A");
        createTestTheme("Theme B");

        mockMvc.perform(get("/api/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getById_existingTheme_returnsTheme() throws Exception {
        Theme saved = createTestTheme("Find Me");

        mockMvc.perform(get("/api/themes/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Find Me"));
    }

    @Test
    void getById_nonExistent_returns404() throws Exception {
        mockMvc.perform(get("/api/themes/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTheme_changesFields() throws Exception {
        Theme saved = createTestTheme("Original");

        Theme updated = new Theme("Updated",
                Arrays.asList("#AAAAAA", "#BBBBBB"),
                "#000000", "#FFFFFF", "#FF0000");

        mockMvc.perform(put("/api/themes/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.sliceColors", hasSize(2)))
                .andExpect(jsonPath("$.accentColor").value("#FF0000"));
    }

    @Test
    void deleteTheme_removesTheme() throws Exception {
        Theme saved = createTestTheme("Delete Me");

        mockMvc.perform(delete("/api/themes/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/themes/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void sliceColorsPreserveOrder() throws Exception {
        List<String> colors = Arrays.asList("#111", "#222", "#333", "#444", "#555");
        Theme theme = new Theme("Ordered", colors, "#000", "#FFF", "#AAA");

        String response = mockMvc.perform(post("/api/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(theme)))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/themes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sliceColors[0]").value("#111"))
                .andExpect(jsonPath("$.sliceColors[1]").value("#222"))
                .andExpect(jsonPath("$.sliceColors[2]").value("#333"))
                .andExpect(jsonPath("$.sliceColors[3]").value("#444"))
                .andExpect(jsonPath("$.sliceColors[4]").value("#555"));
    }

    private Theme createTestTheme(String name) {
        return repository.save(new Theme(name,
                Arrays.asList("#FF0000", "#00FF00", "#0000FF"),
                "#111111", "#222222", "#333333"));
    }
}
