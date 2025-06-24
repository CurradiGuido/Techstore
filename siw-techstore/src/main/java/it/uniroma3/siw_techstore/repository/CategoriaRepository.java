package it.uniroma3.siw_techstore.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw_techstore.model.Categoria;

public interface CategoriaRepository extends CrudRepository<Categoria, Integer>{
	
	Optional<Categoria> findByNomeCategoria(String nomeCategoria);
}
