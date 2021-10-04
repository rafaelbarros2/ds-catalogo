package com.rafaelb.dscatalog.services;

import com.rafaelb.dscatalog.dtos.CategoryDTO;
import com.rafaelb.dscatalog.dtos.ProductDTO;
import com.rafaelb.dscatalog.dtos.ProductListDTO;
import com.rafaelb.dscatalog.entities.Category;
import com.rafaelb.dscatalog.entities.Product;
import com.rafaelb.dscatalog.exceptions.DataBaseExcptions;
import com.rafaelb.dscatalog.exceptions.ResourceNotFoundExcptions;
import com.rafaelb.dscatalog.mappers.ProductMapper;
import com.rafaelb.dscatalog.repositories.CategoryRepository;
import com.rafaelb.dscatalog.repositories.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {

    private final  ProductRepository repository;
    private final  ProductMapper mapper;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository repository, ProductMapper mapper, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }



    @Transactional(readOnly = true)
    public Page<ProductListDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> list = repository.findAll(pageRequest);
        return list.map(x -> new ProductListDTO(x));

    }

    @Transactional(readOnly = true)
    public ProductListDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product product = obj.orElseThrow(() -> new ResourceNotFoundExcptions("Entity not Found"));
        return new ProductListDTO(product, product.getCategories() );
    }

    @Transactional
    public ProductListDTO insert(ProductListDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductListDTO(entity);
    }

    @Transactional(readOnly = true)
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product product =  repository.getOne(id);
          mapper.toProduct(productDTO);
          repository.save(product);
            return mapper.productToDto(product);
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

    private void copyDtoToEntity(ProductListDTO dto, Product entity) {

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setGetImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            Category category = categoryRepository.getOne(catDto.getId());
            entity.getCategories().add(category);
        }
    }
}
