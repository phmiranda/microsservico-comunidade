package br.com.phmiranda.comunidade.domain.dto.response;

import br.com.phmiranda.comunidade.domain.entity.Perfil;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PerfilResponse {

    private Long id;
    private String nome;

    public PerfilResponse(Perfil perfil) {
        this.id = perfil.getId();
        this.nome = perfil.getNome();
    }

    public static Page<PerfilResponse> converter(Page<Perfil> perfis) {
        return perfis.map(PerfilResponse::new);
    }
}
