package com.best.vet.repository;

import com.best.vet.entity.SubcategoryOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubcategoryOptionRepository extends JpaRepository<SubcategoryOption, Long> {
    List<SubcategoryOption> findByCategory_Id(Long categoryId);
}
