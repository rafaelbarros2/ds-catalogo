package com.rafaelb.dscatalog.mappers;

import com.rafaelb.dscatalog.dtos.ProductDTO;
import com.rafaelb.dscatalog.dtos.ProductListDTO;
import com.rafaelb.dscatalog.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface ProductMapper {

    ProductDTO productToDto(Product entity);

    ProductListDTO productListToProduct(Product product);

    Product productToProductListDTO(ProductListDTO productListDTO);

    Product toProduct(ProductDTO productDTO);

}
