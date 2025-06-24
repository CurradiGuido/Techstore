package it.uniroma3.siw_techstore.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.uniroma3.siw_techstore.ENUM.Ruolo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="utente")
public class Utente {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "idcliente")
	private int idCliente;
	
	@Column(name = "nome", length = 50, nullable = false)
	private String nome;
	
	@Column(name = "cognome", length = 50, nullable = false)
	private String cognome;
	
	@Column(name = "username", length = 50, nullable = false, unique = true)
	private String username;
	
	@Column(name="pswd", length = 255, nullable = false)
	private String password;
	
	@Column(name="email", nullable = false, unique = true)
	private String email;
	
	private String image;
	
	@Enumerated(EnumType.STRING)
	@Column(name="ruolo", length = 7, nullable = false)
	private Ruolo ruolo;
	
	@OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ordine> ordini = new ArrayList<>();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utente other = (Utente) obj;
		return idCliente == other.idCliente && Objects.equals(ruolo, other.ruolo)
				&& Objects.equals(username, other.username);
	}

	public String getCognome() {
		return cognome;
	}

	public String getEmail() {
		return email;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public String getImage() {
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

	@Override
	public int hashCode() {
		return Objects.hash(idCliente, ruolo, username);
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}	

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public void setImage(String image) {
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

	@Override
	public String toString() {
		return "Utente [idCliente=" + idCliente + ", username=" + username + ", password=" + password + ", ruolo="
				+ ruolo + "]";
	}
	
	
	
	

}
