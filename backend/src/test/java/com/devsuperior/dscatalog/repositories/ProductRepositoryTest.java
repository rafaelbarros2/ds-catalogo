package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;
    private long existingId;
    private   long nonexistingId;
    private long countTotalProducts;

    @BeforeEach
    void setup(){
        existingId = 1L;
        nonexistingId = 1000L;
        countTotalProducts=26L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        long existingId = 1L;
            repository.deleteById(existingId);

           Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistObjectWhitAutoImcrmentIdIsNull(){
        Product product = Factory.createdProduct();
        product.setId(null);
        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts, product.getId());
    }

    @Test
    public void deleteShouldThrowEntityResultNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonexistingId);
        });
    }

    @Test
    public void findShouldWhenIdExists(){

        repository.findById(existingId);

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findShouldWhenIdDoesNotExist(){

        repository.findById(nonexistingId);

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }
}
