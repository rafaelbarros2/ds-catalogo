package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Product;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT obj FROM Product INNER JOIN obj.categories cats WHERE "
            + "(COALESCE(:category) IS NULL OR :category IN :categories) AND "
            + "LOWER(obj.name LIKE LOWER (CONCAT('% ', :name, '%')) ) AND")
    Page<Product> findSort(List<Category> categories, Pageable pageable, String name);

    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products")
    List<Category> findProductsWithCategories(List<Product> products);
}
