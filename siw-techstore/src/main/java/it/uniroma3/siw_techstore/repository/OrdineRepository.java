package it.uniroma3.siw_techstore.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw_techstore.ENUM.StatoOrdine;
import it.uniroma3.siw_techstore.model.Ordine;
import it.uniroma3.siw_techstore.model.Utente;

public interface OrdineRepository extends CrudRepository<Ordine, Integer>{
	Optional<Ordine> findByUtenteAndStato(Utente utente, StatoOrdine stato);
	
	List<Ordine> findByUtente(Utente utente);
	
	List<Ordine> findAll();
	
	Optional<Ordine> findById(Integer id);
}
