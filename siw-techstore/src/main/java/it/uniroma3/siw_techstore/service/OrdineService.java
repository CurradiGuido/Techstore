package it.uniroma3.siw_techstore.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw_techstore.DTO.OrdineDTO;
import it.uniroma3.siw_techstore.ENUM.StatoOrdine;
import it.uniroma3.siw_techstore.model.Ordine;
import it.uniroma3.siw_techstore.model.Utente;
import it.uniroma3.siw_techstore.model.VoceOrdine;
import it.uniroma3.siw_techstore.repository.OrdineRepository;

@Service
public class OrdineService {
	private static final Logger logger = LoggerFactory.getLogger(OrdineService.class);
	@Autowired
	private OrdineRepository ordineRepository;
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private CarrelloService carrelloService;
	
	public Ordine salvaOrdine (Ordine ordine) {
		return ordineRepository.save(ordine);
	}
	
	public Ordine creaOrdine(OrdineDTO ordineDTO) {
		
		Ordine ordine = new Ordine();
		ordine.setCap(ordineDTO.getCap());
		ordine.setCitta(ordineDTO.getCitta());
		ordine.setCivico(ordineDTO.getCivico());
		ordine.setDataOrdine(LocalDateTime.now());
		ordine.setIndirizzo(ordineDTO.getIndirizzo());
		ordine.setPaese(ordineDTO.getPaese());
		ordine.setStato(StatoOrdine.CONFERMATO);
		ordine.setTotale(ordineDTO.getTotale());
		ordine.setUtente(utenteService.getUtenteCorrente());
		
		for (VoceOrdine voce : carrelloService.getVociOrdine()) {
            voce.setOrdine(ordine);
        }
		
		ordine.getVociOrdini().addAll(carrelloService.getVociOrdine());

        return ordine;
	}
	
	public Ordine getOrdineInCorsoPerUtente(Utente utente) {
		Optional<Ordine> ordineEsistente = ordineRepository.findByUtenteAndStato(utente, StatoOrdine.CREATO);
		
		if(ordineEsistente.isPresent()) {
			Ordine ordine = ordineEsistente.get();

			logger.info("Ordine trovato: " + ordineEsistente.get().getIdOrdine() + " con un totale di: " + ordineEsistente.get().getTotale());
	        // Calcolo totale a partire dalle voci ordine esistenti
	        BigDecimal totale = ordine.getVociOrdini()
	                                  .stream()
	                                  .map(VoceOrdine::getPrezzo)
	                                  .reduce(BigDecimal.ZERO, BigDecimal::add);

	        ordine.setTotale(totale);
	        ordineRepository.save(ordine);  // Salvo il totale aggiornato

	        logger.info("Ordine trovato: " + ordine.getIdOrdine() + " con un totale di: " + ordine.getTotale());
	        return ordine;
		}else {
			Ordine nuovoOrdine = new Ordine();
			nuovoOrdine.setUtente(utente); nuovoOrdine.setStato(StatoOrdine.CREATO);
			nuovoOrdine.setDataOrdine(LocalDateTime.now());
			nuovoOrdine.setTotale(BigDecimal.ZERO);
			nuovoOrdine.setIndirizzo("--");
			return ordineRepository.save(nuovoOrdine); 			
		}
		
		/*
		 * return ordineRepository.findByUtenteAndStato(utente, StatoOrdine.CREATO)
		 * .orElseGet(() -> { Ordine nuovoOrdine = new Ordine();
		 * nuovoOrdine.setUtente(utente); nuovoOrdine.setStato(StatoOrdine.CREATO);
		 * nuovoOrdine.setDataOrdine(LocalDateTime.now());
		 * nuovoOrdine.setTotale(BigDecimal.ZERO); nuovoOrdine.setIndirizzo("--");
		 * return ordineRepository.save(nuovoOrdine); });
		 */
		
	}
	
	public Ordine confermaOrdineConDTO(Ordine ordine, OrdineDTO ordineDTO) {
		
		ordine.setCap(ordineDTO.getCap());
	    ordine.setCitta(ordineDTO.getCitta());
	    ordine.setCivico(ordineDTO.getCivico());
	    ordine.setIndirizzo(ordineDTO.getIndirizzo());
	    ordine.setPaese(ordineDTO.getPaese());
	    ordine.setTotale(ordineDTO.getTotale());
	    ordine.setStato(StatoOrdine.CONFERMATO);
	    ordine.setDataOrdine(LocalDateTime.now());
	    
	    return ordine;
	}

	public List<Ordine> trovaOrdiniPerUtente(Utente utente){
		return ordineRepository.findByUtente(utente);
	}
	
	public List<Ordine> trovaTuttiGliOrdini(){
		return ordineRepository.findAll();
	}
	
	public Optional<Ordine> trovaOrdineConId(Integer id){
		return ordineRepository.findById(id);
	}
}
