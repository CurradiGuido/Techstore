package it.uniroma3.siw_techstore.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categoria")
public class Categoria {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idcategoria")
	private int idCategoria;
	
	
	@Column(name = "nome", length = 100, nullable = false, unique = true)
	private String nomeCategoria;
	
	@Column(name = "descrizione", length = 255, nullable = false)
	private String descrizione;
	
	private String image;
	
	@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Prodotto> prodotti = new ArrayList<>();


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		return idCategoria == other.idCategoria && Objects.equals(nomeCategoria, other.nomeCategoria);
	}


	public String getDescrizione() {
		return descrizione;
	}


	public int getIdCategoria() {
		return idCategoria;
	}


	public String getImage() {
		return image;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}


	public List<Prodotto> getProdotti() {
		return prodotti;
	}


	@Override
	public int hashCode() {
		return Objects.hash(idCategoria, nomeCategoria);
	}


	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}


	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}


	public void setProdotti(List<Prodotto> prodotti) {
		this.prodotti = prodotti;
	}


	@Override
	public String toString() {
		return "Categoria [idCategoria=" + idCategoria + ", nomeCategoria=" + nomeCategoria + "]";
	}
	
	

}
