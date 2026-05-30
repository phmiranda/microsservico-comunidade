package br.com.phmiranda.comunidade.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DuvidaRequest {

    @NotBlank
    @Length(max = 100)
    private String titulo;

    @NotBlank
    private String descricao;

    @NotNull
    private Long cursoId;
}
