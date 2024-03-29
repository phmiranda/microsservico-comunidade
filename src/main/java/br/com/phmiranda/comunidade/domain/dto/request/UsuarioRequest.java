/*
 * Author: phmiranda
 * Project: comunidade
 * Task Number: 74
 * Description: Trabalhando com POST
 * Date: 28/03/2022
 */

package br.com.phmiranda.comunidade.domain.dto.request;

import br.com.phmiranda.comunidade.domain.entity.Usuario;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UsuarioRequest {
    @NotNull
    @NotEmpty
    private String nome;

    @Email
    @NotNull
    @NotEmpty
    private String email;

    @CPF
    @NotNull
    @NotEmpty
    private String documento;

    @NotNull
    @NotEmpty
    private String senha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Usuario converter() {
        return new Usuario(nome, email, documento, senha);
    }
}
