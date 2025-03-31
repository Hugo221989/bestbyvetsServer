package com.best.vet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubcategoryOptionDTO {
    private Long id;
    private String title;
    private String href;
    private String icon;
}
