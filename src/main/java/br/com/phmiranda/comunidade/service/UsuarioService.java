package br.com.phmiranda.comunidade.service;

import br.com.phmiranda.comunidade.config.exception.BusinessException;
import br.com.phmiranda.comunidade.config.exception.ResourceNotFoundException;
import br.com.phmiranda.comunidade.domain.dto.request.UsuarioRequest;
import br.com.phmiranda.comunidade.domain.dto.request.UsuarioUpdateRequest;
import br.com.phmiranda.comunidade.domain.dto.response.UsuarioResponse;
import br.com.phmiranda.comunidade.domain.entity.Perfil;
import br.com.phmiranda.comunidade.domain.entity.Usuario;
import br.com.phmiranda.comunidade.repository.PerfilRepository;
import br.com.phmiranda.comunidade.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
        UsuarioRepository usuarioRepository,
        PerfilRepository perfilRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listar(Pageable paginacao) {
        return UsuarioResponse.converter(usuarioRepository.findAll(paginacao));
    }

    @Transactional
    public UsuarioResponse salvar(UsuarioRequest request) {
        validarNovoUsuario(request.getEmail(), request.getDocumento());

        Usuario usuario = new Usuario(
            request.getNome(),
            request.getEmail(),
            request.getDocumento(),
            passwordEncoder.encode(request.getSenha())
        );
        usuario.setPerfis(Collections.singletonList(buscarPerfilPadrao()));

        usuarioRepository.save(usuario);
        return new UsuarioResponse(usuario);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = buscarEntidade(id);
        validarUsuarioExistente(id, request.getEmail(), request.getDocumento());

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setDocumento(request.getDocumento());
        usuario.setSituacao(request.getUsuarioStatus());
        usuario.setPerfis(buscarPerfis(request.getPerfilIds()));

        if (request.getSenha() != null && !request.getSenha().trim().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        return new UsuarioResponse(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse pesquisarPorId(Long id) {
        return new UsuarioResponse(buscarEntidade(id));
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = buscarEntidade(id);
        usuarioRepository.delete(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário", id));
    }

    private void validarNovoUsuario(String email, String documento) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("Já existe um usuário cadastrado com este e-mail.");
        }
        if (usuarioRepository.existsByDocumento(documento)) {
            throw new BusinessException("Já existe um usuário cadastrado com este documento.");
        }
    }

    private void validarUsuarioExistente(Long id, String email, String documento) {
        if (usuarioRepository.existsByEmailAndIdNot(email, id)) {
            throw new BusinessException("Já existe outro usuário cadastrado com este e-mail.");
        }
        if (usuarioRepository.existsByDocumentoAndIdNot(documento, id)) {
            throw new BusinessException("Já existe outro usuário cadastrado com este documento.");
        }
    }

    private List<Perfil> buscarPerfis(List<Long> perfilIds) {
        if (perfilIds == null || perfilIds.isEmpty()) {
            return Collections.singletonList(buscarPerfilPadrao());
        }

        List<Perfil> perfis = perfilRepository.findAllById(perfilIds);
        if (perfis.size() != perfilIds.stream().distinct().count()) {
            throw new ResourceNotFoundException("Um ou mais perfis informados não foram encontrados.");
        }
        return perfis;
    }

    private Perfil buscarPerfilPadrao() {
        return perfilRepository.findByNome(PerfilService.PERFIL_PADRAO)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil padrão não encontrado."));
    }
}
