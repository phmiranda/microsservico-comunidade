package br.com.phmiranda.comunidade.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CursoRequest {

    @NotBlank
    @Length(max = 100)
    private String nome;

    @NotBlank
    @Length(max = 50)
    private String categoria;
}
