package com.best.vet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class SubcategoryGroupDTO {
    private Long id;
    private String title;
    private List<SubcategoryOptionDTO> subItems;
}
