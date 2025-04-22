package com.best.vet.dto;

import lombok.Data;

@Data
public class IngredientInfo {
    private final String text;
    private final double percentEstimate;

    public IngredientInfo(String text, double percentEstimate) {
        this.text = text;
        this.percentEstimate = percentEstimate;
    }
}
