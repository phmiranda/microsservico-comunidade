CREATE TABLE IF NOT EXISTS perfis (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS cursos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome VARCHAR(250) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    documento VARCHAR(11) NOT NULL UNIQUE,
    senha VARCHAR(250) NOT NULL,
    situacao VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS duvidas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    situacao VARCHAR(25) NOT NULL,
    data_criacao DATETIME NOT NULL,
    curso_id INTEGER NOT NULL,
    usuario_id INTEGER NOT NULL,
    CONSTRAINT fk_duvidas_curso FOREIGN KEY (curso_id) REFERENCES cursos(id),
    CONSTRAINT fk_duvidas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS respostas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    descricao TEXT NOT NULL,
    data_criacao DATETIME NOT NULL,
    situacao INTEGER NOT NULL,
    usuario_id INTEGER NOT NULL,
    duvida_id INTEGER NOT NULL,
    CONSTRAINT fk_respostas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_respostas_duvida FOREIGN KEY (duvida_id) REFERENCES duvidas(id)
);

CREATE TABLE IF NOT EXISTS usuarios_perfis (
    usuario_id INTEGER NOT NULL,
    perfil_id INTEGER NOT NULL,
    PRIMARY KEY (usuario_id, perfil_id),
    CONSTRAINT fk_usuarios_perfis_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_usuarios_perfis_perfil FOREIGN KEY (perfil_id) REFERENCES perfis(id)
);
