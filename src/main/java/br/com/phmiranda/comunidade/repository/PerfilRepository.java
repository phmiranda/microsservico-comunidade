package br.com.phmiranda.comunidade.repository;

import br.com.phmiranda.comunidade.domain.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByNome(String nome);
    boolean existsByNome(String nome);
    boolean existsByNomeAndIdNot(String nome, Long id);
}
