package it.uniroma3.siw_techstore.controller;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import it.uniroma3.siw_techstore.DTO.UtenteDTO;
import it.uniroma3.siw_techstore.Util.FileUtils;
import it.uniroma3.siw_techstore.model.Ordine;
import it.uniroma3.siw_techstore.model.Utente;
import it.uniroma3.siw_techstore.service.AuthenticatedUserService;
import it.uniroma3.siw_techstore.service.OrdineService;
import it.uniroma3.siw_techstore.service.UtenteService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/utente")
public class UtenteController {
	
	private static final Logger logger = LoggerFactory.getLogger(UtenteController.class);
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private OrdineService ordineService;
	
	@Autowired
	AuthenticatedUserService authenticatedUserService;
	
	
	@GetMapping("/register")
	public String getRegisterPage(Model model) {
		
		UtenteDTO utenteDTO = new UtenteDTO();
		model.addAttribute(utenteDTO);
		model.addAttribute("success", false);
		return "/user/register";
		
	}
	
	@PostMapping("/register")
	public String registerUser(Model model, @Valid @ModelAttribute UtenteDTO utenteDTO, BindingResult result) throws IOException {
		
		
		if(!utenteService.checkPasswordAndConfirm(utenteDTO.getPassword(), utenteDTO.getConfirmPassword())) {
			result.addError(new FieldError("utenteDTO", "confirmPassword", "Reinserisci le password e assicurati che siano uguali"));
		}
		
		if(utenteService.checkEmail(utenteDTO.getEmail())) {
			result.addError(new FieldError("utenteDTO", "email", "Email già in utilizzo"));
		}
		
		if(utenteService.checkUsername(utenteDTO.getUsername())) {
			result.addError(new FieldError("utenteDTO", "username", "Username già in utilizzo"));
		}
		
		if(result.hasErrors()) {
			return "/user/register";
		}
		
		try {
	        String imageName = FileUtils.salvaImmagine(utenteDTO.getImage(), "src/main/resources/static/images/utenti/");
	        Utente nuovoUtente = utenteService.creaUtente(utenteDTO, imageName);
	        utenteService.salvaUtente(nuovoUtente);

	        // Successo: resetta il DTO e mostra il messaggio di successo
	        model.addAttribute("utenteDTO", new UtenteDTO());
	        model.addAttribute("success", true);
	        logger.info("L'utente: " + nuovoUtente.getUsername() + " si è appena registrato");
	        return "/user/register";
	    } catch (Exception e) {
	        result.addError(new FieldError("utenteDTO", "general", "Si è verificato un errore: " + e.getMessage()));
	        logger.error("E' stato rilevato il seguente errore in fase di registrazione dell'utente: " + e.getMessage());
	        return "/user/register";
	    }
		

	}

    @GetMapping("/user/home")
    @PreAuthorize("hasRole('USER')")
    public String userHome(Model model) {
    	Utente utente = authenticatedUserService.getUtenteAutentication();
    	model.addAttribute("utenteDTO", utente);
    	logger.info("L'utente: " + utente.getUsername() + " con ruolo: " + utente.getRuolo() + " ha effettuato l'accesso");
        return "/user/userHomePage";
        
    }
    
    @GetMapping("/admin/home")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminHome(Model model, @AuthenticationPrincipal UserDetails userDetails) {		
    	Utente admin = authenticatedUserService.getUtenteAutentication();
		model.addAttribute("adminDTO", admin);
		logger.info("L'admin: " + admin.getUsername() + " con ruolo: " + admin.getRuolo() + " ha effettuato l'accesso");
        return "/admin/adminHomePage";
        
    }
    
    @GetMapping("/visualizzaProfilo")
    public String visualizzaProfilo(Model model, @AuthenticationPrincipal UserDetails userDetails) {
    	Utente utente = authenticatedUserService.getUtenteAutentication();
    	List<Ordine> ordini = ordineService.trovaOrdiniPerUtente(utente);
    	model.addAttribute("utente", utente);
    	model.addAttribute("ordini", ordini);
    	return "/user/profiloUtente";
    }
    
    
    //Questo va nel Admin Controller --> vediamo se ne vale la pena
    @GetMapping("visualizzaUtenti")
    public String visualizzaUtenti(Model model) {
    	List<Utente> utenti = utenteService.trovaTuttiGliUtenti();
    	model.addAttribute("utenti", utenti);
    	return "/admin/utente/visualizzaUtenti";
    }
    
    @PostMapping("/modificaUtente")
    public String salvaModificheUtente(@ModelAttribute("utente") Utente utenteAggiornato, Principal principal) {
    	Utente utente = authenticatedUserService.getUtenteAutentication();
    	String username = utente.getUsername();
    	try {
    		utenteService.aggiornaDatiProfilo(username, utenteAggiornato);   
    		logger.info("L'utente: " + utente.getUsername() + " ha appena aggiornato i suoi dati personali");
    	}catch(Exception e) {
    		logger.error("E' stato rilevato il seguente errore durante la modifica dei dati personali di un utente: " + e.getMessage());
    	}
    	return "redirect:/utente/visualizzaProfilo?success";
    }
    
    @PostMapping("/modificaFotoProfilo")
    public String cambiaFotoProfilo(@RequestParam("image") MultipartFile image, Principal principal) {
    	String nuovaImmagine = null;
    	Utente utente = authenticatedUserService.getUtenteAutentication();
    	if(!image.isEmpty()) {
    		nuovaImmagine = FileUtils.salvaImmagine(image, "src/main/resources/static/images/utenti/");
    	}
    	utente.setImage(nuovaImmagine);
    	utenteService.salvaUtente(utente);
    	logger.info("L'utente: " + utente.getUsername() + " ha appena modificato la sua foto profilo");
    	return "redirect:/utente/visualizzaProfilo?success";
    }
    
    

}
