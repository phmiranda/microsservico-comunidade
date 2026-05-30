package br.com.phmiranda.comunidade.service;

import br.com.phmiranda.comunidade.config.exception.BusinessException;
import br.com.phmiranda.comunidade.config.exception.ResourceNotFoundException;
import br.com.phmiranda.comunidade.domain.dto.request.PerfilRequest;
import br.com.phmiranda.comunidade.domain.dto.response.PerfilResponse;
import br.com.phmiranda.comunidade.domain.entity.Perfil;
import br.com.phmiranda.comunidade.repository.PerfilRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class PerfilService {

    public static final String PERFIL_ADMIN = "ROLE_ADMIN";
    public static final String PERFIL_PADRAO = "ROLE_ALUNO";
    private static final List<String> PERFIS_PERMITIDOS = Arrays.asList(PERFIL_ADMIN, PERFIL_PADRAO);

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Transactional(readOnly = true)
    public Page<PerfilResponse> listar(Pageable paginacao) {
        return PerfilResponse.converter(perfilRepository.findAll(paginacao));
    }

    @Transactional
    public PerfilResponse salvar(PerfilRequest request) {
        String nome = normalizarNome(request.getNome());
        validarPerfilPermitido(nome);
        if (perfilRepository.existsByNome(nome)) {
            throw new BusinessException("Já existe um perfil cadastrado com este nome.");
        }

        Perfil perfil = new Perfil();
        perfil.setNome(nome);
        perfilRepository.save(perfil);

        return new PerfilResponse(perfil);
    }

    @Transactional
    public PerfilResponse atualizar(Long id, PerfilRequest request) {
        Perfil perfil = buscarEntidade(id);
        String nome = normalizarNome(request.getNome());
        validarPerfilPermitido(nome);

        if (perfilRepository.existsByNomeAndIdNot(nome, id)) {
            throw new BusinessException("Já existe outro perfil cadastrado com este nome.");
        }

        perfil.setNome(nome);
        return new PerfilResponse(perfil);
    }

    @Transactional(readOnly = true)
    public PerfilResponse pesquisarPorId(Long id) {
        return new PerfilResponse(buscarEntidade(id));
    }

    @Transactional
    public void deletar(Long id) {
        Perfil perfil = buscarEntidade(id);
        perfilRepository.delete(perfil);
    }

    @Transactional(readOnly = true)
    public Perfil buscarEntidade(Long id) {
        return perfilRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil", id));
    }

    @Transactional(readOnly = true)
    public Perfil buscarPorNome(String nome) {
        return perfilRepository.findByNome(nome)
            .orElseThrow(() -> new ResourceNotFoundException("Perfil " + nome + " não encontrado."));
    }

    private String normalizarNome(String nome) {
        return nome.trim().toUpperCase();
    }

    private void validarPerfilPermitido(String nome) {
        if (!PERFIS_PERMITIDOS.contains(nome)) {
            throw new BusinessException("Perfil inválido. Perfis permitidos: ROLE_ADMIN e ROLE_ALUNO.");
        }
    }
}
