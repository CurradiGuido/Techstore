package it.uniroma3.siw_techstore.DTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw_techstore.model.VoceOrdine;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ProdottoDTO {
	
	@NotEmpty(message = "nomeProdotto: parametro richiesto")
	private String nomeProdotto;
	
	@NotEmpty(message = "descrizione: parametro richiesto")
	private String descrizione;
	
	@NotNull(message = "prezzo: parametro richiesto")
	private BigDecimal prezzo;
	
	@NotEmpty(message = "nomeCategoria: parametro richiesto")
	private String nomeCategoria;
	
	@NotNull(message = "Immagine: parametro richiesto")
	private MultipartFile image;
	
	
	private List<VoceOrdine> vociOrdini = new ArrayList<>();


	public String getDescrizione() {
		return descrizione;
	}


	public MultipartFile getImage() {
		return image;
	}


	public String getNomeCategoria() {
		return nomeCategoria;
	}


	public String getNomeProdotto() {
		return nomeProdotto;
	}


	public BigDecimal getPrezzo() {
		return prezzo;
	}


	public List<VoceOrdine> getVociOrdini() {
		return vociOrdini;
	}


	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	public void setImage(MultipartFile image) {
		this.image = image;
	}


	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}


	public void setNomeProdotto(String nomeProdotto) {
		this.nomeProdotto = nomeProdotto;
	}


	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}


	public void setVociOrdini(List<VoceOrdine> vociOrdini) {
		this.vociOrdini = vociOrdini;
	}
	
	
	
	

}
