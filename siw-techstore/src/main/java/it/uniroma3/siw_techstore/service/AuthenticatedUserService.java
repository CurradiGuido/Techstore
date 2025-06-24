package it.uniroma3.siw_techstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import it.uniroma3.siw_techstore.model.Utente;
import it.uniroma3.siw_techstore.repository.UtenteRepository;

@Component
public class AuthenticatedUserService {
	
	@Autowired
	private UtenteRepository utenteRepository;
	
	public Utente getUtenteAutentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        String username = null;

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            return utenteRepository.findByUsername(username);
        } else if (principal instanceof OAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            return utenteRepository.findByEmail(email);
        }

        return null;
    }
}
