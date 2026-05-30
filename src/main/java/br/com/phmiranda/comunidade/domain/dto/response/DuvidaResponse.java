package br.com.phmiranda.comunidade.domain.dto.response;

import br.com.phmiranda.comunidade.domain.entity.Duvida;
import br.com.phmiranda.comunidade.domain.enums.DuvidaStatus;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
public class DuvidaResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private DuvidaStatus duvidaStatus;
    private LocalDateTime dataCriacao;
    private Long cursoId;
    private String curso;
    private Long usuarioId;
    private String usuario;

    public DuvidaResponse(Duvida duvida) {
        this.id = duvida.getId();
        this.titulo = duvida.getTitulo();
        this.descricao = duvida.getDescricao();
        this.duvidaStatus = duvida.getDuvidaStatus();
        this.dataCriacao = duvida.getDataCriacao();
        this.cursoId = duvida.getCurso().getId();
        this.curso = duvida.getCurso().getNome();
        this.usuarioId = duvida.getUsuario().getId();
        this.usuario = duvida.getUsuario().getNome();
    }

    public static Page<DuvidaResponse> converter(Page<Duvida> duvidas) {
        return duvidas.map(DuvidaResponse::new);
    }
}
