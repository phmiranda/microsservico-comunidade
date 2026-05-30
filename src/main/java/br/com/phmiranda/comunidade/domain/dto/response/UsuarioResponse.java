package br.com.phmiranda.comunidade.domain.dto.response;

import br.com.phmiranda.comunidade.domain.entity.Usuario;
import br.com.phmiranda.comunidade.domain.enums.UsuarioStatus;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private String documento;
    private UsuarioStatus usuarioStatus;
    private List<String> perfis;

    public UsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.documento = usuario.getDocumento();
        this.usuarioStatus = usuario.getSituacao();
        this.perfis = usuario.getPerfis().stream()
            .map(perfil -> perfil.getNome())
            .collect(Collectors.toList());
    }

    public static Page<UsuarioResponse> converter(Page<Usuario> usuarios) {
        return usuarios.map(UsuarioResponse::new);
    }
}
