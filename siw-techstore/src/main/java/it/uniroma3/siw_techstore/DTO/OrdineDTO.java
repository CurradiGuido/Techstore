package it.uniroma3.siw_techstore.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import it.uniroma3.siw_techstore.ENUM.StatoOrdine;
import jakarta.validation.constraints.NotEmpty;

public class OrdineDTO {
	
	
	private LocalDateTime dataOrdine;
	private BigDecimal totale;
	
	@NotEmpty(message = "Indirizzo: Inserire indirizzo")
	private String indirizzo;
	
	@NotEmpty(message = "Civico: Inserire civico")
	private int civico;
	
	@NotEmpty(message = "CAP: Inserire CAP")
	private int cap;
	
	@NotEmpty(message = "Citta: Inserire Citta")
	private String citta;
	
	@NotEmpty(message = "Paese: Inserire Paese")
	private String paese;
	
	
	private StatoOrdine stato;
	public LocalDateTime getDataOrdine() {
		return dataOrdine;
	}
	public void setDataOrdine(LocalDateTime dataOrdine) {
		this.dataOrdine = dataOrdine;
	}
	public BigDecimal getTotale() {
		return totale;
	}
	public void setTotale(BigDecimal totale) {
		this.totale = totale;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public int getCivico() {
		return civico;
	}
	public void setCivico(int civico) {
		this.civico = civico;
	}
	public int getCap() {
		return cap;
	}
	public void setCap(int cap) {
		this.cap = cap;
	}
	public String getCitta() {
		return citta;
	}
	public void setCitta(String citta) {
		this.citta = citta;
	}
	public String getPaese() {
		return paese;
	}
	public void setPaese(String paese) {
		this.paese = paese;
	}
	public StatoOrdine getStato() {
		return stato;
	}
	public void setStato(StatoOrdine stato) {
		this.stato = stato;
	}
	
	
	

}
