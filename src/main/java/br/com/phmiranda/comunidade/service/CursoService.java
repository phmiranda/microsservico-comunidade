package br.com.phmiranda.comunidade.service;

import br.com.phmiranda.comunidade.config.exception.ResourceNotFoundException;
import br.com.phmiranda.comunidade.domain.dto.request.CursoRequest;
import br.com.phmiranda.comunidade.domain.dto.response.CursoResponse;
import br.com.phmiranda.comunidade.domain.entity.Curso;
import br.com.phmiranda.comunidade.repository.CursoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Transactional(readOnly = true)
    public Page<CursoResponse> listar(Pageable paginacao) {
        return CursoResponse.converter(cursoRepository.findAll(paginacao));
    }

    @Transactional
    public CursoResponse salvar(CursoRequest request) {
        Curso curso = new Curso(request.getNome(), request.getCategoria());
        cursoRepository.save(curso);
        return new CursoResponse(curso);
    }

    @Transactional
    public CursoResponse atualizar(Long id, CursoRequest request) {
        Curso curso = buscarEntidade(id);
        curso.setNome(request.getNome());
        curso.setCategoria(request.getCategoria());
        return new CursoResponse(curso);
    }

    @Transactional(readOnly = true)
    public CursoResponse pesquisarPorId(Long id) {
        return new CursoResponse(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<CursoResponse> pesquisarPorCategoria(Pageable paginacao, String categoria) {
        Page<Curso> cursos = categoria == null || categoria.trim().isEmpty()
            ? cursoRepository.findAll(paginacao)
            : cursoRepository.findByCategoriaContainingIgnoreCase(paginacao, categoria);
        return CursoResponse.converter(cursos);
    }

    @Transactional
    public void deletar(Long id) {
        Curso curso = buscarEntidade(id);
        cursoRepository.delete(curso);
    }

    @Transactional(readOnly = true)
    public Curso buscarEntidade(Long id) {
        return cursoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Curso", id));
    }
}
