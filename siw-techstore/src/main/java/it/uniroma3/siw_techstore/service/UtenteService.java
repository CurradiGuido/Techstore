package it.uniroma3.siw_techstore.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw_techstore.DTO.UtenteDTO;
import it.uniroma3.siw_techstore.ENUM.Ruolo;
import it.uniroma3.siw_techstore.model.Utente;
import it.uniroma3.siw_techstore.repository.UtenteRepository;

@Service
public class UtenteService implements UserDetailsService{
	
	@Autowired
	private UtenteRepository utenteRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	AuthenticatedUserService authenticatedUserService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Utente utente = utenteRepo.findByUsername(username);
		
		if(utente != null) {
			var springUser = User.withUsername(utente.getUsername())
							     .password(utente.getPassword())
							     .roles(utente.getRuolo().toString())
							     .build();
			return springUser;
		}
		
		return null;
	}
	
	public Utente getUtenteCorrente() {
		Utente utente = authenticatedUserService.getUtenteAutentication();
        String username = utente.getUsername(); // questo è lo username loggato
        return utenteRepo.findByUsername(username);
    }
	
	public Utente salvaUtente(Utente utente) {
		return utenteRepo.save(utente);
	}
	
	public List<Utente> trovaTuttiGliUtenti(){
		return StreamSupport.stream(utenteRepo.findAll().spliterator(), false).collect(Collectors.toList());
	}
	
	public Utente trovaUtentePerId(Integer id) {
		return utenteRepo.findById(id).orElse(null);
	}
	
	public Utente trovaUtentePerMail(String email) {
		return utenteRepo.findByEmail(email);
	}
	
	public Utente trovaUtentePerUsername(String username){
		return utenteRepo.findByUsername(username);
	}
	
	public boolean checkEmail(String email) {
		return utenteRepo.findByEmail(email) != null;
	}
	
	public boolean checkUsername(String username) {
		return utenteRepo.findByUsername(username) != null;
	}
	
	public Utente creaUtente(UtenteDTO utenteDTO, String filename) {
		
		Utente newUtente = new Utente();
		newUtente.setNome(utenteDTO.getNome());
		newUtente.setCognome(utenteDTO.getCognome());
		newUtente.setEmail(utenteDTO.getEmail());
		newUtente.setUsername(utenteDTO.getUsername());
		newUtente.setPassword(bCryptPasswordEncoder.encode(utenteDTO.getPassword()));
		newUtente.setRuolo(Ruolo.USER);
		newUtente.setImage(filename);
		
		return newUtente;
		
	}
	
	public Utente completaOauth(Utente utenteOpt, Utente utenteForm) {
		Utente utente = utenteOpt;
		utente.setNome(utenteForm.getNome());
		utente.setCognome(utenteForm.getCognome());
		utente.setUsername(utenteForm.getUsername());
		utente.setPassword(bCryptPasswordEncoder.encode("oauth"));
		return utente;		
	}
	
	public boolean checkPasswordAndConfirm(String password, String confirmPassword) {
		return password.equals(confirmPassword);
	}
	
	public void aggiornaDatiProfilo(String username, Utente utenteAggiornato) {
		Utente utente = trovaUtentePerUsername(username);
		utente.setNome(utenteAggiornato.getNome());
		utente.setCognome(utenteAggiornato.getCognome());
		utente.setEmail(utenteAggiornato.getEmail());
		utenteRepo.save(utente);
	}
	
	public void createTemporaryOAuthUser(String email, String fullName) {
	    // Separa il nome completo in nome e cognome (rudimentale ma efficace)
	    String[] names = fullName.split(" ", 2);
	    String nome = names.length > 0 ? names[0] : "Nome";
	    String cognome = names.length > 1 ? names[1] : "Cognome";

	    // Verifica che non esista già un utente con questa email
	    if (utenteRepo.findByEmail(email) != null) {
	        return; // evita di creare duplicati
	    }

	    Utente u = new Utente();
	    u.setNome(nome);
	    u.setCognome(cognome);
	    u.setEmail(email);

	    // Genera uno username basato sull'email
	    u.setUsername(email);

	    // Imposta una password fittizia sicura (criptata) per evitare vincoli null
	    String fakePassword = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());
	    u.setPassword(fakePassword);
	    u.setRuolo(Ruolo.USER);

	    utenteRepo.save(u);
	}
	
	public void completeRegistration(String email, String username, String password) {
        Utente u = utenteRepo.findByEmail(email);
        u.setUsername(username);
        u.setPassword(new BCryptPasswordEncoder().encode(password));
        utenteRepo.save(u);
    }


}
