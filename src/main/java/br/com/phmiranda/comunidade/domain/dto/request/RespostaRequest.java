package br.com.phmiranda.comunidade.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RespostaRequest {

    @NotBlank
    private String descricao;

    @NotNull
    private Long duvidaId;
}
