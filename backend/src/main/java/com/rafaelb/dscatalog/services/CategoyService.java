package com.rafaelb.dscatalog.services;

import com.rafaelb.dscatalog.dtos.CategoryDTO;
import com.rafaelb.dscatalog.entities.Category;

import com.rafaelb.dscatalog.exceptions.ConstraintViolationException;
import com.rafaelb.dscatalog.exceptions.EntityNotFoundExcptions;
import com.rafaelb.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoyService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private Validator validator;


    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category category = obj.orElseThrow(() -> new EntityNotFoundExcptions("Entity not Found"));
        return new CategoryDTO(category);
    }

    @Transactional(readOnly = true)
    public CategoryDTO insert(CategoryDTO categoryDTO) {

            Category category = new Category();
            category.setName((categoryDTO.getName()));
            category = repository.save(category);
            return new CategoryDTO(category);
    }

    public void validateWithException(Category Category) throws ConstraintViolationException {

        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(Category);
        if (constraintViolations.isEmpty()) {
            return;
        }
        Set<String> errors = constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        throw new ConstraintViolationException(errors);
    }
}
