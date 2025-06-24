package it.uniroma3.siw_techstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.uniroma3.siw_techstore.ENUM.StatoOrdine;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ordine")
public class Ordine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idordine")
	private int idOrdine;
	
	@Column(name = "dataordine", nullable = false)
	private LocalDateTime dataOrdine;
	
	@Column(name = "totale", precision = 10, scale = 2, nullable = false)
	private BigDecimal totale;
	
	@Column(name = "via", length = 50, nullable = false)
	private String indirizzo;
	
	@Column(name = "numero", nullable = false)
	private int civico;
	
	@Column(name = "cap", nullable = false)
	private int cap;
	private String citta;
	private String paese;
	
	@Enumerated(EnumType.STRING)
	private StatoOrdine stato;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nome_cliente", nullable = false, referencedColumnName = "username")
	private Utente utente;
	
	@OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VoceOrdine> vociOrdini = new ArrayList<>();

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ordine other = (Ordine) obj;
		return civico == other.civico && Objects.equals(dataOrdine, other.dataOrdine) && utente == other.utente
				&& idOrdine == other.idOrdine && Objects.equals(indirizzo, other.indirizzo)
				&& Objects.equals(totale, other.totale);
	}

	public int getCap() {
		return cap;
	}

	public String getCitta() {
		return citta;
	}

	public int getCivico() {
		return civico;
	}

	public LocalDateTime getDataOrdine() {
		return dataOrdine;
	}

	public int getIdOrdine() {
		return idOrdine;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public String getPaese() {
		return paese;
	}

	public StatoOrdine getStato() {
		return stato;
	}

	public BigDecimal getTotale() {
		return totale;
	}

	public Utente getUtente() {
		return utente;
	}

	public List<VoceOrdine> getVociOrdini() {
		return vociOrdini;
	}

	@Override
	public int hashCode() {
		return Objects.hash(civico, dataOrdine, utente, idOrdine, indirizzo, totale);
	}

	public void setCap(int cap) {
		this.cap = cap;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public void setCivico(int civico) {
		this.civico = civico;
	}

	public void setDataOrdine(LocalDateTime dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	public void setIdOrdine(int idOrdine) {
		this.idOrdine = idOrdine;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public void setPaese(String paese) {
		this.paese = paese;
	}

	public void setStato(StatoOrdine stato) {
		this.stato = stato;
	}

	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public void setVociOrdini(List<VoceOrdine> vociOrdini) {
		this.vociOrdini = vociOrdini;
	}

	@Override
	public String toString() {
		return "Ordine [idOrdine=" + idOrdine + ", dataOrdine=" + dataOrdine + ", totale=" + totale + ", indirizzo="
				+ indirizzo + ", civico=" + civico + ", idCliente=" + utente + "]";
	}
	
	

}
