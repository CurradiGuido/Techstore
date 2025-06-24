package it.uniroma3.siw_techstore.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import it.uniroma3.siw_techstore.ENUM.StatoOrdine;
import it.uniroma3.siw_techstore.model.Ordine;
import it.uniroma3.siw_techstore.model.Prodotto;
import it.uniroma3.siw_techstore.model.Utente;
import it.uniroma3.siw_techstore.model.VoceOrdine;
import it.uniroma3.siw_techstore.repository.OrdineRepository;
import it.uniroma3.siw_techstore.repository.VoceOrdineRepository;

@Service
@SessionScope
public class CarrelloService {

	private List<VoceOrdine> vociCarrello = new ArrayList<>();

	@Autowired
	private OrdineRepository ordineRepository;
	
	@Autowired
	private OrdineService ordineService;

	@Autowired
	private VoceOrdineRepository voceOrdineRepository;
	
	@Autowired
	private UtenteService utenteService;

	public Ordine getCarrelloCorrente() {
		Utente utente = utenteService.getUtenteCorrente();
		return ordineService.getOrdineInCorsoPerUtente(utente);
	}
	
	public List<VoceOrdine> getVociOrdine() {
	    return getCarrelloCorrente().getVociOrdini();
	}

	public void aggiungiProdotto(Prodotto prodotto) {
	    Ordine ordine = getCarrelloCorrente();

	    for (VoceOrdine voce : vociCarrello) {
	        if (voce.getProdotto().getIdProdotto() == prodotto.getIdProdotto()) {
	            voce.setQuantita(voce.getQuantita() + 1);
	            voce.setPrezzo(prodotto.getPrezzo().multiply(BigDecimal.valueOf(voce.getQuantita())));
	            
	            // Ricalcolo totale
	            BigDecimal totale = ordine.getVociOrdini()
	                                      .stream()
	                                      .map(VoceOrdine::getPrezzo)
	                                      .reduce(BigDecimal.ZERO, BigDecimal::add);
	            ordine.setTotale(totale);

	            ordineService.salvaOrdine(ordine);
	            return;
	        }
	    }

	    VoceOrdine voceOrdine = new VoceOrdine();
	    voceOrdine.setProdotto(prodotto);
	    voceOrdine.setQuantita(1);
	    voceOrdine.setPrezzo(prodotto.getPrezzo());
	    voceOrdine.setOrdine(ordine);
	    ordine.getVociOrdini().add(voceOrdine);
	    vociCarrello.add(voceOrdine);

	    // Ricalcolo totale
	    BigDecimal totale = ordine.getVociOrdini()
	                              .stream()
	                              .map(VoceOrdine::getPrezzo)
	                              .reduce(BigDecimal.ZERO, BigDecimal::add);
	    ordine.setTotale(totale);

	    ordineService.salvaOrdine(ordine);
	}
	
	public void aggiornaTotaleOrdine(Ordine ordine) {
		BigDecimal totale = ordine.getVociOrdini().stream()
				.map(VoceOrdine::getPrezzo)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		ordine.setTotale(totale);
		ordineService.salvaOrdine(ordine);
	}


	public void incrementa(Integer idVoceOrdine) {
		VoceOrdine voceOrdine = voceOrdineRepository.findById(idVoceOrdine).orElse(null);
		voceOrdine.setQuantita(voceOrdine.getQuantita() + 1);
		
		BigDecimal nuovoPrezzo = voceOrdine.getProdotto().getPrezzo().multiply(BigDecimal.valueOf(voceOrdine.getQuantita()));
		voceOrdine.setPrezzo(nuovoPrezzo);
		
		voceOrdineRepository.save(voceOrdine);
		aggiornaTotaleOrdine(voceOrdine.getOrdine());
	}
	
	public void decrementa(Integer idVoceOrdine) {
		VoceOrdine voceOrdine = voceOrdineRepository.findById(idVoceOrdine).orElse(null);
		Ordine ordine = voceOrdine.getOrdine();
		
		if(voceOrdine.getQuantita() <= 1) {
			voceOrdineRepository.delete(voceOrdine);
		}else {
			voceOrdine.setQuantita(voceOrdine.getQuantita() - 1);
			BigDecimal nuovoPrezzo = voceOrdine.getProdotto().getPrezzo().multiply(BigDecimal.valueOf(voceOrdine.getQuantita()));
			voceOrdine.setPrezzo(nuovoPrezzo);
			voceOrdineRepository.save(voceOrdine);
		}
		
		aggiornaTotaleOrdine(ordine);
	}
	
	public void rimuovi(Integer idVoce) {
		VoceOrdine voceOrdine = voceOrdineRepository.findById(idVoce).orElse(null);
		Ordine ordine = voceOrdine.getOrdine();
		
		voceOrdineRepository.deleteById(idVoce);
		aggiornaTotaleOrdine(ordine);
	}

	public void confermaOrdine(Utente utente) {
		Ordine carrello = getCarrelloCorrente();
		carrello.setStato(StatoOrdine.CONFERMATO);
		ordineRepository.save(carrello);
	}

	/*
	 * public List<VoceOrdine> getVociOrdine() { return vociCarrello; }
	 */

	public double getTotale() {
		return vociCarrello.stream().mapToDouble(voce -> voce.getPrezzo().intValue() * voce.getQuantita()).sum();
	}

	public void svuotaCarrello() {
		vociCarrello.clear();
	}

	public int getNumeroTotaleProdotti() {
		return vociCarrello.stream().mapToInt(VoceOrdine::getQuantita).sum();
	}

}
