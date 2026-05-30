package br.com.phmiranda.comunidade.domain.dto.response;

import br.com.phmiranda.comunidade.domain.entity.Curso;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class CursoResponse {

    private Long id;
    private String nome;
    private String categoria;

    public CursoResponse(Curso curso) {
        this.id = curso.getId();
        this.nome = curso.getNome();
        this.categoria = curso.getCategoria();
    }

    public static Page<CursoResponse> converter(Page<Curso> cursos) {
        return cursos.map(CursoResponse::new);
    }
}
