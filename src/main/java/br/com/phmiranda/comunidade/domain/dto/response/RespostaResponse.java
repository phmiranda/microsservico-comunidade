package br.com.phmiranda.comunidade.domain.dto.response;

import br.com.phmiranda.comunidade.domain.entity.Resposta;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
public class RespostaResponse {

    private Long id;
    private String descricao;
    private LocalDateTime dataCriacao;
    private Boolean solucao;
    private Long usuarioId;
    private String usuario;
    private Long duvidaId;
    private String duvida;

    public RespostaResponse(Resposta resposta) {
        this.id = resposta.getId();
        this.descricao = resposta.getDescricao();
        this.dataCriacao = resposta.getDataCriacao();
        this.solucao = resposta.getSolucao();
        this.usuarioId = resposta.getUsuario().getId();
        this.usuario = resposta.getUsuario().getNome();
        this.duvidaId = resposta.getDuvida().getId();
        this.duvida = resposta.getDuvida().getTitulo();
    }

    public static Page<RespostaResponse> converter(Page<Resposta> respostas) {
        return respostas.map(RespostaResponse::new);
    }
}
