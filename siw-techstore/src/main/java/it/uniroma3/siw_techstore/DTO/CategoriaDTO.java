package it.uniroma3.siw_techstore.DTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw_techstore.model.Prodotto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CategoriaDTO {
	
	
	@NotEmpty(message = "Nome: parametro richiesto")
	private String nomeCategoria;
	
	@NotEmpty(message = "Descrizione: parametro richiesto")
	@Size(max = 255, message = "Descrizione: la lunghezza non può superare i 255 caratteri")
	private String descrizione;
	
	@NotNull(message = "Immagine: parametro richiesto")
	private MultipartFile image;
	
	private List<Prodotto> prodotti = new ArrayList<>();
	public String getDescrizione() {
		return descrizione;
	}
	public MultipartFile getImage() {
		return image;
	}
	public String getNomeCategoria() {
		return nomeCategoria;
	}
	public List<Prodotto> getProdotti() {
		return prodotti;
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
	public void setProdotti(List<Prodotto> prodotti) {
		this.prodotti = prodotti;
	}
	
	
	

}
