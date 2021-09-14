package com.rafaelb.dscatalog.dtos;

import com.rafaelb.dscatalog.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO implements Serializable {

    private Long id;
    private String name;

    public CategoryDTO(Category category) {
       id = category.getId();
       name = category.getName();
    }
}
