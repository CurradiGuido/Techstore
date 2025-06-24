package it.uniroma3.siw_techstore.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import it.uniroma3.siw_techstore.model.Categoria;
import it.uniroma3.siw_techstore.model.Prodotto;
import it.uniroma3.siw_techstore.service.CategoriaService;
import it.uniroma3.siw_techstore.service.ProdottoService;

@Controller
@RequestMapping("/shop")
public class ShopController {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private ProdottoService prodottoService;
	
	@GetMapping("/visualizzaShop")
    public String visualizzaShop(Model model) {
    	List<Categoria> categorie = categoriaService.trovaTutteLeCategorie();
    	List<Prodotto> prodotti = prodottoService.trovaTuttiIProdotti();
    	model.addAttribute("categorie", categorie);
    	model.addAttribute("prodotti", prodotti);
    	return "/user/shop";
    }
	
	@GetMapping("/prodotti")
	public String mostraTuttiIProdotti(Model model) {
		model.addAttribute("prodotti", prodottoService.trovaTuttiIProdotti());
		model.addAttribute("categorie", categoriaService.trovaTutteLeCategorie());
		return "/user/shop";
	}
	
	@GetMapping("/prodotti/categoria/{nome}")
	public String mostraProdottiPerCategoria(@PathVariable String nome, Model model) {
	    List<Prodotto> prodotti = prodottoService.findByCategoria(nome);
	    model.addAttribute("prodotti", prodotti);
	    model.addAttribute("categorie", categoriaService.trovaTutteLeCategorie());
	    return "/user/shop";
	}

}
