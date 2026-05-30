package br.com.phmiranda.comunidade.controller;

import br.com.phmiranda.comunidade.domain.entity.Curso;
import br.com.phmiranda.comunidade.domain.entity.Duvida;
import br.com.phmiranda.comunidade.domain.entity.Perfil;
import br.com.phmiranda.comunidade.domain.entity.Resposta;
import br.com.phmiranda.comunidade.domain.entity.Usuario;
import br.com.phmiranda.comunidade.repository.CursoRepository;
import br.com.phmiranda.comunidade.repository.DuvidaRepository;
import br.com.phmiranda.comunidade.repository.PerfilRepository;
import br.com.phmiranda.comunidade.repository.RespostaRepository;
import br.com.phmiranda.comunidade.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ApiFunctionalTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DuvidaRepository duvidaRepository;

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    private Perfil perfilAdmin;
    private Perfil perfilAluno;
    private Curso curso;
    private Usuario admin;
    private Usuario usuario;
    private Usuario outroUsuario;

    @Before
    public void setUp() {
        respostaRepository.deleteAll();
        duvidaRepository.deleteAll();
        cursoRepository.deleteAll();
        usuarioRepository.deleteAll();
        perfilRepository.deleteAll();

        perfilAdmin = salvarPerfil("ROLE_ADMIN");
        perfilAluno = salvarPerfil("ROLE_ALUNO");
        curso = cursoRepository.save(new Curso("Java", "BACKEND"));
        admin = salvarUsuario("Admin", "admin@test.com", "70436011000", perfilAdmin, perfilAluno);
        usuario = salvarUsuario("Usuario", "usuario@test.com", "60172076013", perfilAluno);
        outroUsuario = salvarUsuario("Outro Usuario", "outro@test.com", "33721917065", perfilAluno);
    }

    @Test
    public void devePermitirConsultasPublicas() throws Exception {
        mockMvc.perform(get("/cursos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].nome", is("Java")));

        mockMvc.perform(get("/cursos/filtro/categoria").param("categoria", "back"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].categoria", is("BACKEND")));

        mockMvc.perform(get("/duvidas"))
            .andExpect(status().isOk());
    }

    @Test
    public void devePermitirAcessoPublicoADocumentacaoSwagger() throws Exception {
        mockMvc.perform(get("/v2/api-docs"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.swagger", is("2.0")))
            .andExpect(jsonPath("$.info.title", is("comunidade-test")));
    }

    @Test
    public void deveRestringirRecursosAdministrativos() throws Exception {
        String adminToken = autenticar(admin.getEmail());
        String userToken = autenticar(usuario.getEmail());

        mockMvc.perform(get("/perfis")
                .header("Authorization", bearer(adminToken)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/perfis")
                .header("Authorization", bearer(userToken)))
            .andExpect(status().isForbidden());

        Map<String, Object> cursoRequest = new HashMap<>();
        cursoRequest.put("nome", "Spring");
        cursoRequest.put("categoria", "BACKEND");

        mockMvc.perform(post("/cursos")
                .header("Authorization", bearer(userToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(cursoRequest)))
            .andExpect(status().isForbidden());

        mockMvc.perform(post("/cursos")
                .header("Authorization", bearer(adminToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(cursoRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome", is("Spring")));
    }

    @Test
    public void deveRejeitarPerfisForaDeAdminEAluno() throws Exception {
        mockMvc.perform(post("/perfis")
                .header("Authorization", bearer(autenticar(admin.getEmail())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(singletonRequest("nome", "ROLE_AUDITOR"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Perfil inválido. Perfis permitidos: ROLE_ADMIN e ROLE_ALUNO.")));
    }

    @Test
    public void alunoDeveCadastrarPesquisarEEditarDuvida() throws Exception {
        String userToken = autenticar(usuario.getEmail());

        Map<String, Object> duvidaRequest = new HashMap<>();
        duvidaRequest.put("titulo", "Como criar uma API?");
        duvidaRequest.put("descricao", "Preciso de ajuda com Spring Boot.");
        duvidaRequest.put("cursoId", curso.getId());

        String duvidaJson = mockMvc.perform(post("/duvidas")
                .header("Authorization", bearer(userToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(duvidaRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.usuarioId", is(usuario.getId().intValue())))
            .andExpect(jsonPath("$.duvidaStatus", is("NAO_RESPONDIDO")))
            .andReturn()
            .getResponse()
            .getContentAsString();

        Long duvidaId = objectMapper.readTree(duvidaJson).get("id").asLong();

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("titulo", "Como criar uma API REST?");
        updateRequest.put("descricao", "Preciso de ajuda com Spring Boot e camadas.");
        updateRequest.put("duvidaStatus", "NAO_SOLUCIONADO");
        updateRequest.put("cursoId", curso.getId());

        mockMvc.perform(put("/duvidas/{id}", duvidaId)
                .header("Authorization", bearer(userToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.titulo", is("Como criar uma API REST?")));

        mockMvc.perform(get("/duvidas/{id}", duvidaId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.duvidaStatus", is("NAO_SOLUCIONADO")))
            .andExpect(jsonPath("$.descricao", is("Preciso de ajuda com Spring Boot e camadas.")));
    }

    @Test
    public void deveRetornarConflitoAoRemoverCursoVinculado() throws Exception {
        Duvida duvida = new Duvida("Dúvida vinculada", "Descrição", curso, usuario);
        duvidaRepository.save(duvida);

        mockMvc.perform(delete("/cursos/{id}", curso.getId())
                .header("Authorization", bearer(autenticar(admin.getEmail()))))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status", is(409)));
    }

    @Test
    public void deveBloquearAlteracaoDeDuvidaPorUsuarioQueNaoEDono() throws Exception {
        Duvida duvida = duvidaRepository.save(new Duvida("Dúvida do usuário", "Descrição", curso, usuario));

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("titulo", "Tentativa de alteração");
        updateRequest.put("descricao", "Descrição alterada");
        updateRequest.put("duvidaStatus", "NAO_SOLUCIONADO");
        updateRequest.put("cursoId", curso.getId());

        mockMvc.perform(put("/duvidas/{id}", duvida.getId())
                .header("Authorization", bearer(autenticar(outroUsuario.getEmail())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(updateRequest)))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message", is("Acesso negado.")));
    }

    @Test
    public void alunoNaoDeveExcluirDuvidaNemCadastrarResposta() throws Exception {
        String userToken = autenticar(usuario.getEmail());
        Duvida duvida = duvidaRepository.save(new Duvida("Dúvida do usuário", "Descrição", curso, usuario));

        mockMvc.perform(delete("/duvidas/{id}", duvida.getId())
                .header("Authorization", bearer(userToken)))
            .andExpect(status().isForbidden());

        Map<String, Object> respostaRequest = new HashMap<>();
        respostaRequest.put("descricao", "Resposta do aluno.");
        respostaRequest.put("duvidaId", duvida.getId());

        mockMvc.perform(post("/respostas")
                .header("Authorization", bearer(userToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(respostaRequest)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void adminDeveExecutarTodasAsAcoes() throws Exception {
        String adminToken = autenticar(admin.getEmail());
        Duvida duvida = duvidaRepository.save(new Duvida("Dúvida administrativa", "Descrição", curso, usuario));
        Resposta resposta = respostaRepository.save(new Resposta("Resposta administrativa", admin, duvida));

        mockMvc.perform(get("/usuarios")
                .header("Authorization", bearer(adminToken)))
            .andExpect(status().isOk());

        mockMvc.perform(delete("/respostas/{id}", resposta.getId())
                .header("Authorization", bearer(adminToken)))
            .andExpect(status().isNoContent());

        mockMvc.perform(delete("/duvidas/{id}", duvida.getId())
                .header("Authorization", bearer(adminToken)))
            .andExpect(status().isNoContent());
    }

    @Test
    public void devePermitirAtualizarPerfisDoUsuarioPorAdmin() throws Exception {
        String adminToken = autenticar(admin.getEmail());

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("nome", usuario.getNome());
        updateRequest.put("email", usuario.getEmail());
        updateRequest.put("documento", usuario.getDocumento());
        updateRequest.put("usuarioStatus", "ATIVO");
        updateRequest.put("perfilIds", Arrays.asList(perfilAdmin.getId(), perfilAluno.getId()));

        mockMvc.perform(put("/usuarios/{id}", usuario.getId())
                .header("Authorization", bearer(adminToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.perfis", hasItems("ROLE_ADMIN", "ROLE_ALUNO")));
    }

    private String autenticar(String email) throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("email", email);
        request.put("senha", "123456");

        String response = mockMvc.perform(post("/auth/basica")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        return json.get("token").asText();
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    private String json(Object value) throws Exception {
        return objectMapper.writeValueAsString(value);
    }

    private Map<String, Object> singletonRequest(String key, Object value) {
        Map<String, Object> request = new HashMap<>();
        request.put(key, value);
        return request;
    }

    private Perfil salvarPerfil(String nome) {
        Perfil perfil = new Perfil();
        perfil.setNome(nome);
        return perfilRepository.save(perfil);
    }

    private Usuario salvarUsuario(String nome, String email, String documento, Perfil... perfis) {
        Usuario usuario = new Usuario(nome, email, documento, passwordEncoder.encode("123456"));
        usuario.setPerfis(Arrays.asList(perfis));
        return usuarioRepository.save(usuario);
    }
}
