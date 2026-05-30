package br.com.phmiranda.comunidade.domain.dto.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldErrorResponse {

    private String field;
    private String message;
}
