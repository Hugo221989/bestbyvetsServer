package com.best.vet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SideBarInfoDTO {
    private Long categoryId;
    private Long subcategoryTypeId;
    private Long subcategoryOptionId;
    private String categoryName;
    private String subcategoryTypeName;
    private String subcategoryOptionName;
}
