package it.uniroma3.siw_techstore;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import it.uniroma3.siw_techstore.ENUM.Ruolo;
import it.uniroma3.siw_techstore.model.Categoria;
import it.uniroma3.siw_techstore.model.Ordine;
import it.uniroma3.siw_techstore.model.Prodotto;
import it.uniroma3.siw_techstore.model.Utente;
import it.uniroma3.siw_techstore.model.VoceOrdine;
import it.uniroma3.siw_techstore.repository.CategoriaRepository;
import it.uniroma3.siw_techstore.repository.OrdineRepository;
import it.uniroma3.siw_techstore.repository.ProdottoRepository;
import it.uniroma3.siw_techstore.repository.UtenteRepository;
import it.uniroma3.siw_techstore.repository.VoceOrdineRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@Rollback(false)
class TestRelazioniTraEntita {
	
	@Autowired
	private CategoriaRepository categoriaRepo;
	
	@Autowired
	private ProdottoRepository prodottoRepo;
	
	@Autowired
	private UtenteRepository utenteRepo;
	
	@Autowired
	private OrdineRepository ordineRepo;
	
	@Autowired
	private VoceOrdineRepository voceOrdineRepo;

	@Test
	void CategoriaProdottoMapping() {
		
		Categoria categoria = new Categoria();
		categoria.setNomeCategoria("CategoriaTest");
		categoriaRepo.save(categoria);
		
		Prodotto prodotto = new Prodotto();
		prodotto.setNomeProdotto("prodottoTest");
		prodotto.setDescrizione("descrizioneTest");
		prodotto.setPrezzo(BigDecimal.valueOf(1.00));
		prodotto.setCategoria(categoria);
		prodottoRepo.save(prodotto);
		
		Prodotto prodottoTrovato = prodottoRepo.findById(prodotto.getIdProdotto()).orElseThrow(()-> new AssertionError("Prodotto non trovato"));
		assertNotNull(prodottoTrovato.getCategoria());
		assertEquals("CategoriaTest", prodottoTrovato.getCategoria().getNomeCategoria(), "il nome della categoria associata non corrisponde");
	}
	
	@Test
	void test() {
		//
	}
	
	@Test
	void UtenteOrdineProdottoMapping() {
	    List<Ordine> ordini = new ArrayList<>();
	    List<VoceOrdine> vociOrdini = new ArrayList<>();
	    
	    // Crea e salva l'utente prima di creare l'ordine
	    Utente utente = new Utente();
	    utente.setNome("Guido");
	    utente.setCognome("Curradi");
	    utente.setUsername("gui98");
	    utente.setPassword("password");
	    utente.setRuolo(Ruolo.USER);
	    
	    utenteRepo.save(utente); // Salva l'utente prima di associare l'ordine
	    
	    Ordine ordine = new Ordine();
	    ordine.setUtente(utente);  // Ora l'utente è persistente
	    ordine.setTotale(BigDecimal.valueOf(100));
	    ordine.setIndirizzo("Via di prova");
	    ordine.setPaese("Italia");
	    ordine.setDataOrdine(LocalDateTime.now());
	    ordine.setCivico(12);
	    ordine.setCitta("Roma");
	    ordine.setCap(00135);
	    
	    ordineRepo.save(ordine); // Ora puoi salvare l'ordine
	    
	    Categoria categoria = new Categoria();
	    categoria.setNomeCategoria("CategoriaTest2");
	    categoriaRepo.save(categoria);
	    
	    Prodotto prodotto = new Prodotto();
	    prodotto.setNomeProdotto("prodottoTest2");
	    prodotto.setDescrizione("descrizioneTest2");
	    prodotto.setPrezzo(BigDecimal.valueOf(100));
	    prodotto.setCategoria(categoria);
	    prodottoRepo.save(prodotto);
	    
	    VoceOrdine voce1 = new VoceOrdine();
	    voce1.setPrezzo(BigDecimal.valueOf(100));
	    voce1.setProdotto(prodotto);
	    voce1.setQuantita(1);
	    voce1.setOrdine(ordine);  // Associa l'ordine alla voce
	    voceOrdineRepo.save(voce1);  // Salva la voce ordine
	    
	    vociOrdini.add(voce1);
	    ordine.setVociOrdini(vociOrdini);  // Aggiungi la voce all'ordine
	    ordineRepo.save(ordine);  // Salva di nuovo l'ordine con le voci associate
	    
	    ordini.add(ordine);
	    utente.setOrdini(ordini);  // Associa l'ordine all'utente
	    utenteRepo.save(utente);  // Salva l'utente di nuovo con l'ordine associato
	    
	    // Verifica che l'utente abbia l'ordine e la voce ordine
	    assertEquals(1, utente.getOrdini().size(), "deve avere 1 ordine");
	    Ordine ordineTrovato = utente.getOrdini().get(0);
	    assertEquals("Roma", ordineTrovato.getCitta(), "la citta deve essere Roma");
	    assertEquals("gui98", utente.getUsername(), "L'username deve essere gui98");
	    assertEquals(1, ordineTrovato.getVociOrdini().size(), "L'ordine deve contenere 1 voce");
	}



}
