package it.uniroma3.siw_techstore.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import it.uniroma3.siw_techstore.DTO.OrdineDTO;
import it.uniroma3.siw_techstore.model.Ordine;
import it.uniroma3.siw_techstore.model.Prodotto;
import it.uniroma3.siw_techstore.model.Utente;
import it.uniroma3.siw_techstore.service.CarrelloService;
import it.uniroma3.siw_techstore.service.OrdineService;
import it.uniroma3.siw_techstore.service.ProdottoService;
import it.uniroma3.siw_techstore.service.UtenteService;

@Controller
@RequestMapping("/carrello")
public class CarrelloController {
	
	private static final Logger logger = LoggerFactory.getLogger(CarrelloController.class);

	@Autowired
	private CarrelloService carrelloService;

	@Autowired
	private ProdottoService prodottoService;

	@Autowired
	private OrdineService ordineService;
	
	@Autowired
	private UtenteService utenteService;

	@GetMapping("/visualizzaCarrello")
	public String visualizzaCarrello(Model model) {
		
		Utente utente = utenteService.getUtenteCorrente();
	    Ordine ordine = ordineService.getOrdineInCorsoPerUtente(utente);
	    
	    model.addAttribute("ordine", ordine);
	    model.addAttribute("vociCarrello", ordine.getVociOrdini());
	    model.addAttribute("totale", ordine.getTotale());
	    
	    return "/user/carrello";
	}

	@PostMapping("/aggiungi/{id}")
	public String aggiungiAlCarrello(@PathVariable("id") Integer idProdotto) {
		Prodotto prodotto = prodottoService.trovaProdottoPerId(idProdotto);
		logger.info("Il prodotto: " + prodotto.getNomeProdotto() + " è stato aggiunto al carrello");
		carrelloService.aggiungiProdotto(prodotto);
		return "redirect:/shop/visualizzaShop";
	}
	
	@PostMapping("/incrementa")
	public String incrementaQuantitaVoce(Integer idVoce) {
		carrelloService.incrementa(idVoce);
		return "redirect:/carrello/visualizzaCarrello?modifica";
	}
	
	@PostMapping("/decrementa")
	public String decrementaQuantitaVoce(Integer idVoce) {
		carrelloService.decrementa(idVoce);
		return "redirect:/carrello/visualizzaCarrello?modifica";
	}
	
	@PostMapping("/rimuovi")
	public String rimuoviVoceCarrello(Integer idVoce) {
		carrelloService.rimuovi(idVoce);
		return "redirect:/carrello/visualizzaCarrello?modifica";
	}

	@GetMapping("/checkout")
	public String formCheckout(Model model) {
		
		Utente utente = utenteService.getUtenteCorrente();
	    Ordine ordine = ordineService.getOrdineInCorsoPerUtente(utente);
	    
		model.addAttribute("ordineDTO", new OrdineDTO());
		model.addAttribute("totale", ordine.getTotale());
		return "/user/checkout";
	}

	@PostMapping("/checkout")
	public String confermaOrdine(@ModelAttribute("ordineDTO") OrdineDTO ordineDTO) {
	    Ordine ordine = carrelloService.getCarrelloCorrente();
	    
	    ordine = ordineService.confermaOrdineConDTO(ordine, ordineDTO);

	    ordineService.salvaOrdine(ordine); // confermiamo

	    logger.info("Ordine confermato da: " + ordine.getUtente().getUsername() + " - Totale: " + ordine.getTotale() + "€");
	    return "redirect:/carrello/visualizzaCarrello?success";
	}

}
