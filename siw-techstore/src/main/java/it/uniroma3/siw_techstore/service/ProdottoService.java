package it.uniroma3.siw_techstore.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw_techstore.DTO.ProdottoDTO;
import it.uniroma3.siw_techstore.Util.FileUtils;
import it.uniroma3.siw_techstore.model.Categoria;
import it.uniroma3.siw_techstore.model.Prodotto;
import it.uniroma3.siw_techstore.repository.ProdottoRepository;

@Service
public class ProdottoService {
	
	@Autowired
	private ProdottoRepository prodottoRepository;
	
	public void cancellaProdotto(Prodotto prodotto) {
		prodottoRepository.delete(prodotto);
	}
	
	public Prodotto salvaProdotto(Prodotto prodotto) {
		return prodottoRepository.save(prodotto);
	}
	
	public Prodotto trovaProdottoPerId(Integer id) {
		return prodottoRepository.findById(id).orElse(null);
	}
	
	public Optional<Prodotto> trovaProdottoPerNome(String nomeProdotto) {
		return prodottoRepository.findByNomeProdotto(nomeProdotto);
	}
	
	public List<Prodotto> trovaTuttiIProdotti(){
		return StreamSupport.stream(prodottoRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}
	
	public boolean checkProdotto(ProdottoDTO prodottoDTO) {
		return prodottoRepository.findByNomeProdotto(prodottoDTO.getNomeProdotto()).isPresent();
	}
	
	public boolean checkImmagineProdotto(ProdottoDTO prodottoDTO) {
		return prodottoDTO.getImage().isEmpty();
	}
	
	public List<Prodotto> findByCategoria(String nomeCategoria) {
        return this.prodottoRepository.findByCategoria_NomeCategoria(nomeCategoria);
    }

	public Prodotto creaProdotto(ProdottoDTO prodottoDTO, String filename, Categoria categoria) {
		Prodotto nuovoProdotto = new Prodotto();
		nuovoProdotto.setNomeProdotto(prodottoDTO.getNomeProdotto());
		nuovoProdotto.setDescrizione(prodottoDTO.getDescrizione());
		nuovoProdotto.setPrezzo(prodottoDTO.getPrezzo());
		nuovoProdotto.setImage(filename);
		nuovoProdotto.setCategoria(categoria);
		
		return nuovoProdotto;
	}
	
	public ProdottoDTO creaProdottoDTO(Prodotto prodotto) {
		ProdottoDTO prodottoDTO = new ProdottoDTO();
		prodottoDTO.setNomeProdotto(prodotto.getNomeProdotto());
		prodottoDTO.setDescrizione(prodotto.getDescrizione());
		prodottoDTO.setPrezzo(prodotto.getPrezzo());
		prodottoDTO.setNomeCategoria(prodotto.getCategoria().getNomeCategoria());
		
		return prodottoDTO;
	}
	
	public Prodotto aggiornaProdottoEsistente(Prodotto prodotto, ProdottoDTO prodottoDTO, String nuovaImmagine, Categoria categoria) {
		
		prodotto.setNomeProdotto(prodottoDTO.getNomeProdotto());
		prodotto.setDescrizione(prodottoDTO.getDescrizione());
		prodotto.setPrezzo(prodottoDTO.getPrezzo());
		prodotto.setCategoria(categoria);
		prodotto.setImage(nuovaImmagine);
		
		return prodotto;
	}
	
	public void eliminaProdottoConRisorse(Integer id) throws IOException{
		Prodotto prodotto = trovaProdottoPerId(id);
		String uploadDir = "src/main/resources/static/images/prodotti/";
		
		if (prodotto == null) {
	        throw new IllegalArgumentException("Categoria non trovata con id " + id);
	    }
		
		if (prodotto.getImage() != null && !prodotto.getImage().isEmpty()) {
			FileUtils.eliminaImmagine(prodotto.getImage(), uploadDir);
		}
		
		// Gestione prodotti associati (da verificare in base alla logica di dominio)
        if (prodotto.getCategoria() != null) {
            prodotto.setCategoria(null);
        }
        
        prodottoRepository.delete(prodotto);
		
	}
	
}
