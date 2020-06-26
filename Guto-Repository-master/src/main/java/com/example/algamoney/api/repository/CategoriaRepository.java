package com.example.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Categoria;

// nessa classe todos o codigo de CRUD vai estar aqui, como inserts id, listar, listar id, delete, etc... ela extend de jpa
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	
}
