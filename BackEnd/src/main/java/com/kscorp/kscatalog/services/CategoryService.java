package com.kscorp.kscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kscorp.kscatalog.dto.CategoryDTO;
import com.kscorp.kscatalog.entities.Category;
import com.kscorp.kscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = repository.findAll();
		
		return  list.stream()
				.map(x -> new CategoryDTO(x))
				.collect(Collectors.toList());
		
	}
	
}
