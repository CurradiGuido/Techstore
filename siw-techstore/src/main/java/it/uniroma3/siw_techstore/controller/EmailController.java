package it.uniroma3.siw_techstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw_techstore.service.EmailService;

@Controller
public class EmailController {
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping("/send-message")
	public String sendMessage(@RequestParam String name, @RequestParam String email, @RequestParam String message, RedirectAttributes redirectAttributes) {
		emailService.sendEmail(name, email, message);
		redirectAttributes.addFlashAttribute("success", "Messaggio inviato con successo");
		return "redirect:/contatti";
	}

}
