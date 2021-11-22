package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existId;
    private Long nonExistId;
    private Long dependentId;
    private PageImpl<ProductDTO> page;
    private ProductDTO productDTO;

    @BeforeEach
    public void setup() {
        existId = 1L;
        nonExistId = 2L;
        dependentId = 3L;

        productDTO = Factory.createdProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        when(service.findAllPaged(any())).thenReturn(page);

        when(service.findById(existId)).thenReturn(productDTO);
        when(service.findById(nonExistId)).thenThrow(ResourceNotFoundException.class);

        when(service.update(eq(existId), any())).thenReturn(productDTO);
        when(service.update(eq(nonExistId), any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(service).delete(existId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistId);
        doThrow(DatabaseException.class).when(service).delete(dependentId);

    }

    @Test
    public void findAllshouldReturnPage() throws Exception {
        ResultActions resultActions= mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());

    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions resultActions= mockMvc.perform(get("/products/{id}", existId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions resultActions= mockMvc.perform(get("/products/{id}", nonExistId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductWhenIdExists() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions resultActions= mockMvc.perform(put("/products/{id}", existId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions resultActions= mockMvc.perform(put("/products/{id}", nonExistId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnProductWhenIdExists() throws Exception {
        ResultActions resultActions= mockMvc.perform(delete("/products/{id}", existId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNoContent());

    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions resultActions= mockMvc.perform(delete("/products/{id}", nonExistId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnDataBaseExcptionWhenIdDoesNotExists() throws Exception {
        ResultActions resultActions= mockMvc.perform(delete("/products/{id}", dependentId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isBadRequest());
    }
}
