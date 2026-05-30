package br.com.phmiranda.comunidade.controller;

import br.com.phmiranda.comunidade.domain.dto.request.UsuarioRequest;
import br.com.phmiranda.comunidade.domain.dto.request.UsuarioUpdateRequest;
import br.com.phmiranda.comunidade.domain.dto.response.UsuarioResponse;
import br.com.phmiranda.comunidade.service.UsuarioService;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Cacheable(value = "listaDeUsuarios")
    public Page<UsuarioResponse> listar(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable paginacao
    ) {
        return usuarioService.listar(paginacao);
    }

    @PostMapping
    @CacheEvict(value = "listaDeUsuarios", allEntries = true)
    public ResponseEntity<UsuarioResponse> cadastrar(
        @RequestBody @Valid UsuarioRequest usuarioRequest,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        UsuarioResponse response = usuarioService.salvar(usuarioRequest);
        URI uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "listaDeUsuarios", allEntries = true)
    public ResponseEntity<UsuarioResponse> atualizar(
        @PathVariable Long id,
        @RequestBody @Valid UsuarioUpdateRequest usuarioUpdateRequest
    ) {
        return ResponseEntity.ok(usuarioService.atualizar(id, usuarioUpdateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.pesquisarPorId(id));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "listaDeUsuarios", allEntries = true)
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
