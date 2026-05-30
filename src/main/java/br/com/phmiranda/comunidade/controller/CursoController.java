package br.com.phmiranda.comunidade.controller;

import br.com.phmiranda.comunidade.domain.dto.request.CursoRequest;
import br.com.phmiranda.comunidade.domain.dto.response.CursoResponse;
import br.com.phmiranda.comunidade.service.CursoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    @Cacheable(value = "listaDeCursos")
    public Page<CursoResponse> listar(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable paginacao
    ) {
        return cursoService.listar(paginacao);
    }

    @PostMapping
    @CacheEvict(value = "listaDeCursos", allEntries = true)
    public ResponseEntity<CursoResponse> cadastrar(
        @RequestBody @Valid CursoRequest cursoRequest,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        CursoResponse response = cursoService.salvar(cursoRequest);
        URI uri = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "listaDeCursos", allEntries = true)
    public ResponseEntity<CursoResponse> atualizar(
        @PathVariable Long id,
        @RequestBody @Valid CursoRequest cursoRequest
    ) {
        return ResponseEntity.ok(cursoService.atualizar(id, cursoRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.pesquisarPorId(id));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "listaDeCursos", allEntries = true)
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtro/categoria")
    public Page<CursoResponse> pesquisarPorCategoria(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable paginacao,
        @RequestParam(required = false) String categoria
    ) {
        return cursoService.pesquisarPorCategoria(paginacao, categoria);
    }
}
