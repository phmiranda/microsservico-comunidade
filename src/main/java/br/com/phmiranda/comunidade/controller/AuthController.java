package br.com.phmiranda.comunidade.controller;

import br.com.phmiranda.comunidade.domain.dto.request.AuthRequest;
import br.com.phmiranda.comunidade.domain.dto.response.AuthResponse;
import br.com.phmiranda.comunidade.domain.entity.Usuario;
import br.com.phmiranda.comunidade.service.AuthTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthTokenService authTokenService;

    public AuthController(AuthenticationManager authenticationManager, AuthTokenService authTokenService) {
        this.authenticationManager = authenticationManager;
        this.authTokenService = authTokenService;
    }

    @PostMapping("/basica")
    public ResponseEntity<AuthResponse> autenticacaoBasica(@RequestBody @Valid AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(authRequest.converter());
        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = authTokenService.gerarToken(authentication);

        return ResponseEntity.ok(new AuthResponse(
            token,
            "Bearer",
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail()
        ));
    }
}
