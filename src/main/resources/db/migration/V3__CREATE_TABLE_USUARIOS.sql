CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(250) NOT NULL,
    email VARCHAR(150) NOT NULL,
    documento VARCHAR(11) NOT NULL,
    senha VARCHAR(250) NOT NULL,
    situacao VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_usuarios_email UNIQUE (email),
    CONSTRAINT uk_usuarios_documento UNIQUE (documento)
);
