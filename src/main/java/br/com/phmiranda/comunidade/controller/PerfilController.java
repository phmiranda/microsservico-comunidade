package br.com.phmiranda.comunidade.controller;

import br.com.phmiranda.comunidade.domain.dto.request.PerfilRequest;
import br.com.phmiranda.comunidade.domain.dto.response.PerfilResponse;
import br.com.phmiranda.comunidade.service.PerfilService;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping
    @Cacheable(value = "listaDePerfis")
    public Page<PerfilResponse> listar(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable paginacao
    ) {
        return perfilService.listar(paginacao);
    }

    @PostMapping
    @CacheEvict(value = "listaDePerfis", allEntries = true)
    public ResponseEntity<PerfilResponse> cadastrar(
        @RequestBody @Valid PerfilRequest perfilRequest,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        PerfilResponse response = perfilService.salvar(perfilRequest);
        URI uri = uriComponentsBuilder.path("/perfis/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "listaDePerfis", allEntries = true)
    public ResponseEntity<PerfilResponse> atualizar(
        @PathVariable Long id,
        @RequestBody @Valid PerfilRequest perfilRequest
    ) {
        return ResponseEntity.ok(perfilService.atualizar(id, perfilRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponse> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(perfilService.pesquisarPorId(id));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "listaDePerfis", allEntries = true)
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        perfilService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
