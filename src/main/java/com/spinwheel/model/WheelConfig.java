package com.spinwheel.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

import java.util.ArrayList;
import java.util.List;

@Entity
public class WheelConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean riggedEnabled;

    private String riggedItemName;

    @OneToMany(mappedBy = "wheelConfig", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @OrderBy("position ASC")
    private List<WheelItem> items = new ArrayList<>();

    public WheelConfig() {
    }

    public WheelConfig(String name, boolean riggedEnabled, String riggedItemName) {
        this.name = name;
        this.riggedEnabled = riggedEnabled;
        this.riggedItemName = riggedItemName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRiggedEnabled() {
        return riggedEnabled;
    }

    public void setRiggedEnabled(boolean riggedEnabled) {
        this.riggedEnabled = riggedEnabled;
    }

    public String getRiggedItemName() {
        return riggedItemName;
    }

    public void setRiggedItemName(String riggedItemName) {
        this.riggedItemName = riggedItemName;
    }

    public List<WheelItem> getItems() {
        return items;
    }

    public void setItems(List<WheelItem> items) {
        this.items = items;
    }
}
