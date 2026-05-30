/*
 * Author: Pedro
 * Project: comunidade
 * User Story: 72
 * Description: Publicando Endpoints
 * Date: 25/03/2022
 */

package br.com.phmiranda.comunidade.repository;

import br.com.phmiranda.comunidade.domain.entity.Duvida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DuvidaRepository extends JpaRepository<Duvida, Long> {
    Optional<Duvida> findByTitulo(String titulo);
    List<Duvida> findByCursoNome(String titulo);
    Page<Duvida> findByCursoId(Pageable pageable, Long cursoId);
    Page<Duvida> findByUsuarioId(Pageable pageable, Long usuarioId);
}
