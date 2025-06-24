package it.uniroma3.siw_techstore.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Ottieni il ruolo dell'utente
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            response.sendRedirect("/utente/admin/home");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("USER"))) {
            response.sendRedirect("/utente/user/home");
        } //else {
           // response.sendRedirect("/"); // In caso di ruolo non riconosciuto
        //}
    }
}