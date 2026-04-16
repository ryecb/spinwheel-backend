package com.spinwheel.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OrderColumn;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn
    @jakarta.persistence.CollectionTable(name = "theme_slice_colors")
    private List<String> sliceColors = new ArrayList<>();

    private String backgroundStart;

    private String backgroundEnd;

    private String accentColor;

    public Theme() {
    }

    public Theme(String name, List<String> sliceColors, String backgroundStart, String backgroundEnd, String accentColor) {
        this.name = name;
        this.sliceColors = sliceColors;
        this.backgroundStart = backgroundStart;
        this.backgroundEnd = backgroundEnd;
        this.accentColor = accentColor;
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

    public List<String> getSliceColors() {
        return sliceColors;
    }

    public void setSliceColors(List<String> sliceColors) {
        this.sliceColors = sliceColors;
    }

    public String getBackgroundStart() {
        return backgroundStart;
    }

    public void setBackgroundStart(String backgroundStart) {
        this.backgroundStart = backgroundStart;
    }

    public String getBackgroundEnd() {
        return backgroundEnd;
    }

    public void setBackgroundEnd(String backgroundEnd) {
        this.backgroundEnd = backgroundEnd;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }
}
