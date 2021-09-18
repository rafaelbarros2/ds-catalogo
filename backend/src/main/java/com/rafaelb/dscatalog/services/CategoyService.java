package com.rafaelb.dscatalog.services;

import com.rafaelb.dscatalog.dtos.CategoryDTO;
import com.rafaelb.dscatalog.entities.Category;
import com.rafaelb.dscatalog.exceptions.DataBaseExcptions;
import com.rafaelb.dscatalog.exceptions.ResourceNotFoundExcptions;
import com.rafaelb.dscatalog.mappers.CategoryMapper;
import com.rafaelb.dscatalog.repositories.CategoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class CategoyService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoyService(
            CategoryRepository repository,
            CategoryMapper mapper
    ){
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> list = repository.findAll(pageRequest);
        return list.map(x -> mapper.toDto(x));

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category category = obj.orElseThrow(() -> new ResourceNotFoundExcptions("Entity not Found"));
        return mapper.toDto(category);
    }

    @Transactional(readOnly = true)
    public CategoryDTO insert(CategoryDTO categoryDTO) {

            Category category = mapper.toCategory(categoryDTO);
            category = repository.save(category);
            return mapper.toDto(category);
    }

    @Transactional(readOnly = true)
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try {
            Category category =  repository.getOne(id);
          mapper.toCategory(categoryDTO);
          repository.save(category);
            return mapper.toDto(category);
        }catch (EntityNotFoundException e){
            throw new ResourceNotFoundExcptions("Id inexistente: " + id);
        }
    }


    public void delete(Long id) {

        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundExcptions("Id inexistente:" + id);
        }catch (DataIntegrityViolationException e){
            throw new DataBaseExcptions("Violação de integridade");
        }
    }

}
