package br.com.phmiranda.comunidade.config.sqlite;

import br.com.phmiranda.comunidade.domain.entity.Curso;
import br.com.phmiranda.comunidade.domain.entity.Perfil;
import br.com.phmiranda.comunidade.domain.entity.Usuario;
import br.com.phmiranda.comunidade.domain.enums.UsuarioStatus;
import br.com.phmiranda.comunidade.repository.CursoRepository;
import br.com.phmiranda.comunidade.repository.PerfilRepository;
import br.com.phmiranda.comunidade.repository.UsuarioRepository;
import br.com.phmiranda.comunidade.service.PerfilService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

@Component
@Profile("sqlite")
public class SQLiteDataInitializer implements CommandLineRunner {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final PasswordEncoder passwordEncoder;

    public SQLiteDataInitializer(
        PerfilRepository perfilRepository,
        UsuarioRepository usuarioRepository,
        CursoRepository cursoRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Perfil admin = criarPerfilSeNaoExistir(PerfilService.PERFIL_ADMIN);
        Perfil aluno = criarPerfilSeNaoExistir(PerfilService.PERFIL_PADRAO);

        Usuario usuario = usuarioRepository.findByEmail("admin@email.com.br")
            .orElseGet(() -> new Usuario(
                "Administrador",
                "admin@email.com.br",
                "70436011000",
                passwordEncoder.encode("123456")
            ));

        usuario.setNome("Administrador");
        usuario.setSituacao(UsuarioStatus.ATIVO);
        usuario.setPerfis(new ArrayList<>(Arrays.asList(admin, aluno)));
        usuarioRepository.save(usuario);

        if (cursoRepository.count() == 0) {
            cursoRepository.save(new Curso("Java", "BACKEND"));
            cursoRepository.save(new Curso("Spring Boot", "BACKEND"));
            cursoRepository.save(new Curso("Angular", "FRONTEND"));
        }
    }

    private Perfil criarPerfilSeNaoExistir(String nome) {
        return perfilRepository.findByNome(nome)
            .orElseGet(() -> {
                Perfil perfil = new Perfil();
                perfil.setNome(nome);
                return perfilRepository.save(perfil);
            });
    }
}
