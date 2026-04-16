package com.spinwheel.controller;

import com.spinwheel.model.WheelConfig;
import com.spinwheel.repository.WheelConfigRepository;
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
@RequestMapping("/api/wheels")
@CrossOrigin
public class WheelConfigController {

    private final WheelConfigRepository repository;

    public WheelConfigController(WheelConfigRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<WheelConfig> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WheelConfig> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public WheelConfig create(@RequestBody WheelConfig config) {
        if (config.getItems() != null) {
            config.getItems().forEach(item -> item.setWheelConfig(config));
        }
        return repository.save(config);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WheelConfig> update(@PathVariable Long id, @RequestBody WheelConfig updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setRiggedEnabled(updated.isRiggedEnabled());
                    existing.setRiggedItemName(updated.getRiggedItemName());
                    existing.getItems().clear();
                    if (updated.getItems() != null) {
                        updated.getItems().forEach(item -> {
                            item.setWheelConfig(existing);
                            existing.getItems().add(item);
                        });
                    }
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
