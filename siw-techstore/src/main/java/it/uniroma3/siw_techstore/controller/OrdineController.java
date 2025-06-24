package it.uniroma3.siw_techstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import it.uniroma3.siw_techstore.model.Ordine;
import it.uniroma3.siw_techstore.model.VoceOrdine;
import it.uniroma3.siw_techstore.service.OrdineService;

@Controller
@RequestMapping("/admin/ordine")
public class OrdineController {
	
	@Autowired
	private OrdineService ordineService;
	
	@GetMapping("/visualizzaOrdini")
	public String mostraOrdini (Model model) {
		List<Ordine> ordini = ordineService.trovaTuttiGliOrdini();
		model.addAttribute("ordini", ordini);
		return "/admin/ordine/visualizzaOrdini";
	}
	
	@GetMapping("/dettaglioOrdine/{id}")
	public String mostraDettaglioOrdine(@PathVariable("id") Integer idOrdine, Model model) {
		Ordine ordine = ordineService.trovaOrdineConId(idOrdine).orElse(null);
		List<VoceOrdine> vociOrdine = ordine.getVociOrdini();
		model.addAttribute("ordine", ordine);
		model.addAttribute("vociOrdine", vociOrdine);
		return "/admin/ordine/visualizzaDettagliOrdini";
	}
	

}
