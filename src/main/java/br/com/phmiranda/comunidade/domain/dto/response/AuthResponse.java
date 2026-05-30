package br.com.phmiranda.comunidade.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String tipo;
    private Long usuarioId;
    private String nome;
    private String email;
}
