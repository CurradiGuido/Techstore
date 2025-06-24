package it.uniroma3.siw_techstore.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw_techstore.handler.CustomAuthenticationFailureHandler;
import it.uniroma3.siw_techstore.handler.CustomAuthenticationSuccessHandler;
import it.uniroma3.siw_techstore.handler.CustomLogoutSuccessHandler;
import it.uniroma3.siw_techstore.handler.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
    private CustomOAuth2UserService oAuth2UserService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(new BCryptPasswordEncoder())
				.authoritiesByUsernameQuery("SELECT username, ruolo FROM utente WHERE username=?")
				.usersByUsernameQuery("SELECT username, pswd, 1 as enabled FROM utente WHERE username=?");
	}

	@ModelAttribute("userDetails")
	public UserDetails getUser() {
		UserDetails user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}

		return user;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http.csrf(csrf -> csrf.disable())

				// Autorizzazioni
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.GET, "/", "/utente/register", "/login", "/contatti", "/offerte",
								"/css/**", "/images/**", "/assets/**")
						.permitAll().requestMatchers(HttpMethod.POST, "/utente/register", "/login").permitAll()
						.requestMatchers(HttpMethod.GET, "/admin/**").hasAuthority("ADMIN")
						.requestMatchers(HttpMethod.POST, "/admin/**").hasAuthority("ADMIN").anyRequest()
						.authenticated())

				.formLogin(form -> form.loginPage("/user/login").permitAll().defaultSuccessUrl("/", true)
						.successHandler(new CustomAuthenticationSuccessHandler())
						.failureHandler(new CustomAuthenticationFailureHandler()))
				.oauth2Login(oauth -> oauth
						.loginPage("/login")
						.userInfoEndpoint(user -> user
								.userService(oAuth2UserService))
								.defaultSuccessUrl("/oauth/complete", true)
				)
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(new CustomLogoutSuccessHandler())
						.logoutSuccessUrl("/?logout=true").invalidateHttpSession(true).deleteCookies("JSESSIONID")
						.clearAuthentication(true)

						.permitAll())
				.build();
	}

}
