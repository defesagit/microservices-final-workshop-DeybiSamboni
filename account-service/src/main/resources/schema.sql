CREATE TABLE IF NOT EXISTS banks (
    bank_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    account_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    holder VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    bank_id BIGINT NOT NULL,
    FOREIGN KEY (bank_id) REFERENCES banks(bank_id) ON DELETE CASCADE
);

--INSERT INTO accounts (account_number, holder, bank_id, created_at) VALUES
--('1001', 'Juan Pérez', 1, CURRENT_TIMESTAMP),
--('1002', 'Ana Gómez', 2, CURRENT_TIMESTAMP),
--('1003', 'Carlos Ruiz', 3, CURRENT_TIMESTAMP);
