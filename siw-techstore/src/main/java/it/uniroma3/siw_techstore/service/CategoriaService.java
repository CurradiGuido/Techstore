package it.uniroma3.siw_techstore.service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw_techstore.DTO.CategoriaDTO;
import it.uniroma3.siw_techstore.Util.FileUtils;
import it.uniroma3.siw_techstore.model.Categoria;
import it.uniroma3.siw_techstore.model.Prodotto;
import it.uniroma3.siw_techstore.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Categoria creaCategoria(CategoriaDTO categoriaDTO, String filename, List<Prodotto> prodotti) {
		
		Categoria newCategoria = new Categoria();
		newCategoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
		newCategoria.setDescrizione(categoriaDTO.getDescrizione());
		newCategoria.setImage(filename);
		newCategoria.setProdotti(prodotti);
		
		return newCategoria;
		
	}
	
	public CategoriaDTO creaCategoriaDTO(Categoria categoria) {
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		categoriaDTO.setNomeCategoria(categoria.getNomeCategoria());
		categoriaDTO.setDescrizione(categoria.getDescrizione());
		categoriaDTO.setProdotti(categoria.getProdotti());
		
		return categoriaDTO;
	}
	
	public Categoria aggiornaCategoriaEsistente(Categoria categoria, CategoriaDTO categoriaDTO, String nuovaImmagine) {
		categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
	    categoria.setDescrizione(categoriaDTO.getDescrizione());
	    if (nuovaImmagine != null)
	        categoria.setImage(nuovaImmagine);
	    return categoria;
	}
	
	public boolean checkImage(CategoriaDTO categoriaDTO) {
		return categoriaDTO.getImage().isEmpty();
	}
	
	public void eliminaCategoriaConRisorse(Integer id) throws IOException {
	    Categoria categoria = trovaCategoriaPerId(id);
	    if (categoria == null) {
	        throw new IllegalArgumentException("Categoria non trovata con id " + id);
	    }

	    // Elimina l'immagine se esiste
	    String uploadDir = "src/main/resources/static/images/categorie/";
	    if (categoria.getImage() != null && !categoria.getImage().isEmpty()) {
	        FileUtils.eliminaImmagine(categoria.getImage(), uploadDir);
	    }

	    // Scollega i prodotti (se presenti)
	    if (categoria.getProdotti() != null && !categoria.getProdotti().isEmpty()) {
	        for (Prodotto prodotto : categoria.getProdotti()) {
	            prodotto.setCategoria(null); // oppure set a una "CategoriaGenerica"
	        }
	    }

	    // Ora elimina la categoria
	    categoriaRepository.delete(categoria);
	}

	
	public Categoria salvaCategoria(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}
	
	public Categoria trovaCategoriaPerId(Integer id) {
		return categoriaRepository.findById(id).orElse(null);
	}
	
	public Optional<Categoria> trovaCategoriaPerNome(String nome) {
		return categoriaRepository.findByNomeCategoria(nome);
	}
	
	//findAll() restituisce un Iterable. StreamSupport serve per creare uno stream a partite da un Iterable. Spliterator
	//consente di iterare gli elementi di Iteable in modo sequenziale (ecco il perché del parametro false. Se true sarebbe
	//stato in parallelo). Collectors.toList converte gli elementi dello stream in una lista.
	public List<Categoria> trovaTutteLeCategorie(){
		return StreamSupport.stream(categoriaRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

}
