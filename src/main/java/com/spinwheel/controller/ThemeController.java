package com.spinwheel.controller;

import com.spinwheel.model.Theme;
import com.spinwheel.repository.ThemeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@CrossOrigin
public class ThemeController {

    private final ThemeRepository repository;

    public ThemeController(ThemeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Theme> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theme> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Theme create(@RequestBody Theme theme) {
        return repository.save(theme);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theme> update(@PathVariable Long id, @RequestBody Theme updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setSliceColors(updated.getSliceColors());
                    existing.setBackgroundStart(updated.getBackgroundStart());
                    existing.setBackgroundEnd(updated.getBackgroundEnd());
                    existing.setAccentColor(updated.getAccentColor());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
