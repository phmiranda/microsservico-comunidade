package br.com.phmiranda.comunidade.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioRequest {

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @CPF
    @NotBlank
    private String documento;

    @NotBlank
    private String senha;
}
