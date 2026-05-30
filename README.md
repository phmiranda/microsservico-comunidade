# API da Plataforma da Comunidade Alura (Dúvidas)

> API de administração da dúvidas publicadas na comunidade de alunos da plataforma da Alura, este projeto foi desenvolvido utilizando o Spring Boot na versão **_2.1.4.RELEASE_**.

## Documentação

Com a aplicação em execução, a documentação Swagger fica disponível em:

- UI: `http://localhost:9000/api/comunidade/swagger-ui.html`
- JSON: `http://localhost:9000/api/comunidade/v2/api-docs`

Para testar endpoints protegidos na UI, autentique em `POST /auth/basica`, copie o token retornado e informe no botão `Authorize` usando o formato `Bearer <token>`.

## Execução Local

Por padrão a aplicação usa SQLite, sem necessidade de MySQL local.

```bash
mvn spring-boot:run
```

O arquivo do banco local fica em `./data/comunidade.sqlite`.

## Autorização

- Público: cadastro de usuário, login, consulta de cursos e consulta de dúvidas.
- `ROLE_ALUNO`: cadastro e edição das próprias dúvidas.
- `ROLE_ADMIN`: administração de usuários, perfis, cursos, respostas e remoção de dúvidas.
