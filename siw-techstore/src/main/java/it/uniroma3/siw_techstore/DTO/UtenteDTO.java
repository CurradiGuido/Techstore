package it.uniroma3.siw_techstore.DTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw_techstore.ENUM.Ruolo;
import it.uniroma3.siw_techstore.model.Ordine;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UtenteDTO {
	
	
	@NotEmpty(message ="Campo Obbligatorio")
	private String nome;
	
	@NotEmpty(message ="Campo Obbligatorio")
	private String cognome;
	
	@NotEmpty(message ="Campo Obbligatorio")
	private String username;
	
	@NotNull(message = "Immagine: parametro richiesto")
	private MultipartFile image;
	
	@Size(min = 6, message = "La password deve contenere almeno 6 caratteri")
	private String password;
	
	@NotEmpty(message = "Campo Obbligatorio")
	private String email;
	
	@NotEmpty(message = "Campo Obbligatorio")
	private String confirmPassword;
	

	private Ruolo ruolo;
	private List<Ordine> ordini = new ArrayList<>();
	public String getCognome() {
		return cognome;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public String getEmail() {
		return email;
	}
	public MultipartFile getImage() {
		return image;
	}
	public String getNome() {
		return nome;
	}
	public List<Ordine> getOrdini() {
		return ordini;
	}
	public String getPassword() {
		return password;
	}
	public Ruolo getRuolo() {
		return ruolo;
	}
	public String getUsername() {
		return username;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setOrdini(List<Ordine> ordini) {
		this.ordini = ordini;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	

}
