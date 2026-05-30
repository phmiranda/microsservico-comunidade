CREATE TABLE IF NOT EXISTS respostas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    descricao TEXT NOT NULL,
    data_criacao DATETIME NOT NULL,
    situacao BOOLEAN NOT NULL,
    usuario_id BIGINT NOT NULL,
    duvida_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_respostas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_respostas_duvida FOREIGN KEY (duvida_id) REFERENCES duvidas(id)
);
