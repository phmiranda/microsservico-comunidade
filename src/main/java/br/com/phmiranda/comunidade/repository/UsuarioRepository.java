/*
 * Author: phmiranda
 * Project: comunidade
 * Task Number: HU-XXX
 * Description: N/A
 * Date: 07/04/2022
 */

package br.com.phmiranda.comunidade.repository;

import br.com.phmiranda.comunidade.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNome(String nome);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByDocumento(String documento);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByDocumentoAndIdNot(String documento, Long id);
}
