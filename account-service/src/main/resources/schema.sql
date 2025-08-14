CREATE TABLE accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    holder VARCHAR(100) NOT NULL,
    bank_code VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO accounts (account_number, holder, bank_code, created_at) VALUES
('1001', 'Juan Pérez', 'BANCO01', CURRENT_TIMESTAMP),
('1002', 'Ana Gómez', 'BANCO02', CURRENT_TIMESTAMP),
('1003', 'Carlos Ruiz', 'BANCO03', CURRENT_TIMESTAMP);
