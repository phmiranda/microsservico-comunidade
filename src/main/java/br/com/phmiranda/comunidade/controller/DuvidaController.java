package br.com.phmiranda.comunidade.controller;

import br.com.phmiranda.comunidade.domain.dto.request.DuvidaRequest;
import br.com.phmiranda.comunidade.domain.dto.request.DuvidaUpdateRequest;
import br.com.phmiranda.comunidade.domain.dto.response.DuvidaDetalharResponse;
import br.com.phmiranda.comunidade.domain.dto.response.DuvidaResponse;
import br.com.phmiranda.comunidade.service.DuvidaService;
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
@RequestMapping("/duvidas")
public class DuvidaController {

    private final DuvidaService duvidaService;

    public DuvidaController(DuvidaService duvidaService) {
        this.duvidaService = duvidaService;
    }

    @GetMapping
    @Cacheable(value = "listaDeDuvidas")
    public Page<DuvidaResponse> listar(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable paginacao,
        @RequestParam(required = false) Long cursoId,
        @RequestParam(required = false) Long usuarioId
    ) {
        return duvidaService.listar(paginacao, cursoId, usuarioId);
    }

    @PostMapping
    @CacheEvict(value = "listaDeDuvidas", allEntries = true)
    public ResponseEntity<DuvidaResponse> cadastrar(
        @RequestBody @Valid DuvidaRequest duvidaRequest,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        DuvidaResponse response = duvidaService.salvar(duvidaRequest);
        URI uri = uriComponentsBuilder.path("/duvidas/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "listaDeDuvidas", allEntries = true)
    public ResponseEntity<DuvidaResponse> atualizar(
        @PathVariable Long id,
        @RequestBody @Valid DuvidaUpdateRequest duvidaUpdateRequest
    ) {
        return ResponseEntity.ok(duvidaService.atualizar(id, duvidaUpdateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DuvidaDetalharResponse> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(duvidaService.pesquisarPorId(id));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "listaDeDuvidas", allEntries = true)
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        duvidaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
