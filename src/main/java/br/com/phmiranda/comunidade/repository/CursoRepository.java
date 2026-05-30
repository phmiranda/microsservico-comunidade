/*
 * Author: Pedro
 * Project: comunidade
 * User Story: 72
 * Description: Publicando Endpoints
 * Date: 25/03/2022
 */

package br.com.phmiranda.comunidade.repository;

import br.com.phmiranda.comunidade.domain.entity.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    Optional<Curso> findByNome(String nomeCurso);
    Page<Curso> findByCategoria(Pageable paginacao, String categoria);
    Page<Curso> findByCategoriaContainingIgnoreCase(Pageable paginacao, String categoria);
}
