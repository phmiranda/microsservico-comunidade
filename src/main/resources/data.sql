INSERT INTO perfis(id, nome) VALUES(1, 'ROLE_ADMIN');
INSERT INTO perfis(id, nome) VALUES(2, 'ROLE_ALUNO');

INSERT INTO cursos(id, nome, categoria) VALUES(1, 'HTML/CSS', 'FRONTEND');
INSERT INTO cursos(id, nome, categoria) VALUES(2, 'Angular', 'FRONTEND');
INSERT INTO cursos(id, nome, categoria) VALUES(3, 'React Js', 'FRONTEND');
INSERT INTO cursos(id, nome, categoria) VALUES(4, 'React Native', 'MOBILE');
INSERT INTO cursos(id, nome, categoria) VALUES(5, 'Javascript', 'FRONTEND');
INSERT INTO cursos(id, nome, categoria) VALUES(6, 'Java SE', 'PROGRAMAÇÃO');
INSERT INTO cursos(id, nome, categoria) VALUES(7, 'Java EE', 'PROGRAMAÇÃO');
INSERT INTO cursos(id, nome, categoria) VALUES(8, 'Python', 'PROGRAMAÇÃO');
INSERT INTO cursos(id, nome, categoria) VALUES(9, 'Node JS', 'PROGRAMAÇÃO');
INSERT INTO cursos(id, nome, categoria) VALUES(10, 'PHP', 'PROGRAMAÇÃO');

INSERT INTO usuarios(id, documento, email, nome, senha, situacao) VALUES(1, '70436011000', 'admin@email.com.br', 'Administrador', '$2a$04$ygKK8TiwMgUA64teTjLk0eA5QBTRQX/Xym54qzoCk6dbd.dt8Y0OK', 'ATIVO');
INSERT INTO usuarios(id, documento, email, nome, senha, situacao) VALUES(2, '60172076013', 'debora.silva@email.com.br', 'Débora Viana da Silva', '$2a$04$ygKK8TiwMgUA64teTjLk0eA5QBTRQX/Xym54qzoCk6dbd.dt8Y0OK', 'ATIVO');
INSERT INTO usuarios(id, documento, email, nome, senha, situacao) VALUES(3, '33721917065', 'alexandre.silva@email.com.br', 'Alexander Vasconcelos da Silva', '$2a$04$ygKK8TiwMgUA64teTjLk0eA5QBTRQX/Xym54qzoCk6dbd.dt8Y0OK', 'ATIVO');

INSERT INTO usuarios_perfis(usuario_id, perfil_id) VALUES(1, 1);
INSERT INTO usuarios_perfis(usuario_id, perfil_id) VALUES(1, 2);
INSERT INTO usuarios_perfis(usuario_id, perfil_id) VALUES(2, 2);
INSERT INTO usuarios_perfis(usuario_id, perfil_id) VALUES(3, 2);

INSERT INTO duvidas(id, data_criacao, descricao, titulo, situacao, curso_id, usuario_id) VALUES(1, NOW(), 'CONSTRUINDO UMA API RESTFUL COM SPRING BOOT', '[BACKEND] - Dúvida 01', 'NAO_RESPONDIDO', 1, 1);
INSERT INTO duvidas(id, data_criacao, descricao, titulo, situacao, curso_id, usuario_id) VALUES(2, NOW(), 'CONSTRUINDO UMA API RESTFUL COM SPRING BOOT', '[FRONTEND] - Dúvida 02', 'NAO_RESPONDIDO', 2, 3);
INSERT INTO duvidas(id, data_criacao, descricao, titulo, situacao, curso_id, usuario_id) VALUES(3, NOW(), 'CONSTRUINDO UMA API RESTFUL COM SPRING BOOT', '[PROGRAMAÇÃO] - Dúvida 03', 'NAO_RESPONDIDO', 3, 2);

INSERT INTO respostas(id, data_criacao, descricao, situacao, duvida_id, usuario_id) VALUES (1,NOW(),'MENSAGEM DA DÚVIDA',FALSE, 1, 2);
INSERT INTO respostas(id, data_criacao, descricao, situacao, duvida_id, usuario_id) VALUES (2,NOW(),'MENSAGEM DA DÚVIDA',FALSE, 2, 1);
INSERT INTO respostas(id, data_criacao, descricao, situacao, duvida_id, usuario_id) VALUES (3,NOW(),'MENSAGEM DA DÚVIDA',FALSE, 3, 3);
