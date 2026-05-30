package br.com.phmiranda.comunidade.domain.entity;

import br.com.phmiranda.comunidade.domain.enums.DuvidaStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "duvidas")
public class Duvida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "duvida")
    private List<Resposta> respostas = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false, length = 25)
    private DuvidaStatus duvidaStatus = DuvidaStatus.NAO_RESPONDIDO;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    public Duvida(String titulo, String descricao, Curso curso, Usuario usuario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.curso = curso;
        this.usuario = usuario;
    }

    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }
}
