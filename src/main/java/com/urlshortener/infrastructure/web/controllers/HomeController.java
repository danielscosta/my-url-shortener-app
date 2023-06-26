package com.urlshortener.infrastructure.web.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @Value("${DOMAIN:http://localhost:8080}")
    private String domain;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getExampleHTML(Model model) {
        model.addAttribute("domain", this.domain);
        return "index.html";
    }
}
