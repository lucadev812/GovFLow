CREATE TABLE contratos (
                           id BIGSERIAL PRIMARY KEY,

                           numero VARCHAR(100) NOT NULL UNIQUE,

                           valor NUMERIC(15,2) NOT NULL,

                           data_inicio DATE NOT NULL,

                           data_fim DATE NOT NULL,

                           status VARCHAR(30) NOT NULL,

                           categoria_id BIGINT NOT NULL,

                           fornecedor_id BIGINT NOT NULL,

                           CONSTRAINT fk_contrato_categoria
                               FOREIGN KEY (categoria_id)
                                   REFERENCES categorias(id),

                           CONSTRAINT fk_contrato_fornecedor
                               FOREIGN KEY (fornecedor_id)
                                   REFERENCES fornecedores(id)
);