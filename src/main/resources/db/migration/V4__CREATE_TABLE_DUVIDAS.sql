CREATE TABLE IF NOT EXISTS duvidas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    situacao VARCHAR(25) NOT NULL,
    data_criacao DATETIME NOT NULL,
    curso_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_duvidas_curso FOREIGN KEY (curso_id) REFERENCES cursos(id),
    CONSTRAINT fk_duvidas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
