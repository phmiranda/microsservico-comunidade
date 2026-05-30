package br.com.phmiranda.comunidade.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WebController {

    @GetMapping("/olamundo")
    public String olaMundo(@RequestParam(required = false, defaultValue = "Spring Boot") String nome) {
        return "Bem-vindo ao curso de " + nome + ".";
    }
}
