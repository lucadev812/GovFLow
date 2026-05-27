CREATE TABLE fornecedores(
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) not null,
    cnpj VARCHAR(20) not null UNIQUE ,
    email VARCHAR(100) UNIQUE,
    status VARCHAR(30)
)