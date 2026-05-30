package br.com.phmiranda.comunidade.service;

import br.com.phmiranda.comunidade.config.exception.ResourceNotFoundException;
import br.com.phmiranda.comunidade.domain.dto.request.DuvidaRequest;
import br.com.phmiranda.comunidade.domain.dto.request.DuvidaUpdateRequest;
import br.com.phmiranda.comunidade.domain.dto.response.DuvidaDetalharResponse;
import br.com.phmiranda.comunidade.domain.dto.response.DuvidaResponse;
import br.com.phmiranda.comunidade.domain.entity.Curso;
import br.com.phmiranda.comunidade.domain.entity.Duvida;
import br.com.phmiranda.comunidade.domain.entity.Usuario;
import br.com.phmiranda.comunidade.repository.DuvidaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DuvidaService {

    private final DuvidaRepository duvidaRepository;
    private final CursoService cursoService;

    public DuvidaService(DuvidaRepository duvidaRepository, CursoService cursoService) {
        this.duvidaRepository = duvidaRepository;
        this.cursoService = cursoService;
    }

    @Transactional(readOnly = true)
    public Page<DuvidaResponse> listar(Pageable paginacao, Long cursoId, Long usuarioId) {
        if (cursoId != null) {
            return DuvidaResponse.converter(duvidaRepository.findByCursoId(paginacao, cursoId));
        }
        if (usuarioId != null) {
            return DuvidaResponse.converter(duvidaRepository.findByUsuarioId(paginacao, usuarioId));
        }
        return DuvidaResponse.converter(duvidaRepository.findAll(paginacao));
    }

    @Transactional
    public DuvidaResponse salvar(DuvidaRequest request) {
        Curso curso = cursoService.buscarEntidade(request.getCursoId());
        Usuario usuario = getUsuarioAutenticado();
        Duvida duvida = new Duvida(request.getTitulo(), request.getDescricao(), curso, usuario);
        duvidaRepository.save(duvida);
        return new DuvidaResponse(duvida);
    }

    @Transactional
    public DuvidaResponse atualizar(Long id, DuvidaUpdateRequest request) {
        Duvida duvida = buscarEntidade(id);
        validarPermissaoDeAlteracao(duvida.getUsuario());
        Curso curso = cursoService.buscarEntidade(request.getCursoId());

        duvida.setTitulo(request.getTitulo());
        duvida.setDescricao(request.getDescricao());
        duvida.setDuvidaStatus(request.getDuvidaStatus());
        duvida.setCurso(curso);

        return new DuvidaResponse(duvida);
    }

    @Transactional(readOnly = true)
    public DuvidaDetalharResponse pesquisarPorId(Long id) {
        return new DuvidaDetalharResponse(buscarEntidade(id));
    }

    @Transactional
    public void deletar(Long id) {
        Duvida duvida = buscarEntidade(id);
        validarPermissaoDeAlteracao(duvida.getUsuario());
        duvidaRepository.delete(duvida);
    }

    @Transactional(readOnly = true)
    public Duvida buscarEntidade(Long id) {
        return duvidaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dúvida", id));
    }

    private Usuario getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Usuario) {
            return (Usuario) principal;
        }
        throw new ResourceNotFoundException("Usuário autenticado não encontrado.");
    }

    private void validarPermissaoDeAlteracao(Usuario usuarioDono) {
        Usuario usuarioAutenticado = getUsuarioAutenticado();
        if (usuarioAutenticado.getId().equals(usuarioDono.getId()) || usuarioAutenticadoPossuiPerfil(PerfilService.PERFIL_ADMIN)) {
            return;
        }
        throw new AccessDeniedException("Usuário não possui permissão para alterar este recurso.");
    }

    private boolean usuarioAutenticadoPossuiPerfil(String perfil) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(perfil::equals);
    }
}
