package br.com.phmiranda.comunidade.domain.entity;

import br.com.phmiranda.comunidade.domain.enums.UsuarioStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(
    name = "usuarios",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuarios_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_usuarios_documento", columnNames = "documento")
    }
)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 250)
    private String nome;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "documento", nullable = false, length = 11)
    private String documento;

    @Column(name = "senha", nullable = false, length = 250)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, length = 10)
    private UsuarioStatus situacao = UsuarioStatus.ATIVO;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_perfis",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private List<Perfil> perfis = new ArrayList<>();

    public Usuario(String nome, String email, String documento, String senha) {
        this.nome = nome;
        this.email = email;
        this.documento = documento;
        this.senha = senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return UsuarioStatus.ATIVO.equals(situacao);
    }
}
