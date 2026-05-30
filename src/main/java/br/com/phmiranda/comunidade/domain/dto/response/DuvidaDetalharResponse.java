package br.com.phmiranda.comunidade.domain.dto.response;

import br.com.phmiranda.comunidade.domain.entity.Duvida;
import br.com.phmiranda.comunidade.domain.enums.DuvidaStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DuvidaDetalharResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private Long cursoId;
    private String curso;
    private Long usuarioId;
    private String usuario;
    private DuvidaStatus duvidaStatus;
    private LocalDateTime dataCriacao;
    private List<RespostaResponse> respostas;

    public DuvidaDetalharResponse(Duvida duvida) {
        this.id = duvida.getId();
        this.titulo = duvida.getTitulo();
        this.descricao = duvida.getDescricao();
        this.cursoId = duvida.getCurso().getId();
        this.curso = duvida.getCurso().getNome();
        this.usuarioId = duvida.getUsuario().getId();
        this.usuario = duvida.getUsuario().getNome();
        this.duvidaStatus = duvida.getDuvidaStatus();
        this.dataCriacao = duvida.getDataCriacao();
        this.respostas = duvida.getRespostas().stream()
            .map(RespostaResponse::new)
            .collect(Collectors.toList());
    }
}
