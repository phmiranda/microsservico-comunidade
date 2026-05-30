package br.com.phmiranda.comunidade.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PerfilRequest {

    @NotBlank
    @Length(max = 30)
    private String nome;
}
