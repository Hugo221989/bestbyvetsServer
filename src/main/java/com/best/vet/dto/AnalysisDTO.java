package com.best.vet.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnalysisDTO {
    List<IngredientInfo> ingredients;
    List<NutrientsDTO> nutriments;
}
