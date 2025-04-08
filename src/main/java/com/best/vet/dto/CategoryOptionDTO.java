package com.best.vet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryOptionDTO {
    private String categoryName;
    private String optionValue;
}
