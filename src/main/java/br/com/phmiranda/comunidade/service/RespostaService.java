package br.com.phmiranda.comunidade.service;

import br.com.phmiranda.comunidade.config.exception.ResourceNotFoundException;
import br.com.phmiranda.comunidade.domain.dto.request.RespostaRequest;
import br.com.phmiranda.comunidade.domain.dto.request.RespostaUpdateRequest;
import br.com.phmiranda.comunidade.domain.dto.response.RespostaResponse;
import br.com.phmiranda.comunidade.domain.entity.Duvida;
import br.com.phmiranda.comunidade.domain.entity.Resposta;
import br.com.phmiranda.comunidade.domain.entity.Usuario;
import br.com.phmiranda.comunidade.domain.enums.DuvidaStatus;
import br.com.phmiranda.comunidade.repository.RespostaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RespostaService {

    private final RespostaRepository respostaRepository;
    private final DuvidaService duvidaService;

    public RespostaService(RespostaRepository respostaRepository, DuvidaService duvidaService) {
        this.respostaRepository = respostaRepository;
        this.duvidaService = duvidaService;
    }

    @Transactional(readOnly = true)
    public Page<RespostaResponse> listar(Pageable paginacao, Long duvidaId) {
        Page<Resposta> respostas = duvidaId == null
            ? respostaRepository.findAll(paginacao)
            : respostaRepository.findByDuvidaId(paginacao, duvidaId);
        return RespostaResponse.converter(respostas);
    }

    @Transactional
    public RespostaResponse salvar(RespostaRequest request) {
        Duvida duvida = duvidaService.buscarEntidade(request.getDuvidaId());
        Usuario usuario = getUsuarioAutenticado();

        Resposta resposta = new Resposta(request.getDescricao(), usuario, duvida);
        respostaRepository.save(resposta);

        if (DuvidaStatus.NAO_RESPONDIDO.equals(duvida.getDuvidaStatus())) {
            duvida.setDuvidaStatus(DuvidaStatus.NAO_SOLUCIONADO);
        }

        return new RespostaResponse(resposta);
    }

    @Transactional
    public RespostaResponse atualizar(Long id, RespostaUpdateRequest request) {
        Resposta resposta = buscarEntidade(id);
        validarPermissaoDeAlteracao(resposta.getUsuario());
        resposta.setDescricao(request.getDescricao());

        if (request.getSolucao() != null) {
            resposta.setSolucao(request.getSolucao());
            resposta.getDuvida().setDuvidaStatus(Boolean.TRUE.equals(request.getSolucao())
                ? DuvidaStatus.SOLUCIONADO
                : DuvidaStatus.NAO_SOLUCIONADO);
        }

        return new RespostaResponse(resposta);
    }

    @Transactional(readOnly = true)
    public RespostaResponse pesquisarPorId(Long id) {
        return new RespostaResponse(buscarEntidade(id));
    }

    @Transactional
    public void deletar(Long id) {
        Resposta resposta = buscarEntidade(id);
        validarPermissaoDeAlteracao(resposta.getUsuario());
        respostaRepository.delete(resposta);
    }

    private Resposta buscarEntidade(Long id) {
        return respostaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Resposta", id));
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
