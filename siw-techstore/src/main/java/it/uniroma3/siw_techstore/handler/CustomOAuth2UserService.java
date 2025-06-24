package it.uniroma3.siw_techstore.handler;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import it.uniroma3.siw_techstore.ENUM.Ruolo;
import it.uniroma3.siw_techstore.model.Utente;
import it.uniroma3.siw_techstore.repository.UtenteRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("given_name");

        

        if (utenteRepository.findByEmail(email) == null) {
            Utente nuovoUtente = new Utente();
            nuovoUtente.setEmail(email);
            nuovoUtente.setNome(nome);
            nuovoUtente.setCognome("Da inserire");
            nuovoUtente.setUsername("utente_" + UUID.randomUUID().toString().substring(0, 8));
            nuovoUtente.setPassword("oauth"); // Password fittizia
            nuovoUtente.setRuolo(Ruolo.USER);
            utenteRepository.save(nuovoUtente);
        }

        return oAuth2User;
    }
}

