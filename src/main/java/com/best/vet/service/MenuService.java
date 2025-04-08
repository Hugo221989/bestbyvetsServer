package com.best.vet.service;

import com.best.vet.dto.*;
import com.best.vet.entity.Category;
import com.best.vet.entity.SubcategoryOption;
import com.best.vet.repository.CategoryRepository;
import com.best.vet.repository.SubcategoryOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryOptionRepository subcategoryOptionRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> getMenuTree() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();

        for (Category category : categories) {
            CategoryDTO catDto = new CategoryDTO();
            catDto.setId(category.getId());
            catDto.setTitle(category.getName());
            catDto.setIcon(category.getIcon());

            List<SubcategoryOption> options = subcategoryOptionRepository.findByCategory_Id(category.getId());
            Map<Long, List<SubcategoryOption>> grouped = options.stream()
                    .collect(Collectors.groupingBy(o -> o.getSubcategoryType().getId()));

            List<SubcategoryGroupDTO> groups = new ArrayList<>();
            // Para cada grupo (tipo de subcategoría) crea el DTO
            for (Map.Entry<Long, List<SubcategoryOption>> entry : grouped.entrySet()) {
                SubcategoryOption anyOption = entry.getValue().getFirst();
                SubcategoryGroupDTO groupDto = new SubcategoryGroupDTO();
                groupDto.setId(anyOption.getSubcategoryType().getId());
                groupDto.setTitle(anyOption.getSubcategoryType().getName());
                // Mapea las opciones a su DTO
                List<SubcategoryOptionDTO> optionDTOs = entry.getValue().stream().map(o -> {
                    SubcategoryOptionDTO dto = new SubcategoryOptionDTO();
                    dto.setId(o.getId());
                    dto.setTitle(o.getOptionValue());
                    dto.setHref(o.getHref());
                    dto.setIcon(o.getIcon());
                    return dto;
                }).collect(Collectors.toList());
                groupDto.setSubItems(optionDTOs);
                groups.add(groupDto);
            }
            catDto.setSubItems(groups);
            categoryDTOs.add(catDto);
        }
        return categoryDTOs;
    }

    public CategoryOptionDTO getCategoryAndSubcategoryOptionNamesBySubcategoryOptionId(Long id){
        return subcategoryOptionRepository.findCategoryNameAndOptionValueById(id);
    }

    public SideBarInfoDTO getSideBarInfoBySubcategoryOptionId(Long id){
        return subcategoryOptionRepository.findSideBarInfoById(id);
    }

}
