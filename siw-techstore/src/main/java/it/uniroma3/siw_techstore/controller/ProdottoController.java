package it.uniroma3.siw_techstore.controller;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw_techstore.DTO.ProdottoDTO;
import it.uniroma3.siw_techstore.Util.FileUtils;
import it.uniroma3.siw_techstore.model.Categoria;
import it.uniroma3.siw_techstore.model.Prodotto;
import it.uniroma3.siw_techstore.service.CategoriaService;
import it.uniroma3.siw_techstore.service.ProdottoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/prodotto")
public class ProdottoController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProdottoController.class);
	
	@Autowired
	ProdottoService prodottoService;

	@Autowired
	CategoriaService categoriaService;
	
	@GetMapping("/visualizzaProdotti")
	public String visualizzaProdotti(Model model) {
		List<Prodotto> prodotti = prodottoService.trovaTuttiIProdotti();
		model.addAttribute("prodotti", prodotti);
		return "/admin/prodotto/visualizzaProdotti";
	}
	
	@GetMapping("/create")
	public String mostraFormProdotto(Model model) {
		ProdottoDTO prodottoDTO = new ProdottoDTO();
		List<Categoria> categorie = categoriaService.trovaTutteLeCategorie();
		model.addAttribute("prodottoDTO", prodottoDTO);
		model.addAttribute("categorie", categorie);
		return "/admin/prodotto/aggiungiProdotto";
	}

	@PostMapping("/create")
	public String aggiungiNuovoProdotto(@Valid @ModelAttribute("prodottoDTO") ProdottoDTO prodottoDTO, BindingResult result, Model model) {
	
		if (prodottoService.checkProdotto(prodottoDTO)) {
			result.addError(new FieldError("prodottoDTO", "nomeProdotto", "Il prodotto è già esistente"));
		}

		if (prodottoService.checkImmagineProdotto(prodottoDTO)) {
			result.addError(new FieldError("prodottoDTO", "image", "Il file è obbligatorio"));
		}

		if (result.hasErrors()) {
			model.addAttribute("categorie", categoriaService.trovaTutteLeCategorie());
			return "/admin/prodotto/aggiungiProdotto"; // path corretto del form
		}

		try {
			
			String storageFilename = FileUtils.salvaImmagine(prodottoDTO.getImage(), "src/main/resources/static/images/prodotti/");		
			Categoria categoria = categoriaService.trovaCategoriaPerNome(prodottoDTO.getNomeCategoria()).orElse(null);
			Prodotto nuovoProdotto = prodottoService.creaProdotto(prodottoDTO, storageFilename, categoria);
			prodottoService.salvaProdotto(nuovoProdotto);
			
			logger.info("Il prodotto: " + nuovoProdotto.getNomeProdotto() + " è stato aggiunto con successo");
			
		}catch(Exception e) {
			logger.error("Errore in fase di aggiunta del prodotto: " + e.getMessage());
			return "/admin/prodotto/aggiungiProdotto";
		}

		return "redirect:/prodotto/visualizzaProdotti";
	}
	
	@GetMapping("/edit")
	public String mostraPaginaModifica(Model model, @RequestParam Integer id) {
		
		List<Categoria> categorie = categoriaService.trovaTutteLeCategorie();
		model.addAttribute("categorie", categorie);
		
		try {
			Prodotto prodotto = prodottoService.trovaProdottoPerId(id);
			model.addAttribute("prodotto", prodotto);
			
			ProdottoDTO prodottoDTO = prodottoService.creaProdottoDTO(prodotto);			
			model.addAttribute("prodottoDTO", prodottoDTO);
			
		}catch(Exception e) {
			logger.error("Errore in fase di caricamento della pagina per l'aggiornamento del prodotto");
			return "redirect:/prodotto/visualizzaProdotti";
		}
		
		return "/admin/prodotto/modificaProdotto";
		
	}
	
	@PostMapping("/edit")
	public String updateProdotto(Model model, @RequestParam Integer id, @Valid @ModelAttribute ProdottoDTO prodottoDTO, BindingResult result) {
		
		Categoria categoria = categoriaService.trovaCategoriaPerNome(prodottoDTO.getNomeCategoria()).orElse(null);
		
		if(prodottoService.checkImmagineProdotto(prodottoDTO)) {
			result.addError(new FieldError("prodottoDTO", "image", "il file è obbligatorio"));
		}
		
		if(result.hasErrors()) {
			return "/admin/prodotto/modificaProdotto";
		}
		
		try {
			
			Prodotto prodotto = prodottoService.trovaProdottoPerId(id);
			model.addAttribute("prodotto", prodotto);
			String uploadDir = "src/main/resources/static/images/prodotti/";
			String nuovaImmagine = null;
			
			if (prodottoDTO.getImage() != null && !prodottoDTO.getImage().isEmpty()) {
			    if (prodotto.getImage() != null && !prodotto.getImage().isEmpty()) {
			        FileUtils.eliminaImmagine(prodotto.getImage(), uploadDir);
			    }

			    nuovaImmagine = FileUtils.salvaImmagine(prodottoDTO.getImage(), uploadDir);
			}
			
			prodotto = prodottoService.aggiornaProdottoEsistente(prodotto, prodottoDTO, nuovaImmagine, categoria);			
			prodottoService.salvaProdotto(prodotto);
			logger.info("Il prodotto: " + prodotto.getNomeProdotto() + " è stato modificato con successo");
		}catch(Exception e) {
			logger.error("Errore in fase di modifica del prodotto: " + e.getMessage());
			return "prodotto/edit";
		}
		
		return "redirect:/prodotto/visualizzaProdotti";
	}

	@GetMapping("/delete")
	public String deleteProdotto(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
	    try {
	        prodottoService.eliminaProdottoConRisorse(id);
	        redirectAttributes.addFlashAttribute("successMessage", "Prodotto eliminato con successo.");
	        logger.info("Prodotto eliminato con successo");
	        return "redirect:/prodotto/visualizzaProdotti";

	    } catch (Exception e) {
	    	redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione del prodotto.");
			logger.error("Errore in fase di eliminazione del prodotto: " + e.getMessage());
	        return "redirect: /prodotto/visualizzaProdotti";
	    }
	}

}
