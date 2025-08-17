
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_account VARCHAR(50),
    to_account VARCHAR(50),
    type VARCHAR(20),
    amount DECIMAL(19,2),
    timestamp TIMESTAMP
);