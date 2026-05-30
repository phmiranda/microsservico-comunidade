package br.com.phmiranda.comunidade.domain.dto.request;

import br.com.phmiranda.comunidade.domain.enums.UsuarioStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class UsuarioUpdateRequest {

    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @CPF
    @NotBlank
    private String documento;

    private String senha;

    @NotNull
    private UsuarioStatus usuarioStatus;

    private List<Long> perfilIds;
}
