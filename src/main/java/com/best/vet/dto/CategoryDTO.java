package com.best.vet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String title;
    private String icon;
    private List<SubcategoryGroupDTO> subItems;
}
