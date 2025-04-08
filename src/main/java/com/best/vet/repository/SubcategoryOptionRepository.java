package com.best.vet.repository;

import com.best.vet.dto.CategoryOptionDTO;
import com.best.vet.dto.SideBarInfoDTO;
import com.best.vet.entity.SubcategoryOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubcategoryOptionRepository extends JpaRepository<SubcategoryOption, Long> {
    List<SubcategoryOption> findByCategory_Id(Long categoryId);

    @Query("SELECT new com.best.vet.dto.CategoryOptionDTO(so.category.name, so.optionValue) " +
            "FROM SubcategoryOption so WHERE so.id = :id")
    CategoryOptionDTO findCategoryNameAndOptionValueById(@Param("id") Long id);

    @Query("SELECT new com.best.vet.dto.SideBarInfoDTO(so.category.id, so.subcategoryType.id, so.id, so.category.name, so.subcategoryType.name, so.optionValue) " +
            "FROM SubcategoryOption so WHERE so.id = :id")
    SideBarInfoDTO findSideBarInfoById(@Param("id") Long id);
}
