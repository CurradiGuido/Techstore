package it.uniroma3.siw_techstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw_techstore.service.CarrelloService;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CarrelloService carrelloService;

    @ModelAttribute("numeroProdottiCarrello")
    public int numeroProdottiNelCarrello() {
        return carrelloService.getNumeroTotaleProdotti();
    }
}
