package it.uniroma3.siw_techstore.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "prodotto")
public class Prodotto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idprodotto")
	private int idProdotto;
	
	@Column(name = "nome", length = 100, nullable = false, unique = true)
	private String nomeProdotto;
	
	private String descrizione;
	
	private String image;
	
	@Column(name = "prezzo", precision = 10, scale = 3, nullable = false)
	private BigDecimal prezzo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_categoria", nullable = false, referencedColumnName = "idcategoria")
	private Categoria categoria;
	
	@OneToMany(mappedBy = "prodotto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VoceOrdine> vociOrdini = new ArrayList<>();
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prodotto other = (Prodotto) obj;
		return idProdotto == other.idProdotto && Objects.equals(nomeProdotto, other.nomeProdotto);
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public int getIdProdotto() {
		return idProdotto;
	}
	public String getImage() {
		return image;
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
	
	@Override
	public int hashCode() {
		return Objects.hash(idProdotto, nomeProdotto);
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public void setIdProdotto(int idProdotto) {
		this.idProdotto = idProdotto;
	}
	public void setImage(String image) {
		this.image = image;
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
	@Override
	public String toString() {
		return "Prodotto [idProdotto=" + idProdotto + ", nomeProdotto=" + nomeProdotto + ", descrizione=" + descrizione
				+ ", prezzo=" + prezzo + "]";
	}
	
	
	

}
