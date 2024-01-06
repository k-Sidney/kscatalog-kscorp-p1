package com.kscorp.kscatalog.resources;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kscorp.kscatalog.dto.ProductDTO;
import com.kscorp.kscatalog.services.ProductService;
import com.kscorp.kscatalog.services.exceptions.DatabaseException;
import com.kscorp.kscatalog.services.exceptions.ResourceNotFoundException;
import com.kscorp.kscatalog.tests.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private ProductDTO productDto;
	private PageImpl<ProductDTO> page;
	
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		productDto = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDto));
	
		
	when(service.findAllPaged(ArgumentMatchers.any(), null, null)).thenReturn(page);
	when(service.findById(existingId)).thenReturn(productDto);
	when(service.update(eq(existingId), ArgumentMatchers.any())).thenReturn(productDto);
	when(service.update(eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
	when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
	when(service.insert(ArgumentMatchers.any())).thenReturn(productDto);
	
	
	doNothing().when(service).delete(existingId);
	doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
	doThrow(DatabaseException.class).when(service).delete(dependentId);
	
	}
	
	@Test
	public void InsertShouldReturnCreatedAndReturnProductDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(productDto);
		ResultActions result =
				mockMvc.perform(post("/product", productDto)
				.content(jsonBody).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andReturn().equals(productDto);
		result.andExpect(jsonPath("$.id").exists());
	}
	
	@Test
	public void deleteShouldReturnNotContentWhenIdExists() throws Exception {
		ResultActions result =
				mockMvc.perform(delete("/product/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
	
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdNotExists() throws Exception {
		ResultActions result =
				mockMvc.perform(delete("/product/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	
	}
	
	@Test
	public void updateShouldReturnProductWhenIdExists() throws Exception{
		
		String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result =
				mockMvc.perform(put("/product/{id}",existingId)
						.content(jsonBody).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception{
	String jsonBody = objectMapper.writeValueAsString(productDto);
		
		ResultActions result =
				mockMvc.perform(put("/product/{id}",nonExistingId)
						.content(jsonBody).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		
		
		
		ResultActions result =
				mockMvc.perform(get("/product")
					.accept(MediaType.APPLICATION_JSON));
						
						result.andExpect(status().isOk());
		
		
	}
	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		ResultActions result =
				mockMvc.perform(get("/product/{id}",existingId)
					.accept(MediaType.APPLICATION_JSON));
						
						result.andExpect(status().isOk());
						result.andExpect(jsonPath("$.id").exists());
						result.andExpect(jsonPath("$.name").exists());
						result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() throws Exception{
		ResultActions result =
				mockMvc.perform(get("/product/{id}",nonExistingId)
					.accept(MediaType.APPLICATION_JSON));
						
						result.andExpect(status().isNotFound());
	}
}
