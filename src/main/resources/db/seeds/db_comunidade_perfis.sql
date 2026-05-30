INSERT INTO db_comunidade.perfis (id, nome) VALUES (1, 'ROLE_ADMIN');
INSERT INTO db_comunidade.perfis (id, nome) VALUES (2, 'ROLE_ALUNO');

INSERT INTO db_comunidade.usuarios_perfis (usuario_id, perfil_id) VALUES (1, 1);
INSERT INTO db_comunidade.usuarios_perfis (usuario_id, perfil_id) VALUES (1, 2);
