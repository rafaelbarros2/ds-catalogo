package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProductResourceIT {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private ProductDTO productDTO;

    private String password;
    private String userName;

    @BeforeEach
    public void setup()  {
        userName = "maria@gmail.com";
        password = "123456";

        productDTO = Factory.createdProductDTO();
        existingId = 1L;
        nonExistingId = 100000L;
        countTotalProducts = 25L;
    }

    @Test
    public void findAllShouldReturnSortedWhenPageSortByName() throws Exception {

        ResultActions resultActions= mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        resultActions.andExpect(jsonPath("$.content").exists());
        resultActions.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
    }

    @Test
    public void updateShouldReturnProductWhenIdExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, userName, password);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();
        ResultActions resultActions= mockMvc.perform(put("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(existingId));
        resultActions.andExpect(jsonPath("$.name").value(expectedName) );
        resultActions.andExpect(jsonPath("$.description").value(expectedDescription) );
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, userName, password);

        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions resultActions= mockMvc.perform(put("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }

}
