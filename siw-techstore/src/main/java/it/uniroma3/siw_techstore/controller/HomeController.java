package it.uniroma3.siw_techstore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw_techstore.model.Utente;
import it.uniroma3.siw_techstore.service.AuthenticatedUserService;
import it.uniroma3.siw_techstore.service.UtenteService;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private UtenteService utenteService;

	@Autowired
	AuthenticatedUserService authenticatedUserService;

	@GetMapping("/contatti")
	public String getContactsPage() {
		return "/user/contatti";
	}

	@GetMapping({ "", "/" })
	public String getHomePage(@RequestParam(value = "logout", required = false) String logout, Model model) {

		if (logout != null) {
			model.addAttribute("logout", true);
		}
		return "/user/home";
	}

	@GetMapping("/login")
	public String getLoginPage() {
		return "/user/login";
	}
	
	@GetMapping("/oauth/complete")
	public String completePage(Model model, @AuthenticationPrincipal OAuth2User user) {
	    Utente utente = authenticatedUserService.getUtenteAutentication();

	    if (utente == null) {
	        utente = new Utente();
	        if(user != null && user.getAttribute("email") != null) {
	            utente.setEmail(user.getAttribute("email"));
	        }
	    }
	    
	    model.addAttribute("utente", utente);
	    return "/user/complete-profile";
	}
	
	@PostMapping("/oauth/complete")
	public String completaProfilo(@ModelAttribute("utente") Utente utenteForm) {
	    logger.info("Dati ricevuti da form: " + utenteForm);
	    Utente utenteOpt = utenteService.trovaUtentePerMail(utenteForm.getEmail());

	    if (utenteOpt != null) {
	        Utente u = utenteService.completaOauth(utenteOpt, utenteForm);
	        logger.info("UtenteOpt: " + u);
	        utenteService.salvaUtente(u);
	    } else {
	        logger.error("Utente non trovato con email: " + utenteForm.getEmail());
	    }
	    
	    return "redirect:/utente/user/home";
	}



	@PostMapping("/oauth/register")
	public String registerOAuthUser(@RequestParam String email, @RequestParam String username, @RequestParam String password) {
		utenteService.completeRegistration(email, username, password);
		return "redirect:/login?success";
	}

	@GetMapping("/offerte")
	public String getOffersPage() {
		return "/user/offerte";
	}

}
