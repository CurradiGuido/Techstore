package it.uniroma3.siw_techstore.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String username = authentication.getName(); // Sicuro che non sia null
		logger.info("Logout avvenuto con successo per l'utente: {}", username);

		response.sendRedirect("/?logout");

	}

}
