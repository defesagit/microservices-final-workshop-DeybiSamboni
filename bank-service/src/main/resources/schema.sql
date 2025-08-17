CREATE TABLE IF NOT EXISTS banks (
    bank_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100) NOT NULL
);

-- Ejemplos de bancos
INSERT INTO banks (name, country) VALUES ('Banco de Colombia', 'Colombia');
INSERT INTO banks (name, country) VALUES ('Banco Popular', 'Colombia');
INSERT INTO banks (name, country) VALUES ('Citibank', 'Estados Unidos');
INSERT INTO banks (name, country) VALUES ('Santander', 'España');
INSERT INTO banks (name, country) VALUES ('HSBC', 'Reino Unido');
