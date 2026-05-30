package br.com.phmiranda.comunidade.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RespostaUpdateRequest {

    @NotBlank
    private String descricao;

    private Boolean solucao;
}
