package it.uniroma3.siw_techstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw_techstore.model.Prodotto;

public interface ProdottoRepository extends CrudRepository<Prodotto, Integer>{
	
	Optional<Prodotto> findByNomeProdotto(String nomeProdotto);
	
	List<Prodotto> findByCategoria_NomeCategoria(String nomeCategoria);

}
