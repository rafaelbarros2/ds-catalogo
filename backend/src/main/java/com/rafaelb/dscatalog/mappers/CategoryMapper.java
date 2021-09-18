package com.rafaelb.dscatalog.mappers;

import com.rafaelb.dscatalog.dtos.CategoryDTO;
import com.rafaelb.dscatalog.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface CategoryMapper {

    CategoryDTO toDto(Category entity);

    Category toCategory(CategoryDTO categoryDTO);

}
