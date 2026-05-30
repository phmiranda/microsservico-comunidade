package br.com.phmiranda.comunidade.domain.dto.request;

import br.com.phmiranda.comunidade.domain.enums.DuvidaStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DuvidaUpdateRequest {

    @NotBlank
    @Length(max = 100)
    private String titulo;

    @NotBlank
    private String descricao;

    @NotNull
    private DuvidaStatus duvidaStatus;

    @NotNull
    private Long cursoId;
}
