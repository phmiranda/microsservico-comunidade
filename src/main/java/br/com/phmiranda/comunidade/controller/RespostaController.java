package br.com.phmiranda.comunidade.controller;

import br.com.phmiranda.comunidade.domain.dto.request.RespostaRequest;
import br.com.phmiranda.comunidade.domain.dto.request.RespostaUpdateRequest;
import br.com.phmiranda.comunidade.domain.dto.response.RespostaResponse;
import br.com.phmiranda.comunidade.service.RespostaService;
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
@RequestMapping("/respostas")
public class RespostaController {

    private final RespostaService respostaService;

    public RespostaController(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    @GetMapping
    @Cacheable(value = "listaDeRespostas")
    public Page<RespostaResponse> listar(
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable paginacao,
        @RequestParam(required = false) Long duvidaId
    ) {
        return respostaService.listar(paginacao, duvidaId);
    }

    @PostMapping
    @CacheEvict(value = {"listaDeRespostas", "listaDeDuvidas"}, allEntries = true)
    public ResponseEntity<RespostaResponse> cadastrar(
        @RequestBody @Valid RespostaRequest respostaRequest,
        UriComponentsBuilder uriComponentsBuilder
    ) {
        RespostaResponse response = respostaService.salvar(respostaRequest);
        URI uri = uriComponentsBuilder.path("/respostas/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = {"listaDeRespostas", "listaDeDuvidas"}, allEntries = true)
    public ResponseEntity<RespostaResponse> atualizar(
        @PathVariable Long id,
        @RequestBody @Valid RespostaUpdateRequest respostaUpdateRequest
    ) {
        return ResponseEntity.ok(respostaService.atualizar(id, respostaUpdateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespostaResponse> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(respostaService.pesquisarPorId(id));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = {"listaDeRespostas", "listaDeDuvidas"}, allEntries = true)
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        respostaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
