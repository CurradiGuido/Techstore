package it.uniroma3.siw_techstore.model;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "voceordine")
public class VoceOrdine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idvoceordine")
	private int idVoceOrdine;
	
	private int quantita;
	
	@Column(name = "prezzo", precision = 10, scale = 2, nullable = false)
	private BigDecimal prezzo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idOrdine", nullable = false)
	private Ordine ordine;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nome_prodotto", nullable = false, referencedColumnName = "nome")
	private Prodotto prodotto;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoceOrdine other = (VoceOrdine) obj;
		return ordine == other.ordine && prodotto == other.prodotto && idVoceOrdine == other.idVoceOrdine;
	}
	public int getIdVoceOrdine() {
		return idVoceOrdine;
	}
	public Ordine getOrdine() {
		return ordine;
	}
	public BigDecimal getPrezzo() {
		return prezzo;
	}
	public Prodotto getProdotto() {
		return prodotto;
	}
	public int getQuantita() {
		return quantita;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(ordine, prodotto, idVoceOrdine);
	}
	public void setIdVoceOrdine(int idVoceOrdine) {
		this.idVoceOrdine = idVoceOrdine;
	}
	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}
	public void setPrezzo(BigDecimal prezzo) {
		this.prezzo = prezzo;
	}
	
	public void setProdotto(Prodotto prodotto) {
		this.prodotto = prodotto;
	}
	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	@Override
	public String toString() {
		return "VoceOrdine [idVoceOrdine=" + idVoceOrdine + ", quantita=" + quantita + ", prezzo=" + prezzo
				+ ", ordine=" + ordine + ", prodotto=" + prodotto + "]";
	}
	
	
	

}
