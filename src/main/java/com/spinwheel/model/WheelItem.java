package com.spinwheel.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class WheelItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int position;

    @ManyToOne
    @JsonBackReference
    private WheelConfig wheelConfig;

    public WheelItem() {
    }

    public WheelItem(String name, int position) {
        this.name = name;
        this.position = position;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public WheelConfig getWheelConfig() {
        return wheelConfig;
    }

    public void setWheelConfig(WheelConfig wheelConfig) {
        this.wheelConfig = wheelConfig;
    }
}
