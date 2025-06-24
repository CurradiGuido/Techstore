package it.uniroma3.siw_techstore.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import it.uniroma3.siw_techstore.DTO.CategoriaDTO;
import it.uniroma3.siw_techstore.Util.FileUtils;
import it.uniroma3.siw_techstore.model.Categoria;
import it.uniroma3.siw_techstore.model.Prodotto;
import it.uniroma3.siw_techstore.service.CategoriaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categoria")
public class CategoriaController {
	
	private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);
	
	@Autowired
	private CategoriaService categoriaService;

	// GESTIONE DELLE CATEGORIE
	@GetMapping("visualizzaCategorie")
	public String visualizzaCategorie(Model model) {
		List<Categoria> categorie = categoriaService.trovaTutteLeCategorie();
		model.addAttribute("categorie", categorie);
		return "/admin/categoria/visualizzaCategorie";
	}

	@GetMapping("/create")
	public String mostraFormCategoria(Model model) {
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		model.addAttribute("categoriaDTO", categoriaDTO);
		return "/admin/categoria/aggiungiCategoria";
	}

	@PostMapping("/create")
	public String aggiungiCategoria(Model model, @Valid @ModelAttribute CategoriaDTO categoriaDTO, BindingResult result)
			throws IOException {

		Optional<Categoria> categoria = categoriaService.trovaCategoriaPerNome(categoriaDTO.getNomeCategoria());
		if (categoria.isPresent()) {
			result.addError(new FieldError("categoriaDTO", "nomeCategoria", "La categoria già esiste"));
		}

		if (categoriaDTO.getImage().isEmpty()) {
			result.addError(new FieldError("categoriaDTO", "image", "Il file è obbligatorio"));
		}

		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> System.out.println(error.toString()));
			System.out.println("C'è qualche problema qui");
			return "/admin/categoria/aggiungiCategoria";
		}

		try {
			List<Prodotto> prodotti = new ArrayList<>();
			String imageName = FileUtils.salvaImmagine(categoriaDTO.getImage(), "src/main/resources/static/images/categorie/");
			Categoria nuovaCategoria = categoriaService.creaCategoria(categoriaDTO, imageName, prodotti);
			categoriaService.salvaCategoria(nuovaCategoria);
			logger.info("Categoria: " + nuovaCategoria.getNomeCategoria() + " creata con successo");
		}catch(Exception e) {
			logger.error("Errore in fase di creazione di una categoria:  " + e.getMessage());
			return "/admin/aggiungiCategoria";
		}

		return "redirect:/admin/categoria/visualizzaCategorie";
	}

	@GetMapping("/edit")
	public String mostraPaginaModifica(Model model, @RequestParam Integer id) {

		try {

			Categoria categoria = categoriaService.trovaCategoriaPerId(id);
			model.addAttribute("categoria", categoria);
			CategoriaDTO categoriaDTO = categoriaService.creaCategoriaDTO(categoria);
			model.addAttribute("categoriaDTO", categoriaDTO);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Errore in fase di visualizzazione della pagina di edit della categoria: " + e.getMessage());
			return "redirect:/admin/categoria/visualizzaCategorie";
		}

		return "/admin/categoria/modificaCategoria";
	}

	@PostMapping("/edit")
	public String updateCategoria(Model model, @RequestParam Integer id,
			@Valid @ModelAttribute CategoriaDTO categoriaDTO, BindingResult result) {

		Categoria categoria = categoriaService.trovaCategoriaPerId(id);
		model.addAttribute("categoria", categoria);

		if (categoriaService.checkImage(categoriaDTO)) {
			result.addError(new FieldError("CategoriaDTO", "image", "il file è obbligatorio"));
		}

		if (result.hasErrors()) {
		    System.out.println("Model attributes: " + model.asMap());
		    return "/admin/categoria/modificaCategoria";
		}

		try {
			String uploadDir = "src/main/resources/static/images/categorie/";
			String nuovaImmagine = null;

			if (categoriaDTO.getImage() != null && !categoriaDTO.getImage().isEmpty()) {
				if (categoria.getImage() != null && !categoria.getImage().isEmpty()) {
					FileUtils.eliminaImmagine(categoria.getImage(), uploadDir);
				}

				nuovaImmagine = FileUtils.salvaImmagine(categoriaDTO.getImage(), uploadDir);
			}

			categoria = categoriaService.aggiornaCategoriaEsistente(categoria, categoriaDTO, nuovaImmagine);
			categoriaService.salvaCategoria(categoria);
			logger.info("Categoria: " + categoria.getNomeCategoria() + " modificata con successo");
		} catch (Exception e) {
			model.addAttribute("categoria", categoria);
			logger.error("Errore in fase di aggiornamento della categoria");
			return "redirect:/admin/categoria/edit";
		}

		return "redirect:/admin/categoria/visualizzaCategorie";
	}

	@GetMapping("/delete")
	public String deleteCategoria(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
		try {
			categoriaService.eliminaCategoriaConRisorse(id);
			redirectAttributes.addFlashAttribute("successMessage", "Categoria eliminata con successo.");
			logger.info("Categoria eliminata con successo");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Errore durante l'eliminazione della categoria.");
			logger.error("Errore durante l'eliminazione della categoria");
		}
		return "redirect:/admin/categoria/visualizzaCategorie";
	}
}
